package com.gmail.ooad.flashcards.utils;

import android.os.Build;

import com.gmail.ooad.flashcards.symbols.Symbol;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 18.05.18.
 */
public class SymbolsUtils {
    public static ArrayList<Symbol> StringToSymbols(String symbolsStr) {
        if (symbolsStr == null) {
            return null;
        }
        ArrayList<Symbol> symbols = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= 24) {
            for (int c : (Iterable<Integer>) symbolsStr.codePoints()::iterator) {
                symbols.add(new Symbol(c));
            }
        } else {
            final int length = symbolsStr.length();
            for (int offset = 0; offset < length; ) {
                final int c = symbolsStr.codePointAt(offset);

                symbols.add(new Symbol(c));

                offset += Character.charCount(c);
            }
        }
        return symbols;
    }

    public static String SymbolsToString(ArrayList<Symbol> symbols) {
        StringBuilder builder = new StringBuilder();
        for (ISymbol s :
                symbols) {
            builder.append(s.getUnicode());
        }
        return builder.toString();
    }
}
