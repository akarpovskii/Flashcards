package com.gmail.ooad.symbolskeyboardapp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gmail.ooad.symbolkeyboard.R;
import com.gmail.ooad.symbolskeyboard.SimpleRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.SymbolsPopup;

// We don't care about duplicated code in the sample.
@SuppressWarnings("CPD-START") public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";

    SymbolsPopup emojiPopup;

    EditTextEx editText;
    ViewGroup rootView;
    ImageView emojiButton;

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.main_activity_chat_bottom_message_edittext);
        rootView = findViewById(R.id.main_activity_root_view);
        emojiButton = findViewById(R.id.main_activity_emoji);
        final ImageView sendButton = findViewById(R.id.main_activity_send);

        emojiButton.setColorFilter(ContextCompat.getColor(this, R.color.symbol_icons), PorterDuff.Mode.SRC_IN);
        sendButton.setColorFilter(ContextCompat.getColor(this, R.color.symbol_icons), PorterDuff.Mode.SRC_IN);

        emojiButton.setOnClickListener(ignore -> emojiPopup.toggle());
        sendButton.setOnClickListener(ignore -> {
            final String text = editText.getText().toString().trim();

            if (text.length() > 0) {

                editText.setText("");
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.main_activity_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setUpEmojiPopup();
    }

    @Override public boolean onCreateOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public void onBackPressed() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }

        super.onStop();
    }

    private void setUpEmojiPopup() {
        emojiPopup = SymbolsPopup.Builder.fromRootView(rootView)
                .setOnSymbolsBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
                .setOnSymbolsClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
                .setOnPopupKeyboardShownListener(() -> emojiButton.setImageResource(android.R.drawable.ic_menu_edit))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
                .setOnPopupKeyboardDismissListener(() -> emojiButton.setImageResource(android.R.drawable.arrow_down_float))
                .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
                .build(new TestPackageProvider(), editText, new SimpleRecentSymbolsManager(getApplicationContext()));
    }
}
