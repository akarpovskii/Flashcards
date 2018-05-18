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

    private static final String DatabaseName = "cards.db";

    private static final int DatabaseVersion = 2;



    private static final String PackagesTable = "PACKAGES";

    private static final String PackageName = "NAME";

    private static final String PackageColor = "COLOR";



    private static final String CardsTable = "CARDS";

    private static final String CardPackage = "PACKAGE";

    private static final String CardName = "NAME";

    private static final String CardFront = "FRONT";

    private static final String CardBack = "BACK";

    private final Context mContext;

    CardsDatabaseAdapter(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            final String CreatePackagesTable = "create table " + PackagesTable + "(" +
                    "ID integer primary key, " +
                    PackageName + " text not null," +
                    PackageColor + " integer not null);";

            final String CreateCardsTable = "create table " + CardsTable + "(" +
                    "ID integer primary key, " +
                    CardPackage + " text not null," +
                    CardName + " text not null," +
                    CardFront + " text," +
                    CardBack + " text);";


            Log.i("Flashcards", "Creating cards database...");
            db.execSQL(CreatePackagesTable);
            db.execSQL(CreateCardsTable);

            addPackage(db, CardsExampleDataHelper.GetExampleData(mContext));
        } catch (Exception ex) {
            Log.e("Flashcards", ex.toString());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Should be appropriately changed before upgrade to a new version, or we will lose all the data
        db.execSQL("drop table if exists " + PackagesTable);
        db.execSQL("drop table if exists " + CardsTable);
        onCreate(db);
    }

    @NonNull ArrayList<CardsPackageDataProxy> getPackageList() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + PackagesTable, null);

        ArrayList<CardsPackageDataProxy> packages = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(PackageName));
            int color = cursor.getInt(cursor.getColumnIndex(PackageColor));

            CardsPackageDataProxy data = new CardsPackageDataProxy(name, color);

            packages.add(data);
        }

        cursor.close();
        db.close();
        return packages;
    }

    CardsPackageDataProxy getPackage(@NonNull String name) {
        String where = PackageName + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(PackagesTable, new String[]{PackageColor}, where,
                new String[]{name}, null, null, null);

        cursor.moveToFirst();
        int color = cursor.getInt(cursor.getColumnIndex(PackageColor));

        cursor.close();

        db.close();
        return new CardsPackageDataProxy(name, color);
    }

    ArrayList<CardDataProxy> getPackageCards(@NonNull String name) {
        SQLiteDatabase db = getReadableDatabase();

        String where = CardPackage + "=?";
        Cursor cursor = db.query(CardsTable, new String[]{CardName}, where,
                new String[]{name}, null, null, null);
        ArrayList<CardDataProxy> cards = new ArrayList<>();

        while (cursor.moveToNext()) {
            String cardName = cursor.getString(cursor.getColumnIndex(CardName));

            cards.add(new CardDataProxy(name, cardName));
        }

        cursor.close();

        return cards;
    }

    CardData getCard(@NonNull String packageName, @NonNull String cardName) {
        String where = CardPackage + "=? and " + CardName + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(CardsTable, null, where,
                new String[]{packageName, cardName}, null, null, null);

        cursor.moveToFirst();
        String cardFront = cursor.getString(cursor.getColumnIndex(CardFront));
        String cardBack = cursor.getString(cursor.getColumnIndex(CardBack));

        cursor.close();
        db.close();
        return new CardData(cardName, cardFront, cardBack);
    }

    private boolean addPackage(SQLiteDatabase db, @NonNull ICardsPackageData packageData) {
        if (hasPackage(db, packageData.getName())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(PackageName, packageData.getName());
        values.put(PackageColor, packageData.getColor());

        boolean success = db.insert(PackagesTable, null, values) != -1;
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
        values.put(CardPackage, packageName);
        values.put(CardName, cardData.getName());
        values.put(CardFront, cardData.getFront());
        values.put(CardBack, cardData.getBack());

        return db.insert(CardsTable, null, values) != -1;
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
            values.put(PackageName, newPackageName);
        }
        values.put(PackageColor, packageData.getColor());

        String where = PackageName + "=?";

        if (oldPackageName == null) {
            oldPackageName = newPackageName;
        }

        int count = db.update(PackagesTable, values, where, new String[]{oldPackageName});
        if (BuildConfig.DEBUG) {
            if (count != 1) throw new AssertionError();
        }

        // Update cards
        if (newNameQuarried) {
            values = new ContentValues();
            values.put(CardPackage, newPackageName);

            where = CardPackage + "=?";
            db.update(CardsTable, values, where, new String[]{oldPackageName});
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
            values.put(CardName, newCardName);
        }
        values.put(CardFront, cardData.getFront());
        values.put(CardBack, cardData.getBack());

        String where = CardPackage + "=?" + " and " + CardName + "=?";

        if (oldCardName == null) {
            oldCardName = newCardName;
        }

        int count = db.update(CardsTable, values, where, new String[]{packageName, oldCardName});
        if (BuildConfig.DEBUG) {
            if (count != 1) throw new AssertionError();
        }

        db.close();
        return true;
    }

    boolean hasPackage(SQLiteDatabase db, @NonNull String name) {
        String query = "select * from " + PackagesTable + " where " + PackageName + "=?";
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
        String query = "select * from " + CardsTable + " where " + CardPackage + "=? and " + CardName + "=?";
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

        String where = PackageName + "=?";
        db.delete(PackagesTable, where, new String[]{packageName});

        where = CardPackage + "=?";
        db.delete(CardsTable, where, new String[]{packageName});

        db.close();
    }

    void removeCard(@NonNull String packageName, @NonNull String cardName) {
        SQLiteDatabase db = getWritableDatabase();

        String where = CardPackage + "=? and " + CardName + "=?";
        db.delete(CardsTable, where, new String[]{packageName, cardName});

        db.close();
    }
}
