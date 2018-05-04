package com.gmail.ooad.flashcards.cards;

import android.os.Parcelable;

/*
 * Created by akarpovskii on 30.04.18.
 */
public interface ICardData extends Parcelable {
    String getName();

    void setName(String name);

    String getFront();

    void setFront(String front);

    String getBack();

    void setBack(String back);


}
