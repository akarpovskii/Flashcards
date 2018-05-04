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
    static private SlideShowStatistics mStatistics;

    public static void StartSlideShow(@NonNull FragmentActivity context, @NonNull final ArrayList<IPackageData> packages) {
        ArrayList<Pair<Integer, ICardData>> pairs = new ArrayList<>();
        for (IPackageData pack :
                packages) {
            for (ICardData card :
                    pack.getCards()) {
                pairs.add(new Pair<>(pack.getColor(), card));
            }
        }

        Collections.shuffle(pairs);

        mStatistics = new SlideShowStatistics();
        Intent intent = new Intent(context, SlideShowActivity.class);
        ICardData[] cards = new ICardData[pairs.size()];
        int[] colors = new int[pairs.size()];
        for (int i = 0; i < cards.length; ++i) {
            cards[i] = pairs.get(i).second;
            colors[i] = pairs.get(i).first;
        }
        intent.putExtra("cards", cards);
        intent.putExtra("colors", colors);
        context.startActivity(intent);
    }

    static void MarkAsMemorized() {
        mStatistics.incMemorized();
    }

    static void MarkAsUnmemorized() {
        mStatistics.incUnmemorized();
    }

    static @NonNull SlideShowStatistics GetStatistics() {
        return mStatistics;
    }
}
