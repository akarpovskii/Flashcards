package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;

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

        ((TextView)findViewById(R.id.package_name)).setText(mPackage.getName());
        Button btn = findViewById(R.id.color_button);
        mColor = mPackage.getColor();
        ((GradientDrawable)btn.getBackground()).setColor(mPackage.getColor());

        setTitle(getString(R.string.title_edit) + mPackage.getName());
    }

    @Override
    protected boolean onSavePackage() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.package_name)).getText();

        CardsPackageData data = new CardsPackageData(name.toString(), mColor, null);

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
