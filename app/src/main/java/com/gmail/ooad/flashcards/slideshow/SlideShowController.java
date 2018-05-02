package com.gmail.ooad.flashcards.slideshow;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;

import com.gmail.ooad.flashcards.cards.ICardData;
import com.gmail.ooad.flashcards.cards.IPackageData;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Created by akarpovskii on 30.04.18.
 */
public class SlideShowController {
    static private ArrayList<Pair<ICardData, Integer>> mCards;

    static private SlideShowStatistics mStatistics;

    static private int mCurrent;

    public static void StartSlideShow(@NonNull FragmentActivity context, @NonNull final ArrayList<IPackageData> packages) {
        mCards = new ArrayList<>();
        for (IPackageData pack :
                packages) {
            for (ICardData card :
                    pack.getCards()) {
                mCards.add(new Pair<>(card, pack.getColor()));
            }
        }

        Collections.shuffle(mCards);

        mStatistics = new SlideShowStatistics();
        mCurrent = -1;
        Intent intent = new Intent(context, SlideShowActivity.class);
        context.startActivity(intent);
    }

    static void MarkAsMemorized() {
        mStatistics.incMemorized();
    }

    static void MarkAsUnmemorized() {
        mStatistics.incUnmemorized();
    }

    /**
     *
     * @return <cardData, color>
     */
    static @Nullable
    Pair<ICardData, Integer> GetNextCard() {
        ++mCurrent;
        return HasNextCard() ? mCards.get(mCurrent) : null;
    }

    static @NonNull SlideShowStatistics GetStatistics() {
        return mStatistics;
    }

    public static int GetCount() {
        return mCards.size();
    }

    private static boolean HasNextCard() {
        return mCurrent < mCards.size();
    }
}
