package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class AddCardPackageActivity extends AppCompatActivity {
    protected int mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.activity_add_package_title);

        mColor = ContextCompat.getColor(this, R.color.colorDefault);

        Button btn = findViewById(R.id.color_button);
        ((GradientDrawable)btn.getBackground()).setColor(mColor);

        btn.setOnClickListener(view -> pickColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_package_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if (onSavePackage()) {
                    finish();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected boolean onSavePackage() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.package_name)).getText();
        CardsPackageData data = new CardsPackageData(name.toString(), mColor, null);

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_enter_the_name,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (CardsController.GetInstance().hasPackage(data.getName())) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_package_already_exists,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (CardsController.GetInstance().addPackage(data)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        } else {
            return false;
        }
    }

    private void pickColor() {
        final ColorPicker cp = new ColorPicker(AddCardPackageActivity.this,
                Color.red(mColor), Color.green(mColor), Color.blue(mColor));

        cp.setCallback(color -> {
            mColor = color;
            Button btn = findViewById(R.id.color_button);
            ((GradientDrawable)btn.getBackground()).setColor(mColor);
            cp.dismiss();
        });

        cp.show();
    }
}
