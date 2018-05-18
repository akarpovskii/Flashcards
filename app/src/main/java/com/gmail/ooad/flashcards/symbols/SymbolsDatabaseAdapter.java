package com.gmail.ooad.flashcards.symbols;

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

import com.gmail.ooad.flashcards.utils.SymbolsUtils;

/*
 * Created by akarpovskii on 09.05.18.
 */
public class SymbolsDatabaseAdapter extends SQLiteOpenHelper {
    private static final String DatabaseName = "symbols.db";

    private static final int DatabaseVersion = 1;

    private static final String Table = "SYMBOLS_PACKAGES";

    private static final String Name = "NAME";

    private static final String Symbols = "SYMBOLS";
    private final Context mContext;

    public SymbolsDatabaseAdapter(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            final String CreatePackagesTable = "create table " + Table + "(" +
                    "ID integer primary key, " +
                    Name + " text not null," +
                    Symbols + " text);";
            Log.i("Flashcards", "Creating symbols database...");
            db.execSQL(CreatePackagesTable);

            for (SymbolsPackageData data :
                    SymbolsExampleDataHelper.GetExampleData(mContext)) {
                addPackage(db, data);
            }
        } catch (Exception ex) {
            Log.e("Flashcards", ex.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Should be appropriately changed before upgrade to a new version, or we will lose all the data
        db.execSQL("drop table if exists " + Table);
        onCreate(db);
    }

    @Nullable ArrayList<SymbolsPackageData> getPackageList() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + Table, null);

        ArrayList<SymbolsPackageData> packages = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(Name));
            String symbolsStr = cursor.getString(cursor.getColumnIndex(Symbols));

            SymbolsPackageData data = new SymbolsPackageData(name, SymbolsUtils.StringToSymbols(symbolsStr));

            packages.add(data);
        }

        cursor.close();
        db.close();
        return packages;
    }

    @Nullable SymbolsPackageData getPackage(@NonNull String name) {
        String where = Name + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Table, new String[]{Symbols}, where,
                new String[]{name}, null, null, null);

        cursor.moveToFirst();
        String symbols = cursor.getString(cursor.getColumnIndex(Symbols));

        cursor.close();

        db.close();
        return new SymbolsPackageData(name, SymbolsUtils.StringToSymbols(symbols));
    }

    private boolean addPackage(SQLiteDatabase db, @NonNull SymbolsPackageData packageData) {
        if (hasPackage(db, packageData.getName())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(Name, packageData.getName());
        values.put(Symbols, SymbolsUtils.SymbolsToString(packageData.getSymbols()));

        return db.insert(Table, null, values) != -1;
    }

    boolean addPackage(@NonNull SymbolsPackageData packageData) {
        SQLiteDatabase db = getWritableDatabase();
        boolean success = addPackage(db, packageData);
        db.close();
        return success;
    }

    boolean addSymbol(@NonNull String packageName, Symbol symbol) {
        SymbolsPackageData packageData = getPackage(packageName);
        if (packageData == null ) {
            return false;
        }
        ArrayList<Symbol> symbols = packageData.getSymbols();
        if (symbols.contains(symbol)) {
            return true;
        }
        symbols.add(symbol);
        packageData.setSymbols(symbols);
        return updatePackage(packageData, null);
    }

    boolean updatePackage(SymbolsPackageData packageData, @Nullable String oldName) {
        String newName = packageData.getName();
        boolean newNameQuarried = oldName != null && !newName.equals(oldName);

        if (newNameQuarried && hasPackage(newName)) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if (newNameQuarried) {
            values.put(Name, newName);
        }
        values.put(Symbols, SymbolsUtils.SymbolsToString(packageData.getSymbols()));

        String where = Name + "=?";

        if (oldName == null) {
            oldName = newName;
        }

        int count = db.update(Table, values, where, new String[]{oldName});
        if (BuildConfig.DEBUG) {
            if (count != 1) throw new AssertionError();
        }

        db.close();
        return true;
    }

    private boolean hasPackage (SQLiteDatabase db, @NonNull String name) {
        String query = "select * from " + Table + " where " + Name + "=?";
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

    void removePackage(@NonNull String name) {
        SQLiteDatabase db = getWritableDatabase();

        String where = Name + "=?";
        db.delete(Table, where, new String[]{name});

        db.close();
    }

    void removeSymbol(@NonNull String packageName, @NonNull Symbol symbol) {
        SymbolsPackageData packageData = getPackage(packageName);
        if (packageData == null ) {
            return;
        }
        ArrayList<Symbol> symbols = packageData.getSymbols();
        symbols.remove(symbol);
        packageData.setSymbols(symbols);
        updatePackage(packageData, null);
    }
}
