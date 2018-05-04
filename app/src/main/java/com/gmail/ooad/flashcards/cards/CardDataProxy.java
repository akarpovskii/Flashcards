package com.gmail.ooad.flashcards.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class CardDataProxy implements ICardData {
    private boolean mInited = false;

    private String mPackageName;

    private CardData mCardData;

    CardDataProxy(@NonNull String packageName, @NonNull String cardName) {
        mCardData = new CardData(cardName, null, null);
        mPackageName = packageName;
    }

    CardDataProxy(Parcel in) {
        mCardData = new CardData(in);
        mPackageName = in.readString();
        mInited = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mCardData.writeToParcel(dest, flags);
        dest.writeString(mPackageName);
        dest.writeInt(mInited ? 1 : 0);
    }

    private void init() {
        CardData data = CardController.GetCard(mPackageName, mCardData.getName());
        mCardData.setFront(data.getFront());
        mCardData.setBack(data.getBack());
        mInited = true;
    }

    @Override
    public String getName() {
        return mCardData.getName();
    }

    @Override
    public void setName(String name) {
        if (!mInited) {
            init();
        }
        mCardData.setName(name);
    }

    @Override
    public String getFront() {
        if (!mInited) {
            init();
        }
        return mCardData.getFront();
    }

    @Override
    public void setFront(String front) {
        if (!mInited) {
            init();
        }
        mCardData.setFront(front);
    }

    @Override
    public String getBack() {
        if (!mInited) {
            init();
        }
        return mCardData.getBack();
    }

    @Override
    public void setBack(String back) {
        if (!mInited) {
            init();
        }
        mCardData.setBack(back);
    }

    public static final Parcelable.Creator<CardDataProxy> CREATOR =
            new Parcelable.Creator<CardDataProxy>() {
        @Override
        public CardDataProxy createFromParcel(Parcel source) {
            return new CardDataProxy(source);
        }

        @Override
        public CardDataProxy[] newArray(int size) {
            return new CardDataProxy[size];
        }
    };
}
