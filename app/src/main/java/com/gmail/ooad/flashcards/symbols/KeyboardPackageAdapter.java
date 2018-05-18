package com.gmail.ooad.flashcards.symbols;

import android.support.annotation.NonNull;

import com.gmail.ooad.symbolskeyboard.model.ISymbol;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackage;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 18.05.18.
 */
public class KeyboardPackageAdapter implements ISymbolsPackage{
    private final ISymbolsPackageData mPackage;

    public KeyboardPackageAdapter(ISymbolsPackageData pack) {
        mPackage = pack;
    }

    @NonNull
    @Override
    public String getName() {
        return mPackage.getName();
    }

    @NonNull
    @Override
    public ArrayList<ISymbol> getSymbols() {
        return new ArrayList<>(mPackage.getSymbols());
    }
}
