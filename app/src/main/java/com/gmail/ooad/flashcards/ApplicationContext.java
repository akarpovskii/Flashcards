package com.gmail.ooad.flashcards;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gmail.ooad.flashcards.cards.CardController;

/*
 * Created by akarpovskii on 26.04.18.
 */

public class ApplicationContext extends Application {

    @NonNull
    private static ApplicationContext mInstance;

    public ApplicationContext() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CardController.Initialize();
    }

    @NonNull
    public static ApplicationContext GetInstance() {
        return mInstance;
    }
}
