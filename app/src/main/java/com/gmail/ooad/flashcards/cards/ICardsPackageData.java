package com.gmail.ooad.flashcards.cards;

import android.os.Parcelable;

import com.gmail.ooad.flipablecardview.ICardData;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 30.04.18.
 */
public interface ICardsPackageData extends Parcelable {
    String getName();

    void setName(String name);

    int getColor();

    void setColor(int color);

    ArrayList<ICardData> getCards();

    void setCards(ArrayList<ICardData> cards);
}
