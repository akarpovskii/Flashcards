package com.gmail.ooad.flashcards.symbols;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gmail.ooad.flashcards.MyApplication;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 09.05.18.
 */
public class SymbolsController {
    private static SymbolsController mInstance;

    private SymbolsDatabaseAdapter mSymbolsDatabase;

    private SymbolsController() {
        mSymbolsDatabase = new SymbolsDatabaseAdapter(MyApplication.GetInstance().getApplicationContext());
    }

    public static synchronized @NonNull SymbolsController GetInstance() {
        if (mInstance == null) {
            mInstance = new SymbolsController();
        }
        return mInstance;
    }

    public @Nullable
    ArrayList<SymbolsPackageData> getPackageList() {
        return mSymbolsDatabase.getPackageList();
    }

    @Nullable SymbolsPackageData getPackage(@NonNull String name) {
        return mSymbolsDatabase.getPackage(name);
    }

    boolean addPackage(@NonNull SymbolsPackageData packageData) {
        return mSymbolsDatabase.addPackage(packageData);
    }

    boolean addSymbol(@NonNull String packageName, Symbol symbol) {
        return mSymbolsDatabase.addSymbol(packageName, symbol);
    }

    boolean updatePackage(SymbolsPackageData packageData, @Nullable String oldName) {
        return mSymbolsDatabase.updatePackage(packageData, oldName);
    }

    boolean hasPackage(@NonNull String name) {
        return mSymbolsDatabase.hasPackage(name);
    }

    void removePackage(@NonNull String name) {
        mSymbolsDatabase.removePackage(name);
    }

    void removeSymbol(@NonNull String packageName, @NonNull Symbol symbol) {
        mSymbolsDatabase.removeSymbol(packageName, symbol);
    }
}
