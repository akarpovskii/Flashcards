package com.gmail.ooad.flashcards.utils;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.cards.CardsPackagesListActivity;
import com.gmail.ooad.flashcards.symbols.SymbolsPackagesListViewActivity;

import java.util.ArrayList;

public abstract class EditableListViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected ArrayList<String> mRecords = new ArrayList<>();

    protected ArrayAdapter<String> mRecordAdapter;

    protected enum ListMode {
        View,
        Selection,
    }

    protected ListMode mListMode;

    protected ArrayList<Integer> mSelected = new ArrayList<>();

    protected ActionBarDrawerToggle mDrawerToggle;

    // We need this for show/hide options dynamically (see switchListMode method)
    protected Menu mMenu;

    protected int mItemId;

    protected boolean mItemClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_list_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add_record = findViewById(R.id.add_record);
        add_record.setOnClickListener(ignore -> onAddRecord());
        add_record.setBackgroundTintList(ColorStateList.valueOf(getColorAccent()));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (mListMode == ListMode.Selection) {
                    switchListMode();
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (mItemClicked) {
                    Intent intent = null;
                    switch (mItemId) {
                        case R.id.nav_cards:
                            intent = new Intent(EditableListViewActivity.this,
                                    CardsPackagesListActivity.class);
                            break;
                        case R.id.nav_symbols:
                            intent = new Intent(EditableListViewActivity.this,
                                    SymbolsPackagesListViewActivity.class);
                            break;
                        case R.id.nav_settings:
                            break;
                    }

                    if (intent != null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        // disable annoying animation
                        overridePendingTransition(0, 0);
                    }
                }
            }
        };

        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecordAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                mRecords);

        ListView recordsList = findViewById(R.id.recordList);
        recordsList.setAdapter(mRecordAdapter);

        mListMode = ListMode.Selection;  // since we switch it in the next method
        switchListMode();

        syncListWitchDatabase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mListMode == ListMode.Selection){
            switchListMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editable_list_view_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // there is a space in orderInCategory in case we want to manage it. See editable_list_view_menu.xml_menu.xml
        mMenu = menu;
        switch (mListMode) {
            case View:
                menu.findItem(R.id.action_edit).setVisible(false);
                menu.findItem(R.id.action_delete).setVisible(false);
                break;
            case Selection:
                menu.findItem(R.id.action_edit).setVisible(true);
                menu.findItem(R.id.action_delete).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                onEditRecord();
                switchListMode();
                return true;
            case R.id.action_delete:
                onDeleteRecord();
                switchListMode();
                return true;
            case android.R.id.home:
                switchListMode();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mItemClicked = true;
        mItemId = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void switchListMode() {
        final ListView recordsList = findViewById(R.id.recordList);

        mListMode = mListMode == ListMode.View ? ListMode.Selection : ListMode.View;

        switch (mListMode) {
            case View:
                recordsList.setOnItemClickListener((adapterView, view, pos, id) -> onViewRecord(pos));

                recordsList.setOnItemLongClickListener((adapterView, view, pos, id) -> {
                    mSelected.add(pos);
                    view.setBackgroundColor(getColorAccent());
                    switchListMode();
                    return true;
                });
                setTitle(getCustomTitle());
                break;
            case Selection:
                recordsList.setOnItemClickListener((adapterView, view, pos, id) -> {
                    int idx = mSelected.indexOf(pos);
                    boolean selected = idx != -1;

                    if (selected) {
                        mSelected.remove(idx);
                        view.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        mSelected.add(pos);
                        view.setBackgroundColor(getColorAccent());
                    }

                    if (mSelected.size() == 0) {
                        switchListMode();
                    }

                    mMenu.findItem(R.id.action_edit).setVisible(mSelected.size() == 1);
                });

                recordsList.setOnItemLongClickListener(null);

                setTitle("");
                break;
        }

        switchOptionsMenu();
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    protected void switchOptionsMenu() {
        ActionBar bar = getSupportActionBar();

        switch (mListMode) {
            case View:
                for (int pos :
                        mSelected) {
                    ListView packagesList = findViewById(R.id.recordList);
                    getViewByPosition(pos, packagesList).setBackgroundColor(Color.TRANSPARENT);
                }

                mSelected.clear();

                if (bar != null) {
                    bar.setDisplayHomeAsUpEnabled(false);
                }
                mDrawerToggle.setToolbarNavigationClickListener(null);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                break;

            case Selection:
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                mDrawerToggle.setToolbarNavigationClickListener(view -> onBackPressed());
                if (bar != null) {
                    bar.setDisplayHomeAsUpEnabled(true);
                }
                break;
        }

        supportInvalidateOptionsMenu();
    }

    abstract protected void syncListWitchDatabase();

    abstract protected void onAddRecord();

    abstract protected void onEditRecord();

    abstract protected void onDeleteRecord();

    abstract protected void onViewRecord(int position);

    abstract protected int getColorAccent();

    abstract protected CharSequence getCustomTitle();
}
