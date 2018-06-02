package com.gmail.ooad.symbolskeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolLongClickListener;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

import java.util.Collection;
import java.util.List;

/*
 * Created by akarpovskii on 13.05.18.
 */
public class SymbolsArrayAdapter extends ArrayAdapter<ISymbol> {
    @Nullable
    private final OnSymbolClickListener mOnSymbolClickListener;

    @Nullable
    private final OnSymbolLongClickListener mOnSymbolLongClickListener;

    SymbolsArrayAdapter(@NonNull final Context context, @NonNull final List<ISymbol> symbols,
                        @Nullable final OnSymbolClickListener listener,
                        @Nullable final OnSymbolLongClickListener longListener) {
        super(context, 0, symbols);

        this.mOnSymbolClickListener = listener;
        this.mOnSymbolLongClickListener = longListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SymbolTextView symbolView = (SymbolTextView) convertView;

        if (symbolView == null) {
            symbolView = (SymbolTextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.symbol_item_view, parent, false);

            symbolView.setOnSymbolClickListener(mOnSymbolClickListener);
            symbolView.setOnSymbolLongClickListener(mOnSymbolLongClickListener);
        }

        ISymbol symbol = getItem(position);
        if (symbol != null) {
            symbolView.setSymbol(symbol);
        }
        return symbolView;
    }

    void updateSymbols(final Collection<ISymbol> symbols) {
        clear();
        addAll(symbols);
        notifyDataSetChanged();
    }
}
