package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flipablecardview.CardEditFragment;
import com.gmail.ooad.flipablecardview.ICardData;

/*
 * Created by akarpovskii on 27.04.18.
 */
public class EditCardActivity extends AddCardActivity {

    protected ICardData mCard;
    private boolean mInited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mCard = intent.getParcelableExtra("card");

        ((TextInputEditText) findViewById(R.id.card_name)).setText(mCard.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mInited) {
            CardEditFragment fragment = (CardEditFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            fragment.setCard(mCard);
            mInited = true;
        }
    }

    @Override
    protected boolean onSaveCard() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.card_name)).getText();
        CharSequence front = mEditFront.getHtml();
        CharSequence back = mEditBack.getHtml();
        CardData data = new CardData(name.toString(), front.toString(), back.toString());

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_enter_the_name,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        boolean nameChanged = !mCard.getName().equals(name.toString());
        String oldName = nameChanged ? mCard.getName() : null;

        if (nameChanged && CardsController.GetInstance().hasCard(mPackage, data.getName())) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_card_already_exists,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (CardsController.GetInstance().updateCard(mPackage, data, oldName)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        }

        return false;
    }
}
