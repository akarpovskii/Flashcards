package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.utils.ColorUtil;

/*
 * Created by akarpovskii on 27.04.18.
 */
public class EditCardPackageActivity extends AddCardPackageActivity {

    protected ICardsPackageData mPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPackage = intent.getParcelableExtra("package");

        setTitle(getString(R.string.title_edit) + mPackage.getName());

        mCardsColorInterpolation = (int) (Deinterpolate(mPackage.getPalette().getPrimary(),
                mPackage.getPalette().getCardsColor()) * 100);

                ((TextView)findViewById(R.id.package_name)).setText(mPackage.getName());
        mPalette = ColorUtil.GetNearest(mPackage.getPalette().getPrimary());
        setColorButtonColor(mPalette.getPrimary());

        SeekBar seekBar = findViewById(R.id.cards_alpha_seekbar);
        seekBar.setProgress(mCardsColorInterpolation);
        setCardsColorButtonColor(mPackage.getPalette().getCardsColor());
    }

    @Override
    protected boolean onSavePackage() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.package_name)).getText();

        CardsPackageData data = new CardsPackageData(name.toString(), new PackagePalette(mPalette,
                Interpolate(mPalette.getPrimary(), mCardsColorInterpolation / 100.f)), null);

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_card_already_exists,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        String oldName = mPackage.getName().equals(name.toString()) ? null : mPackage.getName();

        if (CardsController.GetInstance().updatePackage(data, oldName)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        }

        return false;
    }
}
