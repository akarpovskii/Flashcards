package com.gmail.ooad.flashcards.cards.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.gmail.ooad.flashcards.cards.CardsController;
import com.gmail.ooad.flashcards.cards.CardsPackageData;
import com.gmail.ooad.flashcards.cards.ICardsPackageData;
import com.gmail.ooad.flashcards.cards.PackagePalette;
import com.gmail.ooad.flipablecardview.ICardData;

import java.util.ArrayList;

public class CardsPackageDataProxy implements ICardsPackageData {
    private CardsPackageData mPackageData;

    CardsPackageDataProxy(@NonNull String name, PackagePalette palette) {
        mPackageData = new CardsPackageData(name, palette, null);
    }

    private CardsPackageDataProxy(Parcel in) {
        mPackageData = new CardsPackageData(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        mPackageData.writeToParcel(dest, flags);
    }

    @Override
    public String getName() {
        return mPackageData.getName();
    }

    @Override
    public PackagePalette getPalette() {
        return mPackageData.getPalette();
    }

    @Override
    public ArrayList<ICardData> getCards() {
        return CardsController.GetInstance().getPackage(getName()).getCards();
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
