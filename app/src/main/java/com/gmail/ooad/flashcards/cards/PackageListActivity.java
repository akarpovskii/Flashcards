package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.slideshow.SlideShowController;

import java.util.ArrayList;

public class PackageListActivity extends ListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

    protected ArrayList<IPackageData> mPackages = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.package_list_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(mListMode) {
            case View:
                menu.findItem(R.id.action_slide_show).setVisible(false);
                break;
            case Selection:
                menu.findItem(R.id.action_slide_show).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_slide_show:
                onSlideShow();
                switchListMode();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        mPackages.clear();

        ArrayList<PackageDataProxy> packs = CardController.GetPackageList();
        mPackages.addAll(packs);
        for (IPackageData pack:
                mPackages) {
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
        IPackageData data = mPackages.get(mSelected.get(0));

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
        IPackageData data = mPackages.get(position);

        Intent intent = new Intent(this, PackageViewActivity.class);
        intent.putExtra("package", data.getName());
        intent.putExtra("color", data.getColor());
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected int getColorAccent() {
        return getResources().getColor(R.color.colorAccent);
    }

    private void onSlideShow() {
        ArrayList<IPackageData> packs = new ArrayList<>();
        for (int idx :
                mSelected) {
            packs.add(mPackages.get(idx));
        }
        SlideShowController.StartSlideShow(this, packs);
    }
}
