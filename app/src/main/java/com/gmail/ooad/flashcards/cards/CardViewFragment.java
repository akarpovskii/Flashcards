package com.gmail.ooad.flashcards.cards;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.ooad.flashcards.R;

public class CardViewFragment extends Fragment {
    private int mColor;

    private ICardData mCard;

    private AnimatorSet mSetRightOut;

    private AnimatorSet mSetLeftIn;

    private boolean mIsBackVisible = false;

    private View mCardFrontLayout;

    private View mCardBackLayout;

    private GestureDetector mGestureDetector;

    private View mCardFrame;

    private TextView mTextBack;

    private TextView mTextFront;

    public static CardViewFragment NewInstance(int color, ICardData card) {
        CardViewFragment fragment = new CardViewFragment();
        Bundle args = new Bundle();
        args.putInt("color", color);
        args.putParcelable("card", card);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColor = getArguments().getInt("color");
            mCard = getArguments().getParcelable("card");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_view, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGestureDetector = new GestureDetector(getContext(), new MyClickListener());
        // Front
        mCardFrontLayout = view.findViewById(R.id.card_front);
        ((CardView)mCardFrontLayout.findViewById(R.id.card_front_cardView)).setCardBackgroundColor(mColor);

        mTextFront = mCardFrontLayout.findViewById(R.id.text_front);
        mTextFront.setText(mCard.getFront());
        // TextView catches events for some reason even with clickable=false
        // So we need to duplicate the onClick handler
        mTextFront.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });


        // Back
        mCardBackLayout = view.findViewById(R.id.card_back);
        ((CardView)mCardBackLayout.findViewById(R.id.card_back_cardView)).setCardBackgroundColor(mColor);

        mTextBack = mCardBackLayout.findViewById(R.id.text_back);
        mTextBack.setText(mCard.getBack());
        // TextView  catches events for some reason even with clickable=false.
        // So we need to duplicate the onClick handler
        mTextBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        mCardBackLayout.setAlpha(0.0f);


        mCardFrame = view.findViewById(R.id.card_view_frame);
        mCardFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_in);

        mSetRightOut.addListener(new MyAnimatorListener());

        mSetLeftIn.addListener(new MyAnimatorListener());

        changeCameraDistance();
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    protected void flipCard() {
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

    private class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            mCardFrame.setEnabled(false);
            mTextBack.setEnabled(false);
            mTextFront.setEnabled(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mCardFrame.setEnabled(true);
            mTextFront.setEnabled(true);
            mTextBack.setEnabled(true);
            if (mIsBackVisible) {
                mCardBackLayout.bringToFront();
            } else {
                mCardFrontLayout.bringToFront();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mCardFrame.setEnabled(true);
            mTextFront.setEnabled(true);
            mTextBack.setEnabled(true);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
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
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            long pressDuration = System.currentTimeMillis() - pressStartTime;
            if (pressDuration < MAX_CLICK_DURATION &&
                    distance(pressedX, pressedY, e.getX(), e.getY()) < MAX_CLICK_DISTANCE) {
                flipCard();
                return true;
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
