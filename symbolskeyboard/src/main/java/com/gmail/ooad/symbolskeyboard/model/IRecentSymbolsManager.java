package com.gmail.ooad.symbolskeyboard.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 16.05.18.
 */
public interface IRecentSymbolsManager {
    @NonNull
    ArrayList<ISymbol> getRecentSymbols();

    void addSymbol(@NonNull ISymbol symbol);

    void persist();
}
