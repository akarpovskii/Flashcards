package com.gmail.ooad.flashcards.symbols;

import android.os.Parcelable;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 09.05.18.
 */
public interface ISymbolsPackageData extends Parcelable {
    String getName();

    void setName(String name);

    ArrayList<Symbol> getSymbols();

    void setSymbols(ArrayList<Symbol> symbols);
}
