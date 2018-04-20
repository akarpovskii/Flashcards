package com.gmail.ooad.flashcards;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by akarpovskii on 14.04.18.
 */

public class CardData {
    private String mName;

    private String mFront;

    private String mBack;

    CardData(@NonNull String name, @Nullable String front, @Nullable String back) {
        mName = name;
        mFront = front;
        mBack = back;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFront() {
        return mFront;
    }

    public void setFront(String front) {
        mFront = front;
    }

    public String getBack() {
        return mBack;
    }

    public void setBack(String back) {
        mBack = back;
    }
}
