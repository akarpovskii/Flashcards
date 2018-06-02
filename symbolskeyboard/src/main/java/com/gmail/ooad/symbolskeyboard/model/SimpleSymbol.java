package com.gmail.ooad.symbolskeyboard.model;

import android.support.annotation.NonNull;

/*
 * Created by akarpovskii on 18.05.18.
 */
public class SimpleSymbol implements ISymbol {
    @NonNull
    protected final String mUnicode;

    public SimpleSymbol(final int codePoint) {
        this(new int[]{codePoint});
    }

    public SimpleSymbol(@NonNull final int[] codePoints) {
        mUnicode = new String(codePoints, 0, codePoints.length);
    }

    public SimpleSymbol(@NonNull final String unicode) {
        mUnicode = unicode;
    }

    @NonNull public String getUnicode() {
        return mUnicode;
    }
}
