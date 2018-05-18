package com.gmail.ooad.flashcards.symbols;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.gmail.ooad.symbolskeyboard.model.SimpleSymbol;

/*
 * Created by akarpovskii on 18.05.18.
 */
public class Symbol extends SimpleSymbol implements Parcelable {
    public Symbol(int codePoint) {
        super(codePoint);
    }

    public Symbol(@NonNull int[] codePoints) {
        super(codePoints);
    }

    public Symbol(@NonNull String unicode) {
        super(unicode);
    }

    public Symbol(Parcel in) {
        super(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUnicode());
    }

    public static final Parcelable.Creator<Symbol> CREATOR =
            new Parcelable.Creator<Symbol>() {
                @Override
                public Symbol createFromParcel(Parcel source) {
                    return new Symbol(source);
                }

                @Override
                public Symbol[] newArray(int size) {
                    return new Symbol[0];
                }
            };

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Symbol && mUnicode.equals(((Symbol) obj).mUnicode);
    }
}
