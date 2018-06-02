package com.gmail.ooad.symbolskeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.gmail.ooad.symbolskeyboard.listeners.OnSoftKeyboardCloseListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSoftKeyboardOpenListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolBackspaceClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolClickListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolPopupDismissListener;
import com.gmail.ooad.symbolskeyboard.listeners.OnSymbolPopupShownListener;
import com.gmail.ooad.symbolskeyboard.model.IEditInterface;
import com.gmail.ooad.symbolskeyboard.model.IRecentSymbolsManager;
import com.gmail.ooad.symbolskeyboard.model.ISymbolsPackagesProvider;

/*
 * Created by akarpovskii on 14.05.18.
 */
public class SymbolsPopup {
    private static final int MIN_KEYBOARD_HEIGHT = 100;

    private final View mRootView;
    private final Activity mContext;

    private final PopupWindow mPopupWindow;
    private final IRecentSymbolsManager mRecentSymbolsManager;
    private IEditInterface mEditInterface;

    private boolean mIsPendingOpen;
    private boolean mIsKeyboardOpen;

    @Nullable
    OnSymbolPopupShownListener mOnSymbolsPopupShownListener;
    @Nullable
    OnSoftKeyboardCloseListener mOnSoftKeyboardCloseListener;
    @Nullable
    OnSoftKeyboardOpenListener mOnSoftKeyboardOpenListener;

    @Nullable
    OnSymbolBackspaceClickListener mOnSymbolsBackspaceClickListener;
    @Nullable
    OnSymbolClickListener mOnSymbolsClickListener;
    @Nullable
    OnSymbolPopupDismissListener mOnSymbolsPopupDismissListener;

    private final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override public void onGlobalLayout() {
            final Rect rect = Utils.windowVisibleDisplayFrame(mContext);
            final int heightDifference = Utils.screenHeight(mContext) - rect.bottom;

            if (heightDifference > Utils.dpToPx(mContext, MIN_KEYBOARD_HEIGHT)) {
                mPopupWindow.setHeight(heightDifference);
                mPopupWindow.setWidth(rect.right);

                if (!mIsKeyboardOpen && mOnSoftKeyboardOpenListener != null) {
                    mOnSoftKeyboardOpenListener.onKeyboardOpen(heightDifference);
                }

                mIsKeyboardOpen = true;

                if (mIsPendingOpen) {
                    showAtBottom();
                    mIsPendingOpen = false;
                }
            } else {
                if (mIsKeyboardOpen) {
                    mIsKeyboardOpen = false;

                    if (mOnSoftKeyboardCloseListener != null) {
                        mOnSoftKeyboardCloseListener.onKeyboardClose();
                    }

                    dismiss();
                    Utils.removeOnGlobalLayoutListener(mContext.getWindow().getDecorView(), onGlobalLayoutListener);
                }
            }
        }
    };

    SymbolsPopup(@NonNull final View rootView, ISymbolsPackagesProvider provider,
                 @NonNull final IEditInterface editInterface,
                 @Nullable final IRecentSymbolsManager recentSymbolsManager) {
        mContext = Utils.asActivity(rootView.getContext());
        mRootView = rootView.getRootView();
        mEditInterface = editInterface;
        mRecentSymbolsManager = recentSymbolsManager != null ?
                recentSymbolsManager :
                new SimpleRecentSymbolsManager(mContext);

        mPopupWindow = new PopupWindow(mContext);

//        final OnSymbolLongClickListener longClickListener = new OnSymbolLongClickListener() {
//            @Override public void onSymbolsLongClick(@NonNull final SymbolTextView view, @NonNull final ISymbol symbol) {
//                variantPopup.show(view, symbol);
//            }
//        };

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

        mPopupWindow.setContentView(symbolView);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null)); // To avoid borders and overdraw.
        mPopupWindow.setOnDismissListener(() -> {
            if (mOnSymbolsPopupDismissListener != null) {
                mOnSymbolsPopupDismissListener.onSymbolsPopupDismiss();
            }
        });
    }

    public void toggle() {
        if (!mPopupWindow.isShowing()) {
            // Remove any previous listeners to avoid duplicates.
            Utils.removeOnGlobalLayoutListener(mContext.getWindow().getDecorView(), onGlobalLayoutListener);
            mContext.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

            if (mIsKeyboardOpen) {
                // If the keyboard is visible, simply show the symbol popup.
                showAtBottom();
            } else if (mEditInterface instanceof View) {
                final View view = (View) mEditInterface;

                // Open the text keyboard first and immediately after that show the symbol popup.
                view.setFocusableInTouchMode(true);
                view.requestFocus();

                showAtBottomPending();

                final InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            } else {
                throw new IllegalArgumentException("The provided editInterace isn't a View instance.");
            }
        } else {
            dismiss();
        }

        // Manually dispatch the event. In some cases this does not work out of the box reliably.
        mContext.getWindow().getDecorView().getViewTreeObserver().dispatchOnGlobalLayout();
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
        mRecentSymbolsManager.persist();
    }

    private void showAtBottom() {
        final Point desiredLocation = new Point(0, Utils.screenHeight(mContext) - mPopupWindow.getHeight());

        mPopupWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, desiredLocation.x, desiredLocation.y);
        Utils.fixPopupLocation(mPopupWindow, desiredLocation);

        if (mOnSymbolsPopupShownListener != null) {
            mOnSymbolsPopupShownListener.onSymbolsPopupShown();
        }
    }

    private void showAtBottomPending() {
        if (mIsKeyboardOpen) {
            showAtBottom();
        } else {
            mIsPendingOpen = true;
        }
    }

    public void setEditInterface(@NonNull final IEditInterface editInterface) {
        this.mEditInterface = editInterface;
    }

    public static final class Builder {
        @NonNull private final View rootView;
        @Nullable private OnSymbolPopupShownListener onSymbolsPopupShownListener;
        @Nullable private OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
        @Nullable private OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;
        @Nullable private OnSymbolBackspaceClickListener onSymbolsBackspaceClickListener;
        @Nullable private OnSymbolClickListener onSymbolsClickListener;
        @Nullable private OnSymbolPopupDismissListener onSymbolsPopupDismissListener;

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

        @CheckResult public Builder setOnSymbolsClickListener(@Nullable final OnSymbolClickListener listener) {
            onSymbolsClickListener = listener;
            return this;
        }

        @CheckResult public Builder setOnSoftKeyboardOpenListener(@Nullable final OnSoftKeyboardOpenListener listener) {
            onSoftKeyboardOpenListener = listener;
            return this;
        }

        @CheckResult public Builder setOnSymbolsPopupShownListener(@Nullable final OnSymbolPopupShownListener listener) {
            onSymbolsPopupShownListener = listener;
            return this;
        }

        @CheckResult public Builder setOnSymbolsPopupDismissListener(@Nullable final OnSymbolPopupDismissListener listener) {
            onSymbolsPopupDismissListener = listener;
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
            symbolPopup.mOnSymbolsPopupShownListener = onSymbolsPopupShownListener;
            symbolPopup.mOnSymbolsPopupDismissListener = onSymbolsPopupDismissListener;
            symbolPopup.mOnSymbolsBackspaceClickListener = onSymbolsBackspaceClickListener;
            return symbolPopup;
        }
    }
}
