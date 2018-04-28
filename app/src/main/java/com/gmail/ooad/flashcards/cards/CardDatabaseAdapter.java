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

class CardDatabaseAdapter extends SQLiteOpenHelper {
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

    CardDatabaseAdapter(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
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


            Log.i("Flashcards", "Creating database...");
            sqLiteDatabase.execSQL(CreatePackagesTable);
            sqLiteDatabase.execSQL(CreateCardsTable);
        } catch (Exception ex) {
            Log.e("Flashcards", ex.toString());
        }
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Should be appropriately changed before upgrade to a new version, or we will lose all the data
        sqLiteDatabase.execSQL("drop table if exists " + PackagesTable);
        sqLiteDatabase.execSQL("drop table if exists " + CardsTable);
        onCreate(sqLiteDatabase);
    }

    /**
     * @return packages with no card. You need to call getPackage explicitly to query its cards
     */
    @NonNull ArrayList<PackageData> getPackageList() {
        ArrayList<PackageData> packages = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + PackagesTable, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(PackageName));
            int color = cursor.getInt(cursor.getColumnIndex(PackageColor));

            PackageData data = new PackageData(name, color, null);

            packages.add(data);
        }

        cursor.close();
        db.close();
        return packages;
    }

    PackageData getPackage(@NonNull String name) {
        String where = PackageName + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(PackagesTable, new String[]{PackageColor}, where,
                new String[]{name}, null, null, null);

        cursor.moveToFirst();
        int color = cursor.getInt(cursor.getColumnIndex(PackageColor));

        cursor.close();

        where = CardPackage + "=?";
        cursor = db.query(CardsTable, null, where,
                new String[]{name}, null, null, null);
        ArrayList<CardData> cards = new ArrayList<>();

        while (cursor.moveToNext()) {
            String cardName = cursor.getString(cursor.getColumnIndex(CardName));
            String cardFront = cursor.getString(cursor.getColumnIndex(CardFront));
            String cardBack = cursor.getString(cursor.getColumnIndex(CardBack));

            cards.add(new CardData(cardName, cardFront, cardBack));
        }

        cursor.close();
        db.close();
        return new PackageData(name, color, cards);
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

    boolean addPackage(@NonNull PackageData packageData) {
        if (hasPackage(packageData.getName())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(PackageName, packageData.getName());
        values.put(PackageColor, packageData.getColor());

        SQLiteDatabase db = getWritableDatabase();
        boolean success = db.insert(PackagesTable, null, values) != -1;
        if (!success) {
            return false;
        }
        ArrayList<CardData> cards = packageData.getCards();
        if (cards != null) {
            for (CardData card :
                    cards) {
                success &= addCard(packageData.getName(), card);
            }
        }
        db.close();
        return success;
    }

    boolean addCard(@NonNull String packageName, @NonNull CardData cardData) {
        if (hasCard(packageName, cardData.getName())) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(CardPackage, packageName);
        values.put(CardName, cardData.getName());
        values.put(CardFront, cardData.getFront());
        values.put(CardBack, cardData.getBack());

        SQLiteDatabase db = getWritableDatabase();
        boolean success = db.insert(CardsTable, null, values) != -1;

        db.close();
        return success;
    }

    /**
     *
     * @param oldPackageName - if null, the name remains unchanged
     */
    boolean updatePackage(@NonNull PackageData packageData, @Nullable String oldPackageName) {
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
    boolean updateCard(@NonNull String packageName, @NonNull CardData cardData,
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

    boolean hasPackage(@NonNull String name) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from " + PackagesTable + " where " + PackageName + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        boolean res = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return res;
    }

    boolean hasCard(@NonNull String packageName, @NonNull String cardName) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from " + CardsTable + " where " + CardPackage + "=? and " + CardName + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{packageName, cardName});
        boolean res = cursor.getCount() > 0;

        cursor.close();
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
