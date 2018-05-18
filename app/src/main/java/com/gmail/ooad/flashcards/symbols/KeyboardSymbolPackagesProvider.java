package com.gmail.ooad.flashcards.symbols;

import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackage;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackagesProvider;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 18.05.18.
 */
public class KeyboardSymbolPackagesProvider implements ISymbolsPackagesProvider {
    @Override
    public ArrayList<ISymbolsPackage> getPackages() {
        ArrayList<SymbolsPackageData> packages = SymbolsController.GetInstance().getPackageList();
        ArrayList<ISymbolsPackage> adapters = new ArrayList<>();
        if (packages != null) {
            for (SymbolsPackageData data :
                    packages) {
                adapters.add(new KeyboardPackageAdapter(data));
            }
        }
        return adapters;
    }
}
