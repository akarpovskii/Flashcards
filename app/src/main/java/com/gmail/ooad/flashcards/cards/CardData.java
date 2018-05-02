package com.gmail.ooad.flashcards.cards;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class CardData implements ICardData {
    private String mName;

    private String mFront;

    private String mBack;

    CardData(@NonNull String name, @Nullable String front, @Nullable String back) {
        mName = name;
        mFront = front;
        mBack = back;
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
    public String getFront() {
        return mFront;
    }

    @Override
    public void setFront(String front) {
        mFront = front;
    }

    @Override
    public String getBack() {
        return mBack;
    }

    @Override
    public void setBack(String back) {
        mBack = back;
    }
}
