package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.cards.database.CardsPackageDataProxy;
import com.gmail.ooad.flashcards.slideshow.SlideShowController;
import com.gmail.ooad.flashcards.utils.ShareableEditableListViewActivity;

import java.util.ArrayList;

//import android.app.AlertDialog;

public class CardsPackagesListActivity extends ShareableEditableListViewActivity {
    // request codes for intents
    private static final int SyncList = 100;

    protected ArrayList<ICardsPackageData> mPackages = new ArrayList<>();

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

        ArrayList<CardsPackageDataProxy> packs = CardsController.GetInstance().getPackageList();
        mPackages.addAll(packs);
        for (ICardsPackageData pack:
                mPackages) {
            mRecordAdapter.add(pack.getName());
        }
    }

    @Override
    protected void onAddRecord() {
        Intent intent = new Intent(this, AddCardPackageActivity.class);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onEditRecord() {
        ICardsPackageData data = mPackages.get(mSelected.get(0));

        Intent intent = new Intent(this, EditCardPackageActivity.class);
        intent.putExtra("package", data);
        startActivityForResult(intent, SyncList);
    }

    @Override
    protected void onDeleteRecord() {
        String title = getResources().getString(R.string.menu_action_delete_message_pre);
        title += " " + String.valueOf(mSelected.size()) + " ";
        if (mSelected.size() == 1) {
            title += getResources().getString(R.string.menu_action_delete_cards_packages_message_post_singular);
        } else {
            title += getResources().getString(R.string.menu_action_delete_cards_packages_message_post_plural);
        }

        // Make a copy, because mSelected will be cleared after method return
        final ArrayList<Integer> selected = new ArrayList<>(mSelected);

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.menu_action_delete_packages_title))
                .setMessage(title)
                .setPositiveButton(R.string.menu_action_delete_button_yes,
                        (dialog, which) -> {
                            for (int pos :
                                    selected) {
                                String packageName = mRecordAdapter.getItem(pos);
                                assert packageName != null;
                                CardsController.GetInstance().removePackage(packageName);
                            }
                            syncListWitchDatabase();
                        })
                .setNegativeButton(R.string.menu_action_delete_button_no, null)
                .show();
    }

    @Override
    protected void onViewRecord(int position) {
        String name = mRecordAdapter.getItem(position);
        assert name != null;
        ICardsPackageData data = mPackages.get(position);

        Intent intent = new Intent(this, CardsPackageViewActivity.class);
        intent.putExtra("package", data);
        startActivity(intent);
    }

    @Override
    protected int getColorAccent() {
        return ContextCompat.getColor(this, R.color.colorAccent);
    }

    @Override
    protected CharSequence getCustomTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected void onShare() {
        // TODO
    }

    private void onSlideShow() {
        ArrayList<ICardsPackageData> packs = new ArrayList<>();
        for (int idx :
                mSelected) {
            packs.add(mPackages.get(idx));
        }
        SlideShowController.GetInstance().startSlideShow(this, packs);
    }
}
