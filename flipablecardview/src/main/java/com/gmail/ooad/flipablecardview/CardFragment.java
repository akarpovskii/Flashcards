package com.gmail.ooad.flipablecardview;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CardFragment extends Fragment {
    private AnimatorSet mSetRightOut;

    private AnimatorSet mSetLeftIn;

    private View mCardFrame;

    private ViewGroup mCardFrontLayout;

    private ViewGroup mCardBackLayout;

    protected CardView mCardFront;

    protected CardView mCardBack;

    protected int mColor;

    protected ICardData mCard;

    protected boolean mIsFrontVisible = true;

    final protected void Init(int color, ICardData card) {
        Bundle args = new Bundle();
        args.putInt("color", color);
        if (card != null) {
            args.putParcelable("card", card);
        }
        setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_card_view, container, false);

        mCardFront = view.findViewById(R.id.card_front_cardView);
        mCardBack = view.findViewById(R.id.card_back_cardView);

        mCardFront.addView(inflateCardFront(inflater, mCardFront, savedInstanceState));
        mCardBack.addView(inflateCardBack(inflater, mCardBack, savedInstanceState));
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardFrame = view.findViewById(R.id.card_view_frame);

        mCardFrontLayout = view.findViewById(R.id.card_front);
        mCardBackLayout = view.findViewById(R.id.card_back);

        mCardFront.setCardBackgroundColor(mColor);
        mCardBack.setCardBackgroundColor(mColor);

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_in);

        mSetRightOut.addListener(new MyAnimatorListener());
//        mSetLeftIn.addListener(new MyAnimatorListener());

        changeCameraDistance();
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    protected abstract View inflateCardFront(@NonNull LayoutInflater inflater, ViewGroup parent,
                                             Bundle savedInstanceState);

    protected abstract View inflateCardBack(@NonNull LayoutInflater inflater, ViewGroup parent,
                                            Bundle savedInstanceState);

    protected abstract void onAnimationStart(Animator animation);

    protected abstract void onAnimationEnd(Animator animation);

    protected abstract void onAnimationCancel(Animator animation);

    protected abstract void onAnimationRepeat(Animator animation);



    protected void flipCard() {
        if (mIsFrontVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mIsFrontVisible = false;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mIsFrontVisible = true;
        }
        mSetRightOut.start();
        mSetLeftIn.start();
    }

    private class MyAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            mCardFrame.setEnabled(false);
            CardFragment.this.onAnimationStart(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mCardFrame.setEnabled(true);
            if (mIsFrontVisible) {
                mCardFront.setEnabled(true);
                mCardFrontLayout.bringToFront();
                mCardBack.setEnabled(false);
            } else {
                mCardBack.setEnabled(true);
                mCardBackLayout.bringToFront();
                mCardFront.setEnabled(false);
            }
            CardFragment.this.onAnimationEnd(animation);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            CardFragment.this.onAnimationCancel(animation);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            CardFragment.this.onAnimationRepeat(animation);
        }
    }
}
