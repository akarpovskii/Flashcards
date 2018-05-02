package com.gmail.ooad.flashcards.slideshow;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.cards.CardViewFragment;
import com.gmail.ooad.flashcards.cards.ICardData;

public class SlideShowActivity extends AppCompatActivity {

    private GestureDetectorCompat mGestureDetector;

    boolean mStatisticsShowed = false;

    enum SwipeDirection {
        Left,
        Right
    }

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

        if (savedInstanceState == null) {
            Pair<ICardData, Integer> card = SlideShowController.GetNextCard();
            if (card != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, CardViewFragment
                                .NewInstance(card.second, card.first.getFront(), card.first.getBack()))
                        .commit();
            }
        }

        mGestureDetector = new GestureDetectorCompat(this, new MyGestureListener() {
            @Override
            public boolean onRightToLeft() {
                if (mStatisticsShowed) {
                    return false;
                }
                SlideShowController.MarkAsMemorized();
                showNext(SwipeDirection.Left);
                return true;
            }

            @Override
            public boolean onLeftToRight() {
                if (mStatisticsShowed) {
                    return false;
                }
                SlideShowController.MarkAsUnmemorized();
                showNext(SwipeDirection.Right);
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
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

    private void showNext(SwipeDirection direction) {
        int in = direction == SwipeDirection.Left ?
                R.animator.slide_show_swipe_left_in :
                R.animator.slide_show_swipe_right_in;
        int out = direction == SwipeDirection.Left ?
                R.animator.slide_show_swipe_left_out :
                R.animator.slide_show_swipe_right_out;

        Pair<ICardData, Integer> card = SlideShowController.GetNextCard();
        if (card != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(in,out)
                    .replace(R.id.container, CardViewFragment
                            .NewInstance(card.second, card.first.getFront(), card.first.getBack()))
                    .commit();
        } else {
            SlideShowStatistics stat = SlideShowController.GetStatistics();
            FrameLayout layout = findViewById(R.id.container);
            layout.setPadding(0, 0, 0, 0);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(in,out)
                    .replace(R.id.container, SlideShowStatisticsFragment
                            .NewInstance(stat))
                    .commit();
            mStatisticsShowed = true;
        }
    }

    public abstract class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SwipeMinDistance = 120;

        @Override
        public boolean onDown(MotionEvent event) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (event1.getX() - event2.getX() > SwipeMinDistance) {
                return onRightToLeft();
            } else if (event2.getX() - event1.getX() > SwipeMinDistance) {
                return onLeftToRight();
            }

            return false;
        }

        public abstract boolean onRightToLeft();

        public abstract boolean onLeftToRight();
    }
}
