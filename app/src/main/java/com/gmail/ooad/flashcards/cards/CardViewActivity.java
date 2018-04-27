package com.gmail.ooad.flashcards.cards;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.StaticLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gmail.ooad.flashcards.R;

public class CardViewActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;

    private AnimatorSet mSetLeftIn;

    private boolean mIsBackVisible = false;

    private View mCardFrontLayout;

    private View mCardBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String back = intent.getStringExtra("back");
        String front = intent.getStringExtra("front");

        setTitle(name);

        mCardBackLayout = findViewById(R.id.card_back);
        ((TextView) mCardBackLayout.findViewById(R.id.text_back)).setText(back);

        mCardFrontLayout = findViewById(R.id.card_front);
        ((TextView) mCardFrontLayout.findViewById(R.id.text_front)).setText(front);

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_in);

        findViewById(R.id.card_view_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        changeCameraDistance();
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void flipCard() {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
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
