package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.ColorPalette;
import com.gmail.ooad.flashcards.utils.ColorUtil;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class AddCardPackageActivity extends AppCompatActivity implements ColorPickerDialogListener {
    protected ColorPalette mPalette;

    protected int mCardsColorInterpolation = 100;

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

        mPalette = ColorUtil.GetNearest(ContextCompat.getColor(this, R.color.colorDefault));

        Button btn = findViewById(R.id.color_button);
        setColorButtonColor(mPalette.getPrimary());

        btn.setOnClickListener(view -> pickColor());

        SeekBar seekBar = findViewById(R.id.cards_alpha_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCardsColorInterpolation = progress;

                setCardsColorButtonColor(ColorUtil.Interpolate(mPalette.getPrimary(),
                        mCardsColorInterpolation / 100.f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(seekBar.getMax()); // initialize the button color
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
        CardsPackageData data = new CardsPackageData(name.toString(), new PackagePalette(mPalette,
                ColorUtil.Interpolate(mPalette.getPrimary(),
                        mCardsColorInterpolation / 100.f)), null);

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
        final ColorPalette[] palettes = ColorUtil.GetAllPallettes();
        int [] colors = new int[palettes.length];
        for (int i = 0; i < palettes.length; ++i) {
            colors[i] = palettes[i].getPrimary();
        }

        ColorPickerDialog.newBuilder()
                .setAllowCustom(false)
                .setPresets(colors)
                .setColor(mPalette.getPrimary())
                .setShowColorShades(false)
                .show(this);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        mPalette = ColorUtil.GetNearest(color);
        setColorButtonColor(mPalette.getPrimary());

        setCardsColorButtonColor(ColorUtil.Interpolate(mPalette.getPrimary(),
                mCardsColorInterpolation / 100.f));
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        // do nothing
    }

    protected void setColorButtonColor(int color) {
        Button btn = findViewById(R.id.color_button);
        ((GradientDrawable)btn.getBackground()).setColor(color);
    }

    protected void setCardsColorButtonColor(int color) {
        Button btn = findViewById(R.id.cards_alpha_button);
        ((GradientDrawable)btn.getBackground()).setColor(color);
    }
}
