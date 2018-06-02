package com.gmail.ooad.symbolskeyboard.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 13.05.18.
 */
public interface ISymbolsPackage {
    @NonNull String getName();

    @NonNull
    ArrayList<ISymbol> getSymbols();
}
