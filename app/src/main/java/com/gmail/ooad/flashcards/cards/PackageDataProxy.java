package com.gmail.ooad.flashcards.cards;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 30.04.18.
 */
public class PackageDataProxy implements IPackageData {
    private boolean mInited = false;

    private PackageData mPackageData;

    PackageDataProxy(@NonNull String name, int color) {
        mPackageData = new PackageData(name, color, null);
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
    public int getColor() {
        return mPackageData.getColor();
    }

    @Override
    public void setColor(int color) {
        mPackageData.setColor(color);
    }

    @Override
    public ArrayList<ICardData> getCards() {
        if (!mInited) {
            ArrayList<ICardData> cards = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            cards.addAll(CardController.GetPackageCards(mPackageData.getName()));
            mPackageData.setCards(cards);
            mInited = true;
        }
        return mPackageData.getCards();
    }

    @Override
    public void setCards(ArrayList<ICardData> cards) {
        mPackageData.setCards(cards);
    }
}
