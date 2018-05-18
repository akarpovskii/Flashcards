package com.gmail.ooad.flashcards.utils;

import android.view.Menu;
import android.view.MenuItem;

import com.gmail.ooad.flashcards.R;

/*
 * Created by akarpovskii on 09.05.18.
 */
public abstract class ShareableEditableListViewActivity extends EditableListViewActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shareable_editable_list_view_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(mListMode) {
            case View:
                menu.findItem(R.id.action_share).setVisible(false);
                break;
            case Selection:
                menu.findItem(R.id.action_share).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                onShare();
                switchListMode();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void onShare();
}
