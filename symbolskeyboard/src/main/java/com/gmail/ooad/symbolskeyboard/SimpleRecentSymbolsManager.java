package com.gmail.ooad.symbolskeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.gmail.ooad.symbolskeyboard.model.IRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;
import com.gmail.ooad.symbolskeyboard.model.SimpleSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class SimpleRecentSymbolsManager implements IRecentSymbolsManager {
    private static final String PREFERENCE_NAME = "symbol-recent-manager";
    private static final String TIME_DELIMITER = ";";
    private static final String SYMBOL_DELIMITER = "~";
    private static final String RECENT_SYMBOLS = "recent-symbols";
    private static final int SYMBOL_GUESS_SIZE = 5;
    private static final int MAX_RECENTS = 40;

    @NonNull private final Context context;
    @NonNull private SymbolsList symbolsList = new SymbolsList(0);

    public SimpleRecentSymbolsManager(@NonNull final Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull public ArrayList<ISymbol> getRecentSymbols() {
        if (symbolsList.size() == 0) {
            final String savedRecent = getPreferences().getString(RECENT_SYMBOLS, "");

            if (savedRecent.length() > 0) {
                final StringTokenizer stringTokenizer = new StringTokenizer(savedRecent, SYMBOL_DELIMITER);
                symbolsList = new SymbolsList(stringTokenizer.countTokens());

                while (stringTokenizer.hasMoreTokens()) {
                    final String token = stringTokenizer.nextToken();

                    final String[] parts = token.split(TIME_DELIMITER);

                    if (parts.length == 2) {
                        ISymbol symbol = new SimpleSymbol(parts[0]);
                        final long timestamp = Long.parseLong(parts[1]);
                        symbolsList.add(symbol, timestamp);
                    }
                }
            } else {
                symbolsList = new SymbolsList(0);
            }
        }

        return symbolsList.getSymbols();
    }

    public void addSymbol(@NonNull final ISymbol symbol) {
        symbolsList.add(symbol);
    }

    public void persist() {
        if (symbolsList.size() > 0) {
            final StringBuilder stringBuilder = new StringBuilder(symbolsList.size() * SYMBOL_GUESS_SIZE);

            for (int i = 0; i < symbolsList.size(); i++) {
                final Data data = symbolsList.get(i);
                stringBuilder
                        .append(data.symbol.getUnicode())
                        .append(TIME_DELIMITER)
                        .append(data.timestamp)
                        .append(SYMBOL_DELIMITER);
            }

            stringBuilder.setLength(stringBuilder.length() - SYMBOL_DELIMITER.length());

            getPreferences().edit().putString(RECENT_SYMBOLS, stringBuilder.toString()).apply();
        }
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static class SymbolsList {
        static final Comparator<Data> COMPARATOR = (lhs, rhs) ->
                Long.compare(rhs.timestamp, lhs.timestamp);

        @NonNull final List<Data> symbols;

        SymbolsList(final int size) {
            symbols = new ArrayList<>(size);
        }

        void add(final ISymbol symbol) {
            add(symbol, System.currentTimeMillis());
        }

        void add(final ISymbol symbol, final long timestamp) {
            final Iterator<Data> iterator = symbols.iterator();

            while (iterator.hasNext()) {
                final Data data = iterator.next();
                if (data.symbol.equals(symbol)) {
                    iterator.remove();
                }
            }

            symbols.add(0, new Data(symbol, timestamp));

            if (symbols.size() > MAX_RECENTS) {
                symbols.remove(MAX_RECENTS);
            }
        }

        ArrayList<ISymbol> getSymbols() {
            Collections.sort(symbols, COMPARATOR);

            final ArrayList<ISymbol> sorted = new ArrayList<>(symbols.size());

            for (final Data data : symbols) {
                sorted.add(data.symbol);
            }

            return sorted;
        }

        int size() {
            return symbols.size();
        }

        Data get(final int index) {
            return symbols.get(index);
        }
    }

    static class Data {
        final ISymbol symbol;
        final long timestamp;

        Data(final ISymbol symbol, final long timestamp) {
            this.symbol = symbol;
            this.timestamp = timestamp;
        }
    }
}
