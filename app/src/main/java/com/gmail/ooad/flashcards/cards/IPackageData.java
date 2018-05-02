package com.gmail.ooad.flashcards.cards;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 30.04.18.
 */
public interface IPackageData {
    String getName();

    void setName(String name);

    int getColor();

    void setColor(int color);

    ArrayList<ICardData> getCards();

    void setCards(ArrayList<ICardData> cards);
}
