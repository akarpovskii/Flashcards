package com.gmail.ooad.flashcards;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.Selection;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> mPackages = new ArrayList<>();

    private ArrayAdapter<String> mPackagesAdapter;

    private enum ListMode {
        View,
        Selection,
    }

    private ListMode mListMode;

    private ArrayList<Integer> mSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add_package = findViewById(R.id.add_package);
        add_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPackage();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPackagesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                mPackages);

        final ListView recordsList = findViewById(R.id.packagesList);
        recordsList.setAdapter(mPackagesAdapter);

        mListMode = ListMode.Selection;  // since we switch it in the next method
        switchChoiceMode();

        // registerForContextMenu(recordsList);
        // recordsList.switchChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mListMode == ListMode.Selection){
            switchChoiceMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

    private int mIdx = 0;
    void onAddPackage() {
        mPackagesAdapter.add("Package" + mIdx);
        mIdx++;
    }

    void onViewPackage(int position) {
        final String packageName = mPackagesAdapter.getItem(position);
        Toast.makeText(getApplicationContext(), packageName, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, ViewCategoryActivity.class);
//        intent.putExtra("category", category);
//        startActivity(intent);
    }

    void switchChoiceMode() {
        final ListView recordsList = findViewById(R.id.packagesList);

        mListMode = mListMode == ListMode.View ? ListMode.Selection : ListMode.View;

        switch (mListMode) {
            case View:
                recordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        onViewPackage(pos);
                    }
                });

                recordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        mSelected.add(pos);

                        view.setBackgroundColor(Color.RED);

                        switchChoiceMode();
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
                            view.setBackgroundColor(Color.RED);
                        }

                    }
                });

                recordsList.setOnItemLongClickListener(null);
                break;
        }
    }
}
