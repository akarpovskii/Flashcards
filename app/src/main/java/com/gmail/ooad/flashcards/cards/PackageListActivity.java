package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;

import java.util.ArrayList;

public class PackageListActivity extends ListViewActivity {
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
        String name = mRecordAdapter.getItem(mSelected.get(0));
        assert name != null;
        PackageData data = CardController.GetPackage(name);

        Intent intent = new Intent(this, EditPackageActivity.class);
        intent.putExtra("name", data.getName());
        intent.putExtra("color", data.getColor());
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onDeleteRecord() {
        for (int pos :
                mSelected) {
            String packageName = mRecordAdapter.getItem(pos);
            assert packageName != null;
            CardController.RemovePackage(packageName);
        }

        syncListWitchDatabase();
    }

    @Override
    protected void onViewRecord(int position) {
        String name = mRecordAdapter.getItem(position);
        assert name != null;
        PackageData data = CardController.GetPackage(name);

        Intent intent = new Intent(this, PackageViewActivity.class);
        intent.putExtra("package", data.getName());
        intent.putExtra("color", data.getColor());
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected int getSelectionColor() {
        return getResources().getColor(R.color.colorAccent);
    }
}
