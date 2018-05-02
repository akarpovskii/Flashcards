package com.gmail.ooad.flashcards.cards;

/*
 * Created by akarpovskii on 30.04.18.
 */
public interface ICardData {
    String getName();

    void setName(String name);

    String getFront();

    void setFront(String front);

    String getBack();

    void setBack(String back);
}
