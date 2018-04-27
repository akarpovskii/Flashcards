package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;

/*
 * Created by akarpovskii on 27.04.18.
 */
public class EditCardActivity extends AddCardActivity {

    protected String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mName = intent.getStringExtra("name");
        assert mName != null;
        String front = intent.getStringExtra("front");
        assert front != null;
        String back = intent.getStringExtra("back");
        assert back != null;

        ((TextView)findViewById(R.id.card_name)).setText(mName);
        ((TextView)findViewById(R.id.card_front)).setText(front);
        ((TextView)findViewById(R.id.card_back)).setText(back);

        setTitle("Edit" + mName);
    }

    @Override
    protected boolean onSaveCard() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.card_name)).getText();
        CharSequence front = ((TextInputEditText)findViewById(R.id.card_front)).getText();
        CharSequence back = ((TextInputEditText)findViewById(R.id.card_back)).getText();
        CardData data = new CardData(name.toString(), front.toString(), back.toString());

        if (CardController.UpdateCard(mPackage, data, mName)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        } else {
            Toast.makeText(getApplicationContext(),
                    "A card with this name already exists. Please, try another one",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
