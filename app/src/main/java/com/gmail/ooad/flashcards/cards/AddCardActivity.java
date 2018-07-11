package com.gmail.ooad.flashcards.cards;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.even.mricheditor.RichEditor;
import com.even.mricheditor.RichEditorPopup;
import com.gmail.ooad.flashcards.R;
import com.gmail.ooad.flashcards.symbols.KeyboardSymbolPackagesProvider;
import com.gmail.ooad.flipablecardview.CardEditFragment;
import com.gmail.ooad.symbolskeyboard.SimpleRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.SymbolsPopup;

public class AddCardActivity extends AppCompatActivity {
    protected String mPackage;

    protected SymbolsPopup mSymbolsPopup;
    protected RichEditorPopup mRichEditorPopup;

    protected RichEditor mEditFront;
    protected RichEditor mEditBack;
    private boolean mInited;

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
        int color = intent.getIntExtra("color", getResources().getColor(R.color.colorPrimary));
        assert mPackage != null;

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, CardEditFragment
                            .NewInstance(color, null))
                    .commitNow();
        }

        final FloatingActionButton keyboardButton = findViewById(R.id.symbols_button);
        keyboardButton.setColorFilter(ContextCompat.getColor(this, R.color.symbol_icons),
                PorterDuff.Mode.SRC_IN);

        keyboardButton.setTag("keyboard");
        keyboardButton.setOnClickListener(ignore -> {
            String tag = (String) keyboardButton.getTag();
            switch (tag) {
                case "keyboard":
                    mSymbolsPopup.toggle();
                    keyboardButton.setTag("symbols");
                    break;
                case "symbols":
                    mSymbolsPopup.dismiss();
                    keyboardButton.setTag("richedit");
                    mRichEditorPopup.toggle();
                    break;
                default:
                    mRichEditorPopup.dismiss();
                    keyboardButton.setTag("keyboard");
                    break;
            }
        });
    }

    // Since edit front and edit back are initialized after the onCreate method
    // we used to initialize popups here
    @Override
    protected void onResume() {
        super.onResume();
        if (mInited) {
            return;
        }

        CardEditFragment fragment = (CardEditFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        mEditFront = fragment.getEditorFront();
        mEditBack = fragment.getEditorBack();

        ViewGroup rootView = findViewById(R.id.add_card_root);
        final FloatingActionButton keyboardButton = findViewById(R.id.symbols_button);

        mSymbolsPopup = SymbolsPopup.Builder.fromRootView(rootView)
                .setOnPopupKeyboardShownListener(() -> keyboardButton.setImageResource(R.drawable.ic_text_format_black_24dp))
                .setOnPopupKeyboardDismissListener(() -> {
                    keyboardButton.setImageResource(R.drawable.ic_translate_24dp);
                    keyboardButton.setTag("keyboard");
                })
                .build(new KeyboardSymbolPackagesProvider(), new RichEditorAdapter(this, mEditFront),
                        new SimpleRecentSymbolsManager(getApplicationContext()));

        mRichEditorPopup = RichEditorPopup.Builder.fromRootView(rootView)
                .setOnPopupKeyboardShownListener(() -> keyboardButton.setImageResource(R.drawable.ic_keyboard))
                .setOnPopupKeyboardDismissListener(() -> {
                    keyboardButton.setImageResource(R.drawable.ic_translate_24dp);
                    keyboardButton.setTag("keyboard");
                })
                .build(mEditFront);

        mEditFront.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                RichEditorAdapter adapter = new RichEditorAdapter(this, mEditFront);
                mSymbolsPopup.setEditInterface(adapter);
                mRichEditorPopup.setRichEditor(mEditFront);
            }
        });

        mEditBack.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                RichEditorAdapter adapter = new RichEditorAdapter(this, mEditBack);
                mSymbolsPopup.setEditInterface(adapter);
                mRichEditorPopup.setRichEditor(mEditBack);
            }
        });

        mInited = true;
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
                    if (mRichEditorPopup.isShowing()) {
                        mRichEditorPopup.dismiss();
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
        CharSequence front = mEditFront.getHtml();
        CharSequence back = mEditBack.getHtml();
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
        }

        return false;
    }
}
