package com.gmail.ooad.flashcards.cards;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.gmail.ooad.symbolskeyboard.model.IEditInterface;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

/*
 * Created by akarpovskii on 02.06.18.
 */
public class TextInputEditTextEx extends TextInputEditText implements IEditInterface {
    public TextInputEditTextEx(Context context) {
        super(context);
    }

    public TextInputEditTextEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputEditTextEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void input(ISymbol symbol) {
        int selectionStart = Math.max(getSelectionStart(), 0);
        int selectionEnd = Math.max(getSelectionEnd(), 0);

        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        String text = symbol.getUnicode();

        getText().replace(start, end, text, 0, text.length());
        setSelection(start + text.length());
    }

    @Override
    public void backspace() {
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }
}
