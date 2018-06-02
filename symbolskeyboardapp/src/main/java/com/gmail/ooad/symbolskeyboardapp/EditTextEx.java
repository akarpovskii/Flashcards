package com.gmail.ooad.symbolskeyboardapp;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.gmail.ooad.symbolskeyboard.model.IEditInterface;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

/*
 * Created by akarpovskii on 02.06.18.
 */
public class EditTextEx extends AppCompatEditText implements IEditInterface {
    public EditTextEx(Context context) {
        super(context);
    }

    public EditTextEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void input(ISymbol symbol) {
        append(symbol.getUnicode());
    }

    @Override
    public void backspace() {
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }
}
