package com.gmail.ooad.flashcards.slideshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.gmail.ooad.flashcards.cards.CardViewFragment;
import com.gmail.ooad.flashcards.cards.ICardData;

/*
 * Created by akarpovskii on 03.05.18.
 */
public class CardDataAdapter extends ArrayAdapter<Pair<Integer, ICardData>> {
    CardDataAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Pair<Integer, ICardData> item = getItem(position);
        if (item == null) {
            throw new NullPointerException("Required position is greater than expected");
        }

        if (convertView == null) {
            FrameLayout layout = new FrameLayout(parent.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(params);
            int id = View.generateViewId();
            layout.setId(id);

            ((AppCompatActivity)parent.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .add(id, CardViewFragment
                            .NewInstance(item.first, item.second))
                    .commit();
            return layout;
        } else {
            ((AppCompatActivity)parent.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(convertView.getId(), CardViewFragment
                            .NewInstance(item.first, item.second))
                    .commit();
            return convertView;

        }
    }
}
