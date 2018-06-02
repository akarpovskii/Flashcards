package com.gmail.ooad.symbolskeyboardapp;

import android.support.annotation.NonNull;

import com.gmail.ooad.symbolskeyboard.model.ISymbol;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackage;
import com.gmail.ooad.symbolskeyboard.model.SimpleSymbol;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 16.05.18.
 */
public class TestPackage implements ISymbolsPackage {
    private static final ArrayList<ISymbol> DATA = new ArrayList<ISymbol>() {{
        add(new SimpleSymbol('B'));
        add(new SimpleSymbol('C'));
        add(new SimpleSymbol('D'));
    }};

    @NonNull
    @Override
    public String getName() {
        return "test";
    }

    @NonNull
    @Override
    public ArrayList<ISymbol> getSymbols() {
        return DATA;
    }
}
