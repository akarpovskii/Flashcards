package com.gmail.ooad.symbolskeyboard;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolBackspaceClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.model.IEditInterface;
import com.gmail.ooad.symbolskeyboard.model.IRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackagesProvider;
import com.gmail.ooad.utilities.popupkeyboard.PopupKeyboard;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardDismissListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardShownListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardCloseListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardOpenListener;

/*
 * Created by akarpovskii on 14.05.18.
 */
public class SymbolsPopup extends PopupKeyboard {
    private final IRecentSymbolsManager mRecentSymbolsManager;
    private IEditInterface mEditInterface;

    @Nullable
    OnSymbolBackspaceClickListener mOnSymbolsBackspaceClickListener;
    @Nullable
    OnSymbolClickListener mOnSymbolsClickListener;

    SymbolsPopup(@NonNull final View rootView, ISymbolsPackagesProvider provider,
                 @NonNull final IEditInterface editInterface,
                 @Nullable final IRecentSymbolsManager recentSymbolsManager) {
        super(rootView);
        mEditInterface = editInterface;
        mRecentSymbolsManager = recentSymbolsManager != null ?
                recentSymbolsManager :
                new SimpleRecentSymbolsManager(mContext);


        final OnSymbolClickListener clickListener = (imageView, symbol) -> {
            SymbolsPopup.this.mEditInterface.input(symbol);

            mRecentSymbolsManager.addSymbol(symbol);
            if (mOnSymbolsClickListener != null) {
                mOnSymbolsClickListener.onSymbolClick(imageView, symbol);
            }
        };

        final SymbolsView symbolView = new SymbolsView(mContext, provider, clickListener, null, mRecentSymbolsManager);
        symbolView.setOnSymbolBackspaceClickListener(v -> {
            SymbolsPopup.this.mEditInterface.backspace();
            // dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));

            if (mOnSymbolsBackspaceClickListener != null) {
                mOnSymbolsBackspaceClickListener.onSymbolBackspaceClick(v);
            }
        });

        setPopupView(symbolView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mRecentSymbolsManager.persist();
    }

    @Override
    protected void showAtBottom() {
        super.showAtBottom();
        if (mEditInterface instanceof View) {
            final View view = (View) mEditInterface;
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }  else {
            throw new IllegalArgumentException("The provided edit interface isn't a View instance.");
        }
    }

    public void setEditInterface(@NonNull final IEditInterface editInterface) {
        this.mEditInterface = editInterface;
    }

    public static final class Builder{
        @NonNull private final View rootView;
        @Nullable private OnPopupKeyboardShownListener onPopupKeyboardShownListener;
        @Nullable private OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
        @Nullable private OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;
        @Nullable private OnPopupKeyboardDismissListener onPopupKeyboardDismissListener;
        @Nullable private OnSymbolBackspaceClickListener onSymbolsBackspaceClickListener;
        @Nullable private OnSymbolClickListener onSymbolsClickListener;

        private Builder(@NonNull final View rootView) {
            this.rootView = rootView;
        }

        /**
         * @param rootView The root View of your layout.xml which will be used for calculating the height
         *                 of the keyboard.
         * @return builder For building the {@link SymbolsPopup}.
         */
        @CheckResult
        public static Builder fromRootView(final View rootView) {
            return new Builder(rootView);
        }

        @CheckResult public Builder setOnSoftKeyboardCloseListener(@Nullable final OnSoftKeyboardCloseListener listener) {
            onSoftKeyboardCloseListener = listener;
            return this;
        }

        @CheckResult public Builder setOnSoftKeyboardOpenListener(@Nullable final OnSoftKeyboardOpenListener listener) {
            onSoftKeyboardOpenListener = listener;
            return this;
        }

        @CheckResult public Builder setOnPopupKeyboardShownListener(@Nullable final OnPopupKeyboardShownListener listener) {
            onPopupKeyboardShownListener = listener;
            return this;
        }

        @CheckResult public Builder setOnPopupKeyboardDismissListener(@Nullable final OnPopupKeyboardDismissListener listener) {
            onPopupKeyboardDismissListener = listener;
            return this;
        }

        @CheckResult public Builder setOnSymbolsClickListener(@Nullable final OnSymbolClickListener listener) {
            onSymbolsClickListener = listener;
            return this;
        }

        @CheckResult public Builder setOnSymbolsBackspaceClickListener(@Nullable final OnSymbolBackspaceClickListener listener) {
            onSymbolsBackspaceClickListener = listener;
            return this;
        }

        @CheckResult public SymbolsPopup build(@NonNull final ISymbolsPackagesProvider provider,
                                               @NonNull final IEditInterface editInterface,
                                               @Nullable final IRecentSymbolsManager recentSymbolsManager) {
            final SymbolsPopup symbolPopup = new SymbolsPopup(rootView, provider, editInterface, recentSymbolsManager);
            symbolPopup.mOnSoftKeyboardCloseListener = onSoftKeyboardCloseListener;
            symbolPopup.mOnSymbolsClickListener = onSymbolsClickListener;
            symbolPopup.mOnSoftKeyboardOpenListener = onSoftKeyboardOpenListener;
            symbolPopup.mOnPopupKeyboardShownListener = onPopupKeyboardShownListener;
            symbolPopup.mOnPopupKeyboardDismissListener = onPopupKeyboardDismissListener;
            symbolPopup.mOnSymbolsBackspaceClickListener = onSymbolsBackspaceClickListener;
            return symbolPopup;
        }
    }
}
