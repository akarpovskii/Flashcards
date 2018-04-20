package com.gmail.ooad.flashcards;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class PackageData {
    private String mName;

    private Color mColor;

    private ArrayList<CardData> mCards;

    PackageData(@NonNull String name, @NonNull Color color, @Nullable ArrayList<CardData> cards) {
        mName = name;
        mColor = color;
        mCards = cards;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        mColor = color;
    }

    public ArrayList<CardData> getCards() {
        return mCards;
    }

    public void setCards(ArrayList<CardData> cards) {
        mCards = cards;
    }
}
