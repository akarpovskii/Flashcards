package com.gmail.ooad.flashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class DatabaseAdapter extends SQLiteOpenHelper {
    /**
     * schema: Table 'packages' contains packages data (surprisingly) without cards;
     *         table 'cards'    contains all cards one after another with column 'package'
     *         So to query or to store cards we first need to check if the package exists,
     *         and then go to 'cards' table to query all cards where 'PACKAGE' value equals 'package'.
     */

    private static final String DatabaseName = "cards.db";

    private static final int DatabaseVersion = 1;



    private static final String PackagesTable = "PACKAGES";

    private static final String PackageName = "NAME";

    private static final String PackageColor = "COLOR";



    private static final String CardsTable = "CARDS";

    private static final String CardPackage = "PACKAGE";

    private static final String CardName = "NAME";

    private static final String CardFront = "FRONT";

    private static final String CardBack = "BACK";

    public DatabaseAdapter(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            final String CreatePackagesTable = "create table " + PackagesTable + "(" +
                    "rowid integer primary key, " +
                    PackageName + " text not null," +
                    PackageColor + " integer not null);";

            final String CreateCardsTable = "create table " + CardsTable + "(" +
                    "rowid integer primary key, " +
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

    public ArrayList<PackageData> getPackageList() {
        return null;
    }

    public PackageData getPackage(String name) {
        return null;
    }

    public CardData getCard(String packageName, String cardName) {
        return null;
    }

    public void addPackage(PackageData packageData) {

    }

    public void addCard(String packageName, CardData cardData) {

    }

    public void updatePackage(PackageData packageData) {

    }

    public void updateCard(String packageName, CardData cardData) {

    }
}
