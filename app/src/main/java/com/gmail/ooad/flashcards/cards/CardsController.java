package com.gmail.ooad.flashcards.cards;
/*
 * Created by akarpovskii on 26.04.18.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gmail.ooad.flashcards.MyApplication;
import com.gmail.ooad.flashcards.cards.database.CardsDatabaseAdapter;
import com.gmail.ooad.flashcards.cards.database.CardsPackageDataProxy;

import java.util.ArrayList;

public class CardsController {

    private static CardsController mInstance;

    private CardsDatabaseAdapter mCardDatabase;

    private CardsController() {
        mCardDatabase = new CardsDatabaseAdapter(MyApplication.GetInstance().getApplicationContext());
    }

    public static synchronized @NonNull CardsController GetInstance() {
        if (mInstance == null) {
            mInstance = new CardsController();
        }
        return mInstance;
    }

    public @NonNull ArrayList<CardsPackageDataProxy> getPackageList() {
        return mCardDatabase.getPackageList();
    }

    public CardsPackageData getPackage(@NonNull String name) {
        return mCardDatabase.getPackage(name);
    }

    public CardData getCard(@NonNull String packageName, @NonNull String cardName) {
        return mCardDatabase.getCard(packageName, cardName);
    }

    public boolean addPackage(@NonNull CardsPackageData packageData) {
        return mCardDatabase.addPackage(packageData);
    }

    public boolean addCard(@NonNull String packageName, @NonNull CardData cardData) {
        return  mCardDatabase.addCard(packageName, cardData);
    }

    public boolean updatePackage(@NonNull CardsPackageData packageData, @Nullable String oldPackageName) {
        return mCardDatabase.updatePackage(packageData, oldPackageName);
    }

    public boolean updateCard(@NonNull String packageName, @NonNull CardData cardData,
                              @Nullable String oldCardName) {
        return mCardDatabase.updateCard(packageName, cardData, oldCardName);
    }

    public boolean hasPackage(@NonNull String name) {
        return mCardDatabase.hasPackage(name);
    }

    public boolean hasCard(@NonNull String packageName, @NonNull String cardName) {
        return mCardDatabase.hasCard(packageName, cardName);
    }

    public void removePackage(@NonNull String packageName) {
        mCardDatabase.removePackage(packageName);
    }

    public void removeCard(@NonNull String packageName, @NonNull String cardName) {
        mCardDatabase.removeCard(packageName, cardName);
    }
}
