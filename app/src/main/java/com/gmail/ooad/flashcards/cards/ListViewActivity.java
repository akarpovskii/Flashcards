package com.gmail.ooad.flashcards.cards;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gmail.ooad.flashcards.R;

import java.util.ArrayList;

public abstract class ListViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected ArrayList<String> mRecords = new ArrayList<>();

    protected ArrayAdapter<String> mRecordAdapter;

    protected enum ListMode {
        View,
        Selection,
    }

    protected ListMode mListMode;

    protected ArrayList<Integer> mSelected = new ArrayList<>();

    protected Menu mMenu;  // we need this for inflation of a new menu outside the onCreateOptionsMenu

    protected ActionBarDrawerToggle mDrawerToggle;

    // request codes for intents
//    protected static final int SyncList = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add_record = findViewById(R.id.add_record);
        add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddRecord();
            }
        });

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
        ActionBar bar = getSupportActionBar();

        mMenu.clear();

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
                getMenuInflater().inflate(R.menu.list_view_view_mode, mMenu);
                break;

            case Selection:
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                if (bar != null) {
                    bar.setDisplayHomeAsUpEnabled(true);
                }
                getMenuInflater().inflate(R.menu.list_view_selection_mode, mMenu);
                break;
        }

        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                onEditRecord();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    abstract protected void syncListWitchDatabase();

    abstract protected void onAddRecord();

    abstract protected void onEditRecord();

    abstract protected void onDeleteRecord();

    abstract protected void onViewRecord(int position);

    protected void switchListMode() {
        final ListView recordsList = findViewById(R.id.recordList);

        mListMode = mListMode == ListMode.View ? ListMode.Selection : ListMode.View;

        switch (mListMode) {
            case View:
                recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        onViewRecord(pos);
                    }
                });

                recordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        mSelected.add(pos);
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        switchListMode();
                        return true;
                    }
                });
                break;
            case Selection:
                recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        int idx = mSelected.indexOf(pos);
                        boolean selected = idx != -1;

                        if (selected) {
                            mSelected.remove(idx);
                            view.setBackgroundColor(Color.TRANSPARENT);
                        } else {
                            mSelected.add(pos);
                            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }

                        mMenu.findItem(R.id.action_edit).setVisible(mSelected.size() == 1);
                    }
                });

                recordsList.setOnItemLongClickListener(null);
                break;
        }

        invalidateOptionsMenu();
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
}
