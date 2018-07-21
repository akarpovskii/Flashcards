package com.gmail.ooad.flipablecardview;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.even.mricheditor.RichEditor;

/*
 * Created by akarpovskii on 06.07.18.
 */
public class CardEditFragment extends CardFragment {
    private RichEditor mBack;

    private RichEditor mFront;

    private FloatingActionButton mFlipBtn;

    public static CardEditFragment NewInstance(int color, @Nullable ICardData card) {
        CardEditFragment fragment = new CardEditFragment();
        fragment.Init(color, card);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Some hacks
        FrameLayout view = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);
        inflater.inflate(R.layout.card_edit_flip_btn, view, true);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFront = view.findViewById(R.id.edit_front);
        mBack = view.findViewById(R.id.edit_back);
        mFront.requestFocus();

        if (mCard != null) {
            mBack.insertHtml(mCard.getBack());
            mFront.insertHtml(mCard.getFront());
        }

        mFront.setBackgroundColor(Color.TRANSPARENT);
        mBack.setBackgroundColor(Color.TRANSPARENT);

        mFlipBtn = view.findViewById(R.id.flip_btn);
        mFlipBtn.setOnClickListener(v -> flipCard());
    }

    public void setCard(@NonNull ICardData card) {
        mCard = card;
        mFront.empty();
        mFront.insertHtml(mCard.getFront());
        mBack.empty();
        mBack.insertHtml(mCard.getBack());
    }

    public RichEditor getEditorFront() {
        return mFront;
    }

    public RichEditor getEditorBack() {
        return mBack;
    }

    @Override
    protected View inflateCardFront(@NonNull LayoutInflater inflater, ViewGroup parent,
                                    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_edit_front, parent, false);
    }

    @Override
    protected View inflateCardBack(@NonNull LayoutInflater inflater, ViewGroup parent,
                                   Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_edit_back, parent, false);
    }

    @Override
    protected void onAnimationStart(Animator animation) {
        mFlipBtn.setEnabled(false);
    }

    @Override
    protected void onAnimationEnd(Animator animation) {
        mFlipBtn.setEnabled(true);
        if (mIsFrontVisible) {
            mFront.requestFocus();
        } else {
            mBack.requestFocus();
        }

        mFlipBtn.bringToFront();
    }

    @Override
    protected void onAnimationCancel(Animator animation) {
        mFlipBtn.setEnabled(true);
    }

    @Override
    protected void onAnimationRepeat(Animator animation) {

    }
}
