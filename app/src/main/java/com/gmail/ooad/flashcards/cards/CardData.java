package com.gmail.ooad.flashcards.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gmail.ooad.flipablecardview.ICardData;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class CardData implements ICardData {
    private String mName;

    private String mFront;

    private String mBack;

    public CardData(@NonNull String name, @Nullable String front, @Nullable String back) {
        mName = name;
        mFront = front;
        mBack = back;
    }

    public CardData(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        mName = data[0];
        mFront = data[1];
        mBack = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {mName, mFront, mBack});
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getFront() {
        return mFront;
    }

    @Override
    public String getBack() {
        return mBack;
    }

    public static final Parcelable.Creator<CardData> CREATOR =
            new Parcelable.Creator<CardData>() {
        @Override
        public CardData createFromParcel(Parcel source) {
            return new CardData(source);
        }

        @Override
        public CardData[] newArray(int size) {
            return new CardData[size];
        }
    };
}
