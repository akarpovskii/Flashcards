package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.ColorUtil;


public class PackageViewActivity extends ListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

    protected String mName;

    protected int mColor;

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

        PackageData pack = CardController.GetPackage(mName);
        for (CardData card:
                pack.getCards()) {
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
        CardData data = CardController.GetCard(mName, cardName);

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
        String cardName = mRecordAdapter.getItem(position);
        assert cardName != null;
        CardData data = CardController.GetCard(mName, cardName);

        Intent intent = new Intent(this, CardViewActivity.class);
        intent.putExtra("name", data.getName());
        intent.putExtra("front", data.getFront());
        intent.putExtra("back", data.getBack());
        intent.putExtra("color", mColor);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected int getSelectionColor() {
        return ColorUtil.darken(mColor, 24);
    }
}
