package com.gmail.ooad.symbolskeyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolLongClickListener;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

/*
 * Created by akarpovskii on 13.05.18.
 */
public class SymbolTextView extends android.support.v7.widget.AppCompatTextView {

    ISymbol mSymbol = null;

    OnSymbolClickListener mClickListener;
    OnSymbolLongClickListener mLongClickListener;

    public SymbolTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    void setSymbol(@NonNull final ISymbol symbol) {
        mSymbol = symbol;
        setText(mSymbol.getUnicode());
    }

    void setOnSymbolClickListener(@Nullable final OnSymbolClickListener listener) {
        if (listener != null) {
            setOnClickListener(ignore -> mClickListener.onSymbolClick(SymbolTextView.this, mSymbol));
            mClickListener = listener;
        }
    }

    void setOnSymbolLongClickListener(@Nullable final OnSymbolLongClickListener listener) {
        if (listener != null) {
            setOnLongClickListener(ignore -> {
                mLongClickListener.onSymbolsLongClick(SymbolTextView.this, mSymbol);
                return true;
            });
            mLongClickListener = listener;
        }
    }
}
