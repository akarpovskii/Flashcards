package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;

import java.util.ArrayList;

class PackageViewActivity extends ListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mName = intent.getStringExtra("package");
        assert mName != null;

        super.onCreate(savedInstanceState);
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
        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDeleteRecord() {
        Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onViewRecord(int position) {
        Toast.makeText(getApplicationContext(), "view", Toast.LENGTH_SHORT).show();
    }
}
