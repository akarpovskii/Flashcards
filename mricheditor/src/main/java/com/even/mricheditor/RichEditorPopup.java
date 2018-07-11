package com.even.mricheditor;

import android.annotation.SuppressLint;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.even.mricheditor.fragment.EditorMenu;
import com.gmail.ooad.utilities.popupkeyboard.PopupKeyboard;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardDismissListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnPopupKeyboardShownListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardCloseListener;
import com.gmail.ooad.utilities.popupkeyboard.listeners.OnSoftKeyboardOpenListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

@SuppressWarnings({"unused", "WeakerAccess"})
@SuppressLint("SetJavaScriptEnabled")
public class RichEditorPopup extends PopupKeyboard {
    EditorMenu mEditorMenu;

    public RichEditorPopup(@NonNull View rootView, @NonNull RichEditor richEditor) {
        super(rootView);
        initImageLoader();

        mEditorMenu = new EditorMenu(mContext, richEditor);

        setPopupView(mEditorMenu);
    }

    public void setRichEditor(@NonNull RichEditor richEditor) {
        mEditorMenu.setRichEditor(richEditor);
    }

    /**
     * ImageLoader for insert Image
     */
    private void initImageLoader() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(false);
        imagePicker.setSaveRectangle(true);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth(800);
        imagePicker.setFocusHeight(800);
        imagePicker.setOutPutX(256);
        imagePicker.setOutPutY(256);
    }

    public static final class Builder {
        @NonNull private final View mRootView;
        @Nullable
        private OnPopupKeyboardShownListener onPopupKeyboardShownListener;
        @Nullable private OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
        @Nullable private OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;
        @Nullable private OnPopupKeyboardDismissListener onPopupKeyboardDismissListener;

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

        @CheckResult public RichEditorPopup build(@NonNull RichEditor richEditor) {
            final RichEditorPopup popupKeyboard = new RichEditorPopup(mRootView, richEditor);
            popupKeyboard.mOnSoftKeyboardCloseListener = onSoftKeyboardCloseListener;
            popupKeyboard.mOnSoftKeyboardOpenListener = onSoftKeyboardOpenListener;
            popupKeyboard.mOnPopupKeyboardShownListener = onPopupKeyboardShownListener;
            popupKeyboard.mOnPopupKeyboardDismissListener = onPopupKeyboardDismissListener;
            return popupKeyboard;
        }
    }
}
