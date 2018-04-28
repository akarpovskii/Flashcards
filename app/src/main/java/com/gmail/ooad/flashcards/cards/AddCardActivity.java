package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;

public class AddCardActivity extends AppCompatActivity {
    protected String mPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(R.string.activity_edit_card_title);
            bar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mPackage = intent.getStringExtra("package");
        assert mPackage != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if (onSaveCard()) {
                    finish();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected boolean onSaveCard() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.card_name)).getText();
        CharSequence front = ((TextInputEditText)findViewById(R.id.card_front)).getText();
        CharSequence back = ((TextInputEditText)findViewById(R.id.card_back)).getText();
        CardData data = new CardData(name.toString(), front.toString(), back.toString());

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Please, enter the name",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (CardController.AddCard(mPackage, data)) {
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
