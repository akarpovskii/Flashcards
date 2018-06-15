package com.gmail.ooad.flashcards.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.gmail.ooad.flipablecardview.ICardData;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 30.04.18.
 */
public class CardsPackageDataProxy implements ICardsPackageData {
    private boolean mInited = false;

    private CardsPackageData mPackageData;

    public CardsPackageDataProxy(@NonNull String name, PackagePalette palette) {
        mPackageData = new CardsPackageData(name, palette, null);
    }

    public CardsPackageDataProxy(Parcel in) {
        mPackageData = new CardsPackageData(in);
        mInited = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mPackageData.writeToParcel(dest, flags);
        dest.writeInt(mInited ? 1 : 0);
    }

    @Override
    public String getName() {
        return mPackageData.getName();
    }

    @Override
    public void setName(String name) {
        mPackageData.setName(name);
    }

    @Override
    public PackagePalette getPalette() {
        return mPackageData.getPalette();
    }

    @Override
    public void setPalette(PackagePalette palette) {
        mPackageData.setPalette(palette);
    }

    @Override
    public ArrayList<ICardData> getCards() {
        if (!mInited) {
            ArrayList<ICardData> cards = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            cards.addAll(CardsController.GetInstance().getPackageCards(mPackageData.getName()));
            mPackageData.setCards(cards);
            mInited = true;
        }
        return mPackageData.getCards();
    }

    @Override
    public void setCards(ArrayList<ICardData> cards) {
        mPackageData.setCards(cards);
    }

    public static final Parcelable.Creator<CardsPackageDataProxy> CREATOR =
            new Parcelable.Creator<CardsPackageDataProxy>() {
        @Override
        public CardsPackageDataProxy createFromParcel(Parcel source) {
            return new CardsPackageDataProxy(source);
        }

        @Override
        public CardsPackageDataProxy[] newArray(int size) {
            return new CardsPackageDataProxy[size];
        }
    };
}
