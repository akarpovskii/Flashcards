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
public class EditPackageActivity extends AddPackageActivity {

    protected String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        assert mName != null;
        mColor = intent.getIntExtra("color", 0);

        ((TextView)findViewById(R.id.package_name)).setText(mName);
        Button btn = findViewById(R.id.color_button);
        ((GradientDrawable)btn.getBackground()).setColor(mColor);

        setTitle("Edit" + mName);
    }

    @Override
    protected boolean onSavePackage() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.package_name)).getText();
        PackageData data = new PackageData(name.toString(), mColor, null);

        String oldName = mName.equals(name.toString()) ? null : mName;
        if (CardController.UpdatePackage(data, oldName)) {
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
}
