package com.gmail.ooad.symbolskeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolLongClickListener;
import com.gmail.ooad.symbolskeyboard.model.IRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 16.05.18.
 */
public class RecentSymbolsGridView extends SymbolsGridView {
    private IRecentSymbolsManager mManager;

    RecentSymbolsGridView(@NonNull final Context context) {
        super(context);
    }

    public RecentSymbolsGridView init(@Nullable final OnSymbolClickListener listener,
                                    @Nullable final OnSymbolLongClickListener longListener,
                                    @NonNull final IRecentSymbolsManager manager) {
        mManager = manager;

        final ArrayList<ISymbol> symbols = mManager.getRecentSymbols();
        mAdapter = new SymbolsArrayAdapter(getContext(), symbols, listener, longListener);

        setAdapter(mAdapter);

        return this;
    }

    public void invalidateSymbols() {
        mAdapter.updateSymbols(mManager.getRecentSymbols());
    }
}
