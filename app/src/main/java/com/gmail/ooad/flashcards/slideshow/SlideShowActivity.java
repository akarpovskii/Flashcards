package com.gmail.ooad.flashcards.slideshow;

import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.cards.ICardData;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {
    private CardStackView cardStackView;

    private CardDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(R.string.activity_slide_show_title);

        cardStackView = findViewById(R.id.container);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {

            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                switch (direction) {
                    case Left:
                        SlideShowController.MarkAsUnmemorized();
                        break;
                    case Right:
                        SlideShowController.MarkAsMemorized();
                        break;
                }

                if (cardStackView.getTopIndex() == mAdapter.getCount()) {
                    showStatistics();
                }
            }

            @Override
            public void onCardReversed() {

            }

            @Override
            public void onCardMovedToOrigin() {

            }

            @Override
            public void onCardClicked(int index) {

            }
        });

        mAdapter = new CardDataAdapter(getApplicationContext());
        Parcelable[] parcelables = getIntent().getParcelableArrayExtra("cards");
        int[] colors = getIntent().getIntArrayExtra("colors");

        ArrayList<Pair<Integer, ICardData>> cards = new ArrayList<>();
        for (int i = 0; i < parcelables.length; ++i) {
            cards.add(new Pair<>(colors[i], (ICardData)parcelables[i]));
        }

        mAdapter.addAll(cards);

        loadCardStack();
    }

    private void showStatistics() {
        cardStackView.setVisibility(View.GONE);
        SlideShowStatistics statistics = SlideShowController.GetStatistics();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.slide_show_statistics_container, SlideShowStatisticsFragment
                        .NewInstance(statistics))
                .commit();
    }

    private void loadCardStack() {
        final ProgressBar progressBar = findViewById(R.id.slide_show_progress_bar);
        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardStackView.setAdapter(mAdapter);
                cardStackView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
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
