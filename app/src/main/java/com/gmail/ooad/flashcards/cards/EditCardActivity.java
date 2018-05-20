package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;

/*
 * Created by akarpovskii on 27.04.18.
 */
public class EditCardActivity extends AddCardActivity {

    protected ICardData mCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mCard = intent.getParcelableExtra("card");

        ((TextView)findViewById(R.id.card_name)).setText(mCard.getName());
        ((TextView)findViewById(R.id.card_front)).setText(mCard.getFront());
        ((TextView)findViewById(R.id.card_back)).setText(mCard.getBack());

        setTitle(getString(R.string.title_edit) + mCard.getName());
    }

    @Override
    protected boolean onSaveCard() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.card_name)).getText();
        CharSequence front = ((TextInputEditText)findViewById(R.id.card_front)).getText();
        CharSequence back = ((TextInputEditText)findViewById(R.id.card_back)).getText();
        CardData data = new CardData(name.toString(), front.toString(), back.toString());

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_enter_the_name,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        String oldName = mCard.getName().equals(name.toString()) ? null : mCard.getName();

        if (CardsController.GetInstance().updateCard(mPackage, data, oldName)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        }

        return false;
    }
}
