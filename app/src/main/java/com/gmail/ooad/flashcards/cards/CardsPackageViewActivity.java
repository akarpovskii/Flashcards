package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.EditableListViewActivity;
import com.gmail.ooad.flipablecardview.ICardData;

import java.util.ArrayList;


public class CardsPackageViewActivity extends EditableListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

    protected ICardsPackageData mPackageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mPackageData = intent.getParcelableExtra("package");

        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(mPackageData.getPalette().getPrimary());
        getWindow().setStatusBarColor(mPackageData.getPalette().getPrimaryDark());
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

        ArrayList<CardDataProxy> proxies = CardsController.GetInstance().getPackageCards(mPackageData.getName());

        ArrayList<ICardData> cards = new ArrayList<>();
        //noinspection CollectionAddAllCanBeReplacedWithConstructor
        cards.addAll(proxies);
        mPackageData.setCards(cards);
        for (ICardData card:
                cards) {
            mRecordAdapter.add(card.getName());
        }
    }

    @Override
    protected void onAddRecord() {
        Intent intent = new Intent(this, AddCardActivity.class);
        intent.putExtra("package", mPackageData.getName());
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onEditRecord() {
        String cardName = mRecordAdapter.getItem(mSelected.get(0));
        assert cardName != null;
        ICardData data = mPackageData.getCards().get(mSelected.get(0));

        Intent intent = new Intent(this, EditCardActivity.class);
        intent.putExtra("package", mPackageData.getName());
        intent.putExtra("card", data);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onDeleteRecord() {
        String title = getResources().getString(R.string.menu_action_delete_message_pre);
        title += " " + String.valueOf(mSelected.size()) + " ";
        if (mSelected.size() == 1) {
            title += getResources().getString(R.string.menu_action_delete_cards_message_post_singular);
        } else {
            title += getResources().getString(R.string.menu_action_delete_cards_message_post_plural);
        }

        // Make a copy, because mSelected will be cleared after method return
        final ArrayList<Integer> selected = new ArrayList<>(mSelected);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.menu_action_delete_packages_title))
                .setMessage(title)
                .setPositiveButton(getResources().getString(R.string.menu_action_delete_button_yes),
                        (dialog, which) -> {
                            for (int pos :
                                    selected) {
                                String cardName = mRecordAdapter.getItem(pos);
                                assert cardName != null;
                                CardsController.GetInstance().removeCard(mPackageData.getName(), cardName);
                            }
                            syncListWitchDatabase();
                        })
                .setNegativeButton(getResources()
                        .getString(R.string.menu_action_delete_button_no), null)
                .show();
    }

    @Override
    protected void onViewRecord(int position) {
        ICardData data = mPackageData.getCards().get(position);

        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra("card", data);
        intent.putExtra("color", mPackageData.getPalette().getPrimary());
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected int getColorAccent() {
        return mPackageData.getPalette().getAccent();
    }

    @Override
    protected CharSequence getCustomTitle() {
        return mPackageData.getName();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }
}
