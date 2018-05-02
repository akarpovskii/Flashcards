package com.gmail.ooad.flashcards.cards;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class PackageData implements IPackageData {
    private String mName;

    private int mColor;

    private ArrayList<ICardData> mCards;

    PackageData(@NonNull String name, int color, @Nullable ArrayList<ICardData> cards) {
        mName = name;
        mColor = color;
        mCards = cards;
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
}
