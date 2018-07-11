package com.gmail.ooad.flashcards.cards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.even.mricheditor.RichEditor;
import com.gmail.ooad.symbolskeyboard.model.IEditInterface;
import com.gmail.ooad.symbolskeyboard.model.ISymbol;

import java.lang.reflect.Method;

import leo.android.cglib.proxy.MethodInterceptor;
import leo.android.cglib.proxy.MethodProxy;

/*
 * Created by akarpovskii on 09.07.18.
 */
@SuppressLint("ViewConstructor")
public class RichEditorAdapter extends RichEditor implements IEditInterface, MethodInterceptor {
    RichEditor mDelegate;

    public RichEditorAdapter(Context context, @NonNull RichEditor delegate) {
        super(context);
        mDelegate = delegate;
    }

    @Override
    public void input(ISymbol symbol) {
        mDelegate.insertText(symbol.getUnicode());
    }

    @Override
    public void backspace() {
        if (isFocused()) {
            mDelegate.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        }
    }

    @Override
    public Object intercept(Object arg0, Object[] objects,
                            MethodProxy methodProxy) throws Exception {
        final Method method = methodProxy.getOriginalMethod();
        Method m = findMethod(this.getClass().getSuperclass(), method);
        if (m != null) {
            return m.invoke(this, objects);
        }
        return method.invoke(mDelegate, objects);
    }

    private Method findMethod(Class<?> clazz, Method method) {
        try {
            return clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
