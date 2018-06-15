package com.gmail.ooad.flashcards.cards;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.ooad.flashcards.utils.ColorPalette;
import com.gmail.ooad.flashcards.utils.IColorPalette;

/*
 * Created by akarpovskii on 15.06.18.
 */
public class PackagePalette implements IColorPalette, Parcelable{
    private ColorPalette mColorPalette;

    private int mCardsColor;

    public PackagePalette(ColorPalette colorPalette, int cardsColor) {
        mColorPalette = colorPalette;
        mCardsColor = cardsColor;
    }

    public PackagePalette(Parcel in) {
        mColorPalette = ColorPalette.fromValue(in.readString());
        mCardsColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mColorPalette.toValue());
        dest.writeInt(mCardsColor);
    }

    public String toValue() {
        return mColorPalette.toValue();
    }

    @Override
    public int getPrimary() {
        return mColorPalette.getPrimary();
    }

    @Override
    public int getPrimaryDark() {
        return mColorPalette.getPrimaryDark();
    }

    @Override
    public int getAccent() {
        return mColorPalette.getAccent();
    }

    public int getCardsColor() {
        return mCardsColor;
    }

    public static final Parcelable.Creator<PackagePalette> CREATOR =
            new Parcelable.Creator<PackagePalette>() {
                @Override
                public PackagePalette createFromParcel(Parcel source) {
                    return new PackagePalette(source);
                }

                @Override
                public PackagePalette[] newArray(int size) { return new PackagePalette[size]; }
            };
}
