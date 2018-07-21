package com.gmail.ooad.flipablecardview;

import android.os.Parcelable;

/*
 * Created by akarpovskii on 30.04.18.
 */
public interface ICardData extends Parcelable {
    String getName();

    String getFront();

    String getBack();
}
