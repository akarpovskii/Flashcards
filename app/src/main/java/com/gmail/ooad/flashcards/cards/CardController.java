package com.gmail.ooad.flashcards.cards;
/*
 * Created by akarpovskii on 26.04.18.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gmail.ooad.flashcards.ApplicationContext;

import java.util.ArrayList;

public class CardController {

    private static CardDatabaseAdapter mCardDatabase;

    public static void Initialize() {
        mCardDatabase = new CardDatabaseAdapter(ApplicationContext.GetInstance().getApplicationContext());
    }

    public static @NonNull ArrayList<PackageData> GetPackageList() {
        return mCardDatabase.getPackageList();
    }

    public static PackageData GetPackage(@NonNull String name) {
        return mCardDatabase.getPackage(name);
    }

    public static CardData GetCard(@NonNull String packageName, @NonNull String cardName) {
        return mCardDatabase.getCard(packageName, cardName);
    }

    public static boolean AddPackage(@NonNull PackageData packageData) {
        return mCardDatabase.addPackage(packageData);
    }

    public static boolean AddCard(@NonNull String packageName, @NonNull CardData cardData) {
        return  mCardDatabase.addCard(packageName, cardData);
    }

    public static boolean UpdatePackage(@NonNull PackageData packageData, @Nullable String oldPackageName) {
        return mCardDatabase.updatePackage(packageData, oldPackageName);
    }

    public static boolean UpdateCard(@NonNull String packageName, @NonNull CardData cardData,
                                     @Nullable String oldCardName) {
        return mCardDatabase.updateCard(packageName, cardData, oldCardName);
    }

    public static boolean HasPackage(@NonNull String name) {
        return mCardDatabase.hasPackage(name);
    }

    public static boolean HasCard(@NonNull String packageName, @NonNull String cardName) {
        return mCardDatabase.hasCard(packageName, cardName);
    }
}
