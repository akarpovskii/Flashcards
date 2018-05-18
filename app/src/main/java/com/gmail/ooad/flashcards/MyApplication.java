package com.gmail.ooad.flashcards;

import android.app.Application;
import android.support.annotation.NonNull;

/*
 * Created by akarpovskii on 26.04.18.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    public MyApplication() {
        mInstance = this;
    }

    @NonNull
    public static MyApplication GetInstance() {
        return mInstance;
    }
}
