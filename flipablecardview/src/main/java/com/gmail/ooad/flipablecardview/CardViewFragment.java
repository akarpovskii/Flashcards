package com.gmail.ooad.flipablecardview;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.even.mricheditor.RichViewer;

/*
 * Created by akarpovskii on 16.06.18.
 */
public class CardViewFragment extends CardFragment {
    private RichViewer mBack;

    private RichViewer mFront;

    protected GestureDetector mGestureDetector;

    public static CardViewFragment NewInstance(int color, ICardData card) {
        CardViewFragment fragment = new CardViewFragment();
        fragment.Init(color, card);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGestureDetector = new GestureDetector(getContext(), new MyClickListener());

        mCardFront.setOnTouchListener((v, event) -> mGestureDetector.onTouchEvent(event));
        mCardBack.setOnTouchListener((v, event) -> mGestureDetector.onTouchEvent(event));

        mFront = view.findViewById(R.id.view_front);
        mBack = view.findViewById(R.id.view_back);

        if (mCard != null) {
            mBack.insertHtml(mCard.getBack());
            mFront.insertHtml(mCard.getFront());
        }
        
        mFront.setBackgroundColor(Color.TRANSPARENT);
        mBack.setBackgroundColor(Color.TRANSPARENT);

        mBack.setOnTouchListener((v, event) -> mGestureDetector.onTouchEvent(event));
        mFront.setOnTouchListener((v, event) -> mGestureDetector.onTouchEvent(event));
    }

    @Override
    protected View inflateCardFront(@NonNull LayoutInflater inflater, ViewGroup parent,
                                    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_view_front, parent, false);
    }

    @Override
    protected View inflateCardBack(@NonNull LayoutInflater inflater, ViewGroup parent,
                                   Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_view_back, parent, false);
    }

    @Override
    protected void onAnimationStart(Animator animation) {
        mBack.setEnabled(false);
        mFront.setEnabled(false);
        mCardBack.setEnabled(false);
        mCardFront.setEnabled(false);
    }

    @Override
    protected void onAnimationEnd(Animator animation) {
        mCardFront.setEnabled(true);
        mCardBack.setEnabled(true);
        mFront.setEnabled(true);
        mBack.setEnabled(true);
    }

    @Override
    protected void onAnimationCancel(Animator animation) {
        mCardFront.setEnabled(true);
        mCardBack.setEnabled(true);
        mFront.setEnabled(true);
        mBack.setEnabled(true);
    }

    @Override
    protected void onAnimationRepeat(Animator animation) {

    }

    class MyClickListener extends GestureDetector.SimpleOnGestureListener {
        private static final int MAX_CLICK_DURATION = 1000;

        private static final int MAX_CLICK_DISTANCE = 15;

        private long pressStartTime;

        private float pressedX;

        private float pressedY;

        @Override
        public boolean onDown(MotionEvent e) {
            pressStartTime = System.currentTimeMillis();
            pressedX = e.getX();
            pressedY = e.getY();
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            long pressDuration = System.currentTimeMillis() - pressStartTime;
            if (pressDuration < MAX_CLICK_DURATION &&
                    distance(pressedX, pressedY, e.getX(), e.getY()) < MAX_CLICK_DISTANCE) {
                flipCard();
            }
            return false;
        }

        private float distance(float x1, float y1, float x2, float y2) {
            float dx = x1 - x2;
            float dy = y1 - y2;
            float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
            return pxToDp(distanceInPx);
        }

        private  float pxToDp(float px) {
            return px / getResources().getDisplayMetrics().density;
        }


    }
}
