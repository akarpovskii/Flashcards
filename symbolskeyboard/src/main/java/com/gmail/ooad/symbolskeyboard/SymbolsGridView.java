package com.gmail.ooad.symbolskeyboard;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.GridView;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolLongClickListener;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackage;

/*
 * Created by akarpovskii on 13.05.18.
 */
public class SymbolsGridView extends GridView {
    protected SymbolsArrayAdapter mAdapter;

    public SymbolsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Resources resources = getResources();
        final int width = resources.getDimensionPixelSize(R.dimen.symbols_kb_grid_view_column_width);
        final int spacing = resources.getDimensionPixelSize(R.dimen.symbols_kb_grid_view_spacing);

        setColumnWidth(width);
        setHorizontalSpacing(spacing);
        setVerticalSpacing(spacing);
        setPadding(spacing, spacing, spacing, spacing);
        setNumColumns(AUTO_FIT);
        setClipToPadding(false);
        setVerticalScrollBarEnabled(false);
    }

    public SymbolsGridView(Context context) {
        this(context, null);
    }

    public SymbolsGridView init(@Nullable final OnSymbolClickListener onSymbolsClickListener,
                                @Nullable final OnSymbolLongClickListener onSymbolsLongClickListener,
                                @NonNull final ISymbolsPackage category) {
        mAdapter = new SymbolsArrayAdapter(getContext(), category.getSymbols(),
                onSymbolsClickListener, onSymbolsLongClickListener);

        setAdapter(mAdapter);

        return this;
    }

    public void updateSymbols(@NonNull final ISymbolsPackage category) {
        mAdapter.updateSymbols(category.getSymbols());
    }
}
