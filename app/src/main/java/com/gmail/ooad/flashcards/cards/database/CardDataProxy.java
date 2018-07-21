package com.gmail.ooad.flashcards.cards.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.gmail.ooad.flashcards.cards.CardData;
import com.gmail.ooad.flashcards.cards.CardsController;
import com.gmail.ooad.flipablecardview.ICardData;

public class CardDataProxy implements ICardData {
    private boolean mInited = false;

    private String mPackageName;

    private CardData mCardData;

    CardDataProxy(@NonNull String packageName, @NonNull String cardName) {
        mCardData = new CardData(cardName, null, null);
        mPackageName = packageName;
    }

    private CardDataProxy(Parcel in) {
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
        mCardData = CardsController.GetInstance().getCard(mPackageName, mCardData.getName());
        mInited = true;
    }

    @Override
    public String getName() {
        return mCardData.getName();
    }

    @Override
    public String getFront() {
        if (!mInited) {
            init();
        }
        return mCardData.getFront();
    }

    @Override
    public String getBack() {
        if (!mInited) {
            init();
        }
        return mCardData.getBack();
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
