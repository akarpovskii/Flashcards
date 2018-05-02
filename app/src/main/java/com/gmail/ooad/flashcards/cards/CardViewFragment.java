package com.gmail.ooad.flashcards.cards;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.ooad.flashcards.R;

public class CardViewFragment extends Fragment {
    private int mColor;

    private String mFront;

    private String mBack;

    private AnimatorSet mSetRightOut;

    private AnimatorSet mSetLeftIn;

    private boolean mIsBackVisible = false;

    private View mCardFrontLayout;

    private View mCardBackLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CardViewFragment NewInstance(int color, String front, String back) {
        CardViewFragment fragment = new CardViewFragment();
        Bundle args = new Bundle();
        args.putInt("color", color);
        args.putString("front", front);
        args.putString("back", back);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColor = getArguments().getInt("color");
            mFront = getArguments().getString("front");
            mBack = getArguments().getString("back");
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
        // Front
        mCardFrontLayout = view.findViewById(R.id.card_front);
        ((ImageView) mCardFrontLayout.findViewById(R.id.bg_front)).setColorFilter(mColor);

        final TextView text_front = mCardFrontLayout.findViewById(R.id.text_front);
        text_front.setText(mFront);
        // TextView catches events for some reason even with clickable=false
        // So we need to duplicate the onClick handler
        text_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });


        // Back
        mCardBackLayout = view.findViewById(R.id.card_back);
        ((ImageView) mCardBackLayout.findViewById(R.id.bg_back)).setColorFilter(mColor);

        final TextView text_back = mCardBackLayout.findViewById(R.id.text_back);
        text_back.setText(mBack);
        // TextView  catches events for some reason even with clickable=false.
        // So we need to duplicate the onClick handler
        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });


        final View card_frame = view.findViewById(R.id.card_view_frame);
        card_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_in);

        mSetRightOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                card_frame.setClickable(false);
                text_back.setClickable(false);
                text_front.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                card_frame.setClickable(true);
                text_back.setClickable(true);
                text_front.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mSetLeftIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                card_frame.setClickable(false);
                text_back.setClickable(false);
                text_front.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                card_frame.setClickable(true);
                text_back.setClickable(true);
                text_front.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

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
}
