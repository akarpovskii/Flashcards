package com.gmail.ooad.utilities.popupkeyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.gmail.ooad.utilities.Utilities;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardDismissListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardShownListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardCloseListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardOpenListener;

/*
 * Created by akarpovskii on 07.07.18.
 */
public class PopupKeyboard {
    private static final int MIN_KEYBOARD_HEIGHT = 100;

    private final View mRootView;
    protected final AppCompatActivity mContext;

    private final PopupWindow mPopupWindow;

    private boolean mIsPendingOpen = false;
    private boolean mIsSoftKeyboardOpen = false;

    @Nullable
    protected OnPopupKeyboardShownListener mOnPopupKeyboardShownListener;
    @Nullable
    protected OnPopupKeyboardDismissListener mOnPopupKeyboardDismissListener;
    @Nullable
    protected OnSoftKeyboardOpenListener mOnSoftKeyboardOpenListener;
    @Nullable
    protected OnSoftKeyboardCloseListener mOnSoftKeyboardCloseListener;

    private final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override public void onGlobalLayout() {
                    final Rect rect = Utilities.windowVisibleDisplayFrame(mContext);
                    final int heightDifference = Utilities.screenHeight(mContext) - rect.bottom;
                    final float heightDP = TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_KEYBOARD_HEIGHT,
                                    mContext.getResources().getDisplayMetrics());

                    if (heightDifference > heightDP) {
                        mPopupWindow.setHeight(heightDifference);
                        mPopupWindow.setWidth(rect.right);

                        if (!mIsSoftKeyboardOpen && mOnSoftKeyboardOpenListener != null) {
                            mOnSoftKeyboardOpenListener.onKeyboardOpen(heightDifference);
                        }

                        mIsSoftKeyboardOpen = true;

                        if (mIsPendingOpen) {
                            showAtBottom();
                            mIsPendingOpen = false;
                        }
                    } else {
                        if (mIsSoftKeyboardOpen) {
                            mIsSoftKeyboardOpen = false;

                            if (mOnSoftKeyboardCloseListener != null) {
                                mOnSoftKeyboardCloseListener.onKeyboardClose();
                            }

                            dismiss();
                            mContext.getWindow().getDecorView().getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(onGlobalLayoutListener);
                        }
                    }
                }
            };

    public PopupKeyboard(@NonNull final View rootView) {
        mContext = Utilities.asActivity(rootView.getContext());
        mRootView = rootView.getRootView();

        mPopupWindow = new PopupWindow(mContext);


        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null)); // To avoid borders and overdraw.
        mPopupWindow.setOnDismissListener(() -> {
            if (mOnPopupKeyboardDismissListener != null) {
                mOnPopupKeyboardDismissListener.onPopupKeyboardDismiss();
            }
        });
    }

    public void setPopupView(@NonNull final View popup) {
        mPopupWindow.setContentView(popup);
    }

    public void toggle() {
        if (!mPopupWindow.isShowing()) {
            // Remove any previous listeners to avoid duplicates.
            mContext.getWindow().getDecorView().getViewTreeObserver()
                    .removeOnGlobalLayoutListener(onGlobalLayoutListener);
            mContext.getWindow().getDecorView().getViewTreeObserver()
                    .addOnGlobalLayoutListener(onGlobalLayoutListener);

            // Call it to get the soft keyboard state
            mContext.getWindow().getDecorView().getViewTreeObserver().dispatchOnGlobalLayout();

            if (mIsSoftKeyboardOpen) {
                // If the keyboard is visible, simply show the popup.
                showAtBottom();
            } else {
                // Open the text keyboard first and immediately after that show the popup.
                showAtBottomPending();

                final InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    // We sure the keyboard is off
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        } else {
            dismiss();
        }

        // Manually dispatch the event. In some cases this does not work out of the box reliably.
        mContext.getWindow().getDecorView().getViewTreeObserver()
                .dispatchOnGlobalLayout();
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
        mContext.getWindow().getDecorView().getViewTreeObserver()
                .removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    protected void showAtBottom() {
        final Point desiredLocation = new Point(0, Utilities.screenHeight(mContext) - mPopupWindow.getHeight());

        mPopupWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, desiredLocation.x, desiredLocation.y);
        Utilities.fixPopupLocation(mPopupWindow, desiredLocation);

        if (mOnPopupKeyboardShownListener != null) {
            mOnPopupKeyboardShownListener.onPopupKeyboardShown();
        }
    }

    private void showAtBottomPending() {
        if (mIsSoftKeyboardOpen) {
            showAtBottom();
        } else {
            mIsPendingOpen = true;
        }
    }

    public static final class Builder {
        @NonNull private final View rootView;
        @Nullable private OnPopupKeyboardShownListener onPopupKeyboardShownListener;
        @Nullable private OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
        @Nullable private OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;
        @Nullable private OnPopupKeyboardDismissListener onPopupKeyboardDismissListener;

        private Builder(@NonNull final View rootView) {
            this.rootView = rootView;
        }

        /**
         * @param rootView The root View of your layout.xml which will be used for calculating the height
         *                 of the keyboard.
         * @return builder For building the {@link PopupKeyboard}.
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

        @CheckResult public PopupKeyboard build(@NonNull View popup) {
            final PopupKeyboard popupKeyboard = new PopupKeyboard(rootView);
            popupKeyboard.setPopupView(popup);
            popupKeyboard.mOnSoftKeyboardCloseListener = onSoftKeyboardCloseListener;
            popupKeyboard.mOnSoftKeyboardOpenListener = onSoftKeyboardOpenListener;
            popupKeyboard.mOnPopupKeyboardShownListener = onPopupKeyboardShownListener;
            popupKeyboard.mOnPopupKeyboardDismissListener = onPopupKeyboardDismissListener;
            return popupKeyboard;
        }
    }
}

