package com.gmail.ooad.flashcards.cards;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.ColorUtil;
import com.gmail.ooad.flipablecardview.ICardData;

import java.util.ArrayList;

/*
 * Created by akarpovskii on 18.05.18.
 */
class CardsExampleDataHelper {
    static CardsPackageData GetExampleData(Context context) {
        return new CardsPackageData(context.getString(R.string.example_cards_package_title),
                ColorUtil.GetNearest(ContextCompat.getColor(context, R.color.example_cards_package_color)),
                new ArrayList<ICardData>(){{
            add(new CardData(context.getString(R.string.example_card_name),
                    context.getString(R.string.example_card_front),
                    context.getString(R.string.example_card_back)));
        }});
    }
}
