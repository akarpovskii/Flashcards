package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class AddPackageActivity extends AppCompatActivity {
    protected int mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(R.string.activity_add_package_title);
            bar.setDisplayHomeAsUpEnabled(true);
        }

        mColor = getResources().getColor(R.color.colorPrimaryDark);

        Button btn = findViewById(R.id.color_button);
        ((GradientDrawable)btn.getBackground()).setColor(mColor);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickColor();
            }
        });
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
        PackageData data = new PackageData(name.toString(), mColor, null);

        if (CardController.AddPackage(data)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        } else {
            Toast.makeText(getApplicationContext(),
                    "A package with this name already exists. Please, try another one",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void pickColor() {
        final ColorPicker cp = new ColorPicker(AddPackageActivity.this,
                Color.red(mColor), Color.green(mColor), Color.blue(mColor));

        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                mColor = color;
                Button btn = findViewById(R.id.color_button);
                ((GradientDrawable)btn.getBackground()).setColor(mColor);
                cp.dismiss();
            }
        });

        cp.show();
    }
}
