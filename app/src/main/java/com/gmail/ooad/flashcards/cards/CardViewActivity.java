package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flipablecardview.CardViewFragment;
import com.gmail.ooad.flipablecardview.ICardData;

public class CardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ICardData card = intent.getParcelableExtra("card");
        int color = intent.getIntExtra("color", ContextCompat.getColor(this, R.color.colorDefault));

        getWindow().setStatusBarColor(color);
        toolbar.setBackgroundColor(color);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(card.getName());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, CardViewFragment.NewInstance(color, card))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
