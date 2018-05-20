package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.symbols.KeyboardSymbolPackagesProvider;
import com.gmail.ooad.symbolskeyboard.RecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.SymbolsPopup;

public class AddCardActivity extends AppCompatActivity {
    protected String mPackage;

    SymbolsPopup mSymbolsPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(R.string.activity_add_card_title);

        Intent intent = getIntent();
        mPackage = intent.getStringExtra("package");
        assert mPackage != null;

        final TextInputEditText editName = findViewById(R.id.card_name);
        final TextInputEditText editFront = findViewById(R.id.card_front);
        final TextInputEditText editBack = findViewById(R.id.card_back);

        editName.setOnFocusChangeListener((v, hasFocus) -> mSymbolsPopup.setEditInterface(editName));

        editFront.setOnFocusChangeListener((v, hasFocus) -> mSymbolsPopup.setEditInterface(editFront));

        editBack.setOnFocusChangeListener((v, hasFocus) -> mSymbolsPopup.setEditInterface(editBack));

        ViewGroup rootView = findViewById(R.id.add_card_root);
        final FloatingActionButton emojiButton = findViewById(R.id.emoji_button);
        emojiButton.setColorFilter(ContextCompat.getColor(this, R.color.symbol_icons),
                PorterDuff.Mode.SRC_IN);
        emojiButton.setOnClickListener(ignore -> mSymbolsPopup.toggle());

        mSymbolsPopup = SymbolsPopup.Builder.fromRootView(rootView)
                .setOnSymbolsBackspaceClickListener(ignore -> Log.d("Emoji", "Clicked on Backspace"))
                .setOnSymbolsClickListener((ignore, ignore2) -> Log.d("Emoji", "Clicked on emoji"))
                .setOnSymbolsPopupShownListener(() -> emojiButton.setImageResource(R.drawable.ic_keyboard))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d("Emoji", "Opened soft keyboard"))
                .setOnSymbolsPopupDismissListener(() -> emojiButton.setImageResource(R.mipmap.ic_msg_panel_smiles))
                .setOnSoftKeyboardCloseListener(() -> Log.d("Emoji", "Closed soft keyboard"))
                .build(new KeyboardSymbolPackagesProvider(), editName,
                        new RecentSymbolsManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mSymbolsPopup.isShowing()) {
                    mSymbolsPopup.dismiss();
                }
                finish();
                return true;
            case R.id.action_save:
                if (onSaveCard()) {
                    if (mSymbolsPopup.isShowing()) {
                        mSymbolsPopup.dismiss();
                    }
                    finish();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected boolean onSaveCard() {
        CharSequence name = ((TextInputEditText)findViewById(R.id.card_name)).getText();
        CharSequence front = ((TextInputEditText)findViewById(R.id.card_front)).getText();
        CharSequence back = ((TextInputEditText)findViewById(R.id.card_back)).getText();
        CardData data = new CardData(name.toString(), front.toString(), back.toString());

        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_enter_the_name,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (CardsController.GetInstance().hasCard(mPackage, data.getName())) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_card_already_exists,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (CardsController.GetInstance().addCard(mPackage, data)) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            return true;
        } else {
            return false;
        }
    }
}
