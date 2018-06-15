package com.gmail.ooad.flashcards.cards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gmail.ooad.flashcards.BuildConfig;
import com.gmail.ooad.flashcards.utils.ColorPalette;
import com.gmail.ooad.flashcards.utils.ColorUtil;
import com.gmail.ooad.flipablecardview.ICardData;

import java.util.ArrayList;

/**
 * Created by akarpovskii on 14.04.18.
 */

class CardsDatabaseAdapter extends SQLiteOpenHelper {
    /**
     * schema: Table 'packages' contains packages data (surprisingly) without cards;
     *         table 'cards'    contains all the cards one after another with column 'PACKAGE'
     *         So to query or to store cards we first need to check if the package exists,
     *         and then go to 'cards' table and query all the cards where 'PACKAGE' value equals 'package'.
     */

    private static final String DATABASE_NAME = "cards.db";

    private static final int DATABASE_VERSION = 4;



    private static final String PACKAGES_TABLE = "PACKAGES";

    private static final String PACKAGE_NAME = "NAME";

    private static final String PACKAGE_COLOR_OLD = "COLOR";

    private static final String PACKAGE_PALETTE = "PALETTE";

    private static final String PACKAGE_CARDS_COLOR = "CARDS_ALPHA";    // leave the column name unchanged



    private static final String CARDS_TABLE = "CARDS";

    private static final String CARD_PACKAGE = "PACKAGE";

    private static final String CARD_NAME = "NAME";

    private static final String CARD_FRONT = "FRONT";

    private static final String CARD_BACK = "BACK";

    private static final String CREATE_PACKAGES_TABLE = "create table " + PACKAGES_TABLE + "(" +
            "ID integer primary key, " +
            PACKAGE_NAME + " text not null," +
            PACKAGE_PALETTE + " text not null," +
            PACKAGE_CARDS_COLOR + " integer not null" + ");";

    private static final String CREATE_CARDS_TABLE = "create table " + CARDS_TABLE + "(" +
            "ID integer primary key, " +
            CARD_PACKAGE + " text not null," +
            CARD_NAME + " text not null," +
            CARD_FRONT + " text," +
            CARD_BACK + " text);";

    private final Context mContext;

    CardsDatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i("Flashcards", "Creating cards database...");
            db.execSQL(CREATE_PACKAGES_TABLE);
            db.execSQL(CREATE_CARDS_TABLE);

            addPackage(db, CardsExampleDataHelper.GetExampleData(mContext));
        } catch (Exception ex) {
            Log.e("Flashcards", ex.toString());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Should be appropriately changed before upgrade to a new version, or we will lose all the data
        if (oldVersion < 3) {
            final String OldName = PACKAGES_TABLE + "_old";
            db.beginTransaction();

            try {
                db.execSQL("alter table " + PACKAGES_TABLE + " rename to " + OldName + ";");
                db.execSQL(CREATE_PACKAGES_TABLE);

                Cursor cursor = db.rawQuery("select * from " + OldName, null);

                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(PACKAGE_NAME));
                    int color = cursor.getColumnIndex(PACKAGE_COLOR_OLD);
                    ColorPalette palette = ColorUtil.GetNearest(color);

                    CardsPackageData data = new CardsPackageData(name,
                            new PackagePalette(palette, palette.getPrimary()),null);
                    addPackage(db, data);
                }

                cursor.close();

                db.execSQL("drop table " + OldName + ";");

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (oldVersion < 4) {
            db.execSQL("alter table " + PACKAGES_TABLE +
                    " add column " + PACKAGE_CARDS_COLOR + " integer default 255;");
        }
    }

    @NonNull ArrayList<CardsPackageDataProxy> getPackageList() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + PACKAGES_TABLE, null);

        ArrayList<CardsPackageDataProxy> packages = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(PACKAGE_NAME));
            ColorPalette palette = ColorPalette.fromValue(
                    cursor.getString(cursor.getColumnIndex(PACKAGE_PALETTE)));
            int alpha = cursor.getInt(cursor.getColumnIndex(PACKAGE_CARDS_COLOR));

            CardsPackageDataProxy data = new CardsPackageDataProxy(name, new PackagePalette(palette, alpha));

            packages.add(data);
        }

        cursor.close();
        db.close();
        return packages;
    }

    CardsPackageDataProxy getPackage(@NonNull String name) {
        String where = PACKAGE_NAME + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(PACKAGES_TABLE, new String[]{PACKAGE_PALETTE}, where,
                new String[]{name}, null, null, null);

        cursor.moveToFirst();
        ColorPalette palette = ColorPalette.fromValue(
                cursor.getString(cursor.getColumnIndex(PACKAGE_PALETTE)));
        int alpha = cursor.getInt(cursor.getColumnIndex(PACKAGE_CARDS_COLOR));

        cursor.close();

        db.close();
        return new CardsPackageDataProxy(name, new PackagePalette(palette, alpha));
    }

    ArrayList<CardDataProxy> getPackageCards(@NonNull String name) {
        SQLiteDatabase db = getReadableDatabase();

        String where = CARD_PACKAGE + "=?";
        Cursor cursor = db.query(CARDS_TABLE, new String[]{CARD_NAME}, where,
                new String[]{name}, null, null, null);
        ArrayList<CardDataProxy> cards = new ArrayList<>();

        while (cursor.moveToNext()) {
            String cardName = cursor.getString(cursor.getColumnIndex(CARD_NAME));

            cards.add(new CardDataProxy(name, cardName));
        }

        cursor.close();

        return cards;
    }

    CardData getCard(@NonNull String packageName, @NonNull String cardName) {
        String where = CARD_PACKAGE + "=? and " + CARD_NAME + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(CARDS_TABLE, null, where,
                new String[]{packageName, cardName}, null, null, null);

        cursor.moveToFirst();
        String cardFront = cursor.getString(cursor.getColumnIndex(CARD_FRONT));
        String cardBack = cursor.getString(cursor.getColumnIndex(CARD_BACK));

        cursor.close();
        db.close();
        return new CardData(cardName, cardFront, cardBack);
    }

    private boolean addPackage(SQLiteDatabase db, @NonNull ICardsPackageData packageData) {
        if (hasPackage(db, packageData.getName())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, packageData.getName());
        values.put(PACKAGE_PALETTE, packageData.getPalette().toValue());
        values.put(PACKAGE_CARDS_COLOR, packageData.getPalette().getCardsColor());

        boolean success = db.insert(PACKAGES_TABLE, null, values) != -1;
        if (success) {
            ArrayList<ICardData> cards = packageData.getCards();
            if (cards != null) {
                for (ICardData card :
                        cards) {
                    success &= addCard(db, packageData.getName(), card);
                }
            }
        }
        return success;
    }
    boolean addPackage(@NonNull ICardsPackageData packageData) {
        SQLiteDatabase db = getWritableDatabase();
        boolean success = addPackage(db, packageData);
        db.close();
        return success;
    }

    private boolean addCard(SQLiteDatabase db, @NonNull String packageName, @NonNull ICardData cardData) {
        if (hasCard(db, packageName, cardData.getName())) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(CARD_PACKAGE, packageName);
        values.put(CARD_NAME, cardData.getName());
        values.put(CARD_FRONT, cardData.getFront());
        values.put(CARD_BACK, cardData.getBack());

        return db.insert(CARDS_TABLE, null, values) != -1;
    }

    boolean addCard(@NonNull String packageName, @NonNull ICardData cardData) {
        SQLiteDatabase db = getWritableDatabase();
        boolean success = addCard(db, packageName, cardData);
        db.close();
        return success;
    }

    /**
     *
     * @param oldPackageName - if null, the name remains unchanged
     */
    boolean updatePackage(@NonNull ICardsPackageData packageData, @Nullable String oldPackageName) {
        String newPackageName = packageData.getName();
        boolean newNameQuarried = oldPackageName != null && !newPackageName.equals(oldPackageName);

        if (newNameQuarried && hasPackage(newPackageName)) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if (newNameQuarried) {
            values.put(PACKAGE_NAME, newPackageName);
        }
        values.put(PACKAGE_PALETTE, packageData.getPalette().toValue());
        values.put(PACKAGE_CARDS_COLOR, packageData.getPalette().getCardsColor());

        String where = PACKAGE_NAME + "=?";

        if (oldPackageName == null) {
            oldPackageName = newPackageName;
        }

        int count = db.update(PACKAGES_TABLE, values, where, new String[]{oldPackageName});
        if (BuildConfig.DEBUG) {
            if (count != 1) throw new AssertionError();
        }

        // Update cards
        if (newNameQuarried) {
            values = new ContentValues();
            values.put(CARD_PACKAGE, newPackageName);

            where = CARD_PACKAGE + "=?";
            db.update(CARDS_TABLE, values, where, new String[]{oldPackageName});
        }
        db.close();
        return true;
    }

    /**
     *
     * @param oldCardName - if null, the name remains unchanged
     */
    boolean updateCard(@NonNull String packageName, @NonNull ICardData cardData,
                       @Nullable String oldCardName) {

        String newCardName = cardData.getName();
        boolean newNameQuarried = oldCardName != null && !newCardName.equals(oldCardName);

        if (newNameQuarried && hasCard(packageName, newCardName)) {
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if (newNameQuarried) {
            values.put(CARD_NAME, newCardName);
        }
        values.put(CARD_FRONT, cardData.getFront());
        values.put(CARD_BACK, cardData.getBack());

        String where = CARD_PACKAGE + "=?" + " and " + CARD_NAME + "=?";

        if (oldCardName == null) {
            oldCardName = newCardName;
        }

        int count = db.update(CARDS_TABLE, values, where, new String[]{packageName, oldCardName});
        if (BuildConfig.DEBUG) {
            if (count != 1) throw new AssertionError();
        }

        db.close();
        return true;
    }

    boolean hasPackage(SQLiteDatabase db, @NonNull String name) {
        String query = "select * from " + PACKAGES_TABLE + " where " + PACKAGE_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        boolean res = cursor.getCount() > 0;
        cursor.close();
        return res;
    }

    boolean hasPackage(@NonNull String name) {
        SQLiteDatabase db = getReadableDatabase();
        boolean res = hasPackage(db, name);
        db.close();
        return res;
    }

    private boolean hasCard(SQLiteDatabase db, @NonNull String packageName, @NonNull String cardName) {
        String query = "select * from " + CARDS_TABLE + " where " + CARD_PACKAGE + "=? and " + CARD_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{packageName, cardName});
        boolean res = cursor.getCount() > 0;

        cursor.close();
        return res;
    }

    boolean hasCard(@NonNull String packageName, @NonNull String cardName) {
        SQLiteDatabase db = getReadableDatabase();
        boolean res = hasCard(db, packageName, cardName);
        db.close();
        return res;
    }

    void removePackage(@NonNull String packageName) {
        SQLiteDatabase db = getWritableDatabase();

        String where = PACKAGE_NAME + "=?";
        db.delete(PACKAGES_TABLE, where, new String[]{packageName});

        where = CARD_PACKAGE + "=?";
        db.delete(CARDS_TABLE, where, new String[]{packageName});

        db.close();
    }

    void removeCard(@NonNull String packageName, @NonNull String cardName) {
        SQLiteDatabase db = getWritableDatabase();

        String where = CARD_PACKAGE + "=? and " + CARD_NAME + "=?";
        db.delete(CARDS_TABLE, where, new String[]{packageName, cardName});

        db.close();
    }
}
