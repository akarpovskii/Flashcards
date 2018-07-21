package com.even.mricheditor;

import android.annotation.SuppressLint;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.even.mricheditor.interfaces.OnInsertImageClickListener;
import com.even.mricheditor.views.EditorMenu;
import com.gmail.ooad.utilities.popupkeyboard.PopupKeyboard;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardDismissListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardShownListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardCloseListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardOpenListener;

@SuppressWarnings({"unused", "WeakerAccess"})
@SuppressLint("SetJavaScriptEnabled")
public class RichEditorPopup extends PopupKeyboard {
    EditorMenu mEditorMenu;

    public RichEditorPopup(@NonNull View rootView, @NonNull RichEditor richEditor) {
        super(rootView);

        mEditorMenu = new EditorMenu(mContext, richEditor);

        setPopupView(mEditorMenu);
    }

    public void setRichEditor(@NonNull RichEditor richEditor) {
        mEditorMenu.setRichEditor(richEditor);
    }

    public void setOnInsertImageClick(@Nullable OnInsertImageClickListener listener) {
        mEditorMenu.setOnInsertImageClick(listener);
    }

    public void insertImageUrl(String path) {
        mEditorMenu.insertImageUrl(path);
    }

    public void insertImageData(String name, String path) {
        mEditorMenu.insertImageData(name, path);
    }

    public static final class Builder {
        @NonNull private final View mRootView;
        @Nullable private OnPopupKeyboardShownListener onPopupKeyboardShownListener;
        @Nullable private OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
        @Nullable private OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;
        @Nullable private OnPopupKeyboardDismissListener onPopupKeyboardDismissListener;
        @Nullable private OnInsertImageClickListener mOnInsertImageClickListener;

        private Builder(@NonNull final View rootView) {
            mRootView = rootView;
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

        @CheckResult public Builder setOnInsertImageClick(@Nullable OnInsertImageClickListener listener) {
            mOnInsertImageClickListener = listener;
            return this;
        }

        @CheckResult public RichEditorPopup build(@NonNull RichEditor richEditor) {
            final RichEditorPopup popupKeyboard = new RichEditorPopup(mRootView, richEditor);
            popupKeyboard.mOnSoftKeyboardCloseListener = onSoftKeyboardCloseListener;
            popupKeyboard.mOnSoftKeyboardOpenListener = onSoftKeyboardOpenListener;
            popupKeyboard.mOnPopupKeyboardShownListener = onPopupKeyboardShownListener;
            popupKeyboard.mOnPopupKeyboardDismissListener = onPopupKeyboardDismissListener;
            popupKeyboard.setOnInsertImageClick(mOnInsertImageClickListener);
            return popupKeyboard;
        }
    }
}
