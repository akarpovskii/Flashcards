package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.ColorUtil;

import java.util.ArrayList;


public class PackageViewActivity extends ListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

    protected String mName;

    protected int mColor;

    protected ArrayList<ICardData> mCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mName = intent.getStringExtra("package");
        assert mName != null;

        mColor = intent.getIntExtra("color", getResources().getColor(R.color.colorPrimary));

        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(mColor);
        getWindow().setStatusBarColor(ColorUtil.darken(mColor, 12));

        setTitle(mName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SyncList:
                if (resultCode == RESULT_OK) {
                    syncListWitchDatabase();
                }
        }
    }

    @Override
    protected void syncListWitchDatabase() {
        mRecordAdapter.clear();
        mCards.clear();

        ArrayList<CardDataProxy> cards = CardController.GetPackageCards(mName);
        mCards.addAll(cards);
        for (ICardData card:
                mCards) {
            mRecordAdapter.add(card.getName());
        }
    }

    @Override
    protected void onAddRecord() {
        Intent intent = new Intent(this, AddCardActivity.class);
        intent.putExtra("package", mName);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onEditRecord() {
        String cardName = mRecordAdapter.getItem(mSelected.get(0));
        assert cardName != null;
        ICardData data = mCards.get(mSelected.get(0));

        Intent intent = new Intent(this, EditCardActivity.class);
        intent.putExtra("package", mName);
        intent.putExtra("name", data.getName());
        intent.putExtra("front", data.getFront());
        intent.putExtra("back", data.getBack());
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onDeleteRecord() {
        for (int pos :
                mSelected) {
            String cardName = mRecordAdapter.getItem(pos);
            assert cardName != null;
            CardController.RemoveCard(mName, cardName);
        }

        syncListWitchDatabase();
    }

    @Override
    protected void onViewRecord(int position) {
        ICardData data = mCards.get(position);

        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra("name", data.getName());
        intent.putExtra("front", data.getFront());
        intent.putExtra("back", data.getBack());
        intent.putExtra("color", mColor);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected int getColorAccent() {
        return ColorUtil.darken(mColor, 20);
    }
}
