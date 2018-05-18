package com.gmail.ooad.flashcards.symbols;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 09.05.18.
 */
public class SymbolsPackageData implements ISymbolsPackageData {
    private String mName;

    private ArrayList<Symbol> mSymbols;

    public SymbolsPackageData(@NonNull String name, @Nullable ArrayList<Symbol> symbols) {
        mName = name;
        mSymbols = symbols;
    }

    public SymbolsPackageData(Parcel in) {
        mName = in.readString();
        int size = in.readInt();
        if (size > 0) {
            mSymbols = new ArrayList<>(size);
            in.readList(mSymbols, Symbol.class.getClassLoader());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        if (mSymbols == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(mSymbols.size());
            dest.writeList(mSymbols);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public ArrayList<Symbol> getSymbols() {
        return mSymbols == null ? new ArrayList<>(0) : new ArrayList<>(mSymbols);
    }

    @Override
    public void setSymbols(ArrayList<Symbol> symbols) {
        mSymbols = symbols;
    }

    public static final Parcelable.Creator<SymbolsPackageData> CREATOR =
            new Parcelable.Creator<SymbolsPackageData>() {
        @Override
        public SymbolsPackageData createFromParcel(Parcel source) {
            return new SymbolsPackageData(source);
        }

        @Override
        public SymbolsPackageData[] newArray(int size) {
            return new SymbolsPackageData[0];
        }
    };
}
