package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

class PackageListActivity extends ListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

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

        ArrayList<PackageData> packages = CardController.GetPackageList();
        for (PackageData pack:
             packages) {
            mRecordAdapter.add(pack.getName());
        }
    }

    @Override
    protected void onAddRecord() {
        Intent intent = new Intent(this, AddPackageActivity.class);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onEditRecord() {
        Intent intent = new Intent(this, AddCardActivity.class);
        intent.putExtra("package", "1");
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onDeleteRecord() {
        Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onViewRecord(int position) {
        final String packageName = mRecordAdapter.getItem(position);
        Toast.makeText(getApplicationContext(), packageName, Toast.LENGTH_SHORT).show();
    }
}
