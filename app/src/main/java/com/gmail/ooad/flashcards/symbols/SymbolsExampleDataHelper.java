package com.gmail.ooad.flashcards.symbols;

import android.content.Context;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.SymbolsUtils;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 18.05.18.
 */
class SymbolsExampleDataHelper {
    static SymbolsPackageData[] GetExampleData(Context context) {
        return new SymbolsPackageData[]{
                new SymbolsPackageData(context.getString(R.string.example_symbols_math_title),
                        new ArrayList<Symbol>() {{
                            addAll(SymbolsUtils.StringToSymbols(context.getString(R.string.example_symbols_math_symbols)));
                        }}),

                new SymbolsPackageData(context.getString(R.string.example_symbols_chem_title),
                        new ArrayList<Symbol>() {{
                            addAll(SymbolsUtils.StringToSymbols(context.getString(R.string.example_symbols_chem_symbols)));
                        }})
        };
    }
}
