package com.gmail.ooad.flashcards.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class CardsPackageData implements ICardsPackageData {
    private String mName;

    private int mColor;

    private ArrayList<ICardData> mCards;

    public CardsPackageData(@NonNull String name, int color, @Nullable ArrayList<ICardData> cards) {
        mName = name;
        mColor = color;
        mCards = cards;
    }

    public CardsPackageData(Parcel in) {
        mName = in.readString();
        mColor = in.readInt();
        int size = in.readInt();
        if (size > 0) {
            mCards = new ArrayList<>(size);
            in.readList(mCards, ICardData.class.getClassLoader());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mColor);
        if (mCards == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(mCards.size());
            dest.writeList(mCards);
        }
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public int getColor() {
        return mColor;
    }

    @Override
    public void setColor(int color) {
        mColor = color;
    }

    @Override
    public ArrayList<ICardData> getCards() {
        return mCards;
    }

    @Override
    public void setCards(ArrayList<ICardData> cards) {
        mCards = cards;
    }

    public static final Parcelable.Creator<CardsPackageData> CREATOR =
            new Parcelable.Creator<CardsPackageData>() {
        @Override
        public CardsPackageData createFromParcel(Parcel source) {
            return new CardsPackageData(source);
        }

        @Override
        public CardsPackageData[] newArray(int size) {
            return new CardsPackageData[size];
        }
    };
}
