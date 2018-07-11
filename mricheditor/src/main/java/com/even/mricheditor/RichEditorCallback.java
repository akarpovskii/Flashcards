package com.even.mricheditor;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Rich Editor Callback
 * Created by even.wu on 8/8/17.
 */

public class RichEditorCallback {
    private Gson gson = new Gson();
    private FontStyle mFontStyle = new FontStyle();
    private OnGetHtmlListener onGetHtmlListener;

    private static final List<ActionType> FONT_BLOCK_GROUP =
        Arrays.asList(ActionType.NORMAL, ActionType.H1, ActionType.H2, ActionType.H3, ActionType.H4,
            ActionType.H5, ActionType.H6);

    private static final List<ActionType> TEXT_ALIGN_GROUP =
        Arrays.asList(ActionType.JUSTIFY_LEFT, ActionType.JUSTIFY_CENTER, ActionType.JUSTIFY_RIGHT,
            ActionType.JUSTIFY_FULL);

    private static final List<ActionType> LIST_STYLE_GROUP =
        Arrays.asList(ActionType.ORDERED, ActionType.UNORDERED);

    @Nullable private FontStyleChangeListener mFontStyleChangeListener;

    @JavascriptInterface public void returnHtml(String html) {
        if (onGetHtmlListener != null) {
            onGetHtmlListener.getHtml(html);
        }
    }

    @JavascriptInterface public void updateCurrentStyle(String currentStyle) {
        FontStyle fontStyle = gson.fromJson(currentStyle, FontStyle.class);
        if (fontStyle != null) {
            updateStyle(fontStyle);
        }
    }

    private void updateStyle(FontStyle fontStyle) {
        if (mFontStyleChangeListener != null) {
            if (mFontStyle.getFontFamily() == null || !mFontStyle.getFontFamily()
                    .equals(fontStyle.getFontFamily())) {
                if (!TextUtils.isEmpty(fontStyle.getFontFamily())) {
                    String font = fontStyle.getFontFamily().split(",")[0].replace("\"", "");
                    mFontStyleChangeListener.notifyFontStyleChange(ActionType.FAMILY, font);
                }
            }

            if (mFontStyle.getFontForeColor() == null || !mFontStyle.getFontForeColor()
                    .equals(fontStyle.getFontForeColor())) {
                if (!TextUtils.isEmpty(fontStyle.getFontForeColor())) {
                    mFontStyleChangeListener.notifyFontStyleChange(ActionType.FORE_COLOR,
                            fontStyle.getFontForeColor());
                }
            }

            if (mFontStyle.getFontBackColor() == null || !mFontStyle.getFontBackColor()
                    .equals(fontStyle.getFontBackColor())) {
                if (!TextUtils.isEmpty(fontStyle.getFontBackColor())) {
                    mFontStyleChangeListener.notifyFontStyleChange(ActionType.BACK_COLOR,
                            fontStyle.getFontBackColor());
                }
            }

            if (mFontStyle.getFontSize() != fontStyle.getFontSize()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.SIZE,
                            String.valueOf(fontStyle.getFontSize()));
            }

            if (mFontStyle.getTextAlign() != fontStyle.getTextAlign()) {
                for (int i = 0, size = TEXT_ALIGN_GROUP.size(); i < size; i++) {
                    ActionType type = TEXT_ALIGN_GROUP.get(i);
                    mFontStyleChangeListener.notifyFontStyleChange(type,
                                String.valueOf(type == fontStyle.getTextAlign()));
                }
            }

            if (mFontStyle.getLineHeight() != fontStyle.getLineHeight()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.LINE_HEIGHT,
                            String.valueOf(fontStyle.getLineHeight()));
            }

            if (mFontStyle.isBold() != fontStyle.isBold()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.BOLD,
                            String.valueOf(fontStyle.isBold()));
            }

            if (mFontStyle.isItalic() != fontStyle.isItalic()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.ITALIC,
                            String.valueOf(fontStyle.isItalic()));
            }

            if (mFontStyle.isUnderline() != fontStyle.isUnderline()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.UNDERLINE,
                            String.valueOf(fontStyle.isUnderline()));
            }

            if (mFontStyle.isSubscript() != fontStyle.isSubscript()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.SUBSCRIPT,
                            String.valueOf(fontStyle.isSubscript()));
            }

            if (mFontStyle.isSuperscript() != fontStyle.isSuperscript()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.SUPERSCRIPT,
                            String.valueOf(fontStyle.isSuperscript()));
            }

            if (mFontStyle.isStrikethrough() != fontStyle.isStrikethrough()) {
                mFontStyleChangeListener.notifyFontStyleChange(ActionType.STRIKETHROUGH,
                            String.valueOf(fontStyle.isStrikethrough()));
            }

            if (mFontStyle.getFontBlock() != fontStyle.getFontBlock()) {
                for (int i = 0, size = FONT_BLOCK_GROUP.size(); i < size; i++) {
                    ActionType type = FONT_BLOCK_GROUP.get(i);
                    mFontStyleChangeListener.notifyFontStyleChange(type,
                                String.valueOf(type == fontStyle.getFontBlock()));
                }
            }

            if (mFontStyle.getListStyle() != fontStyle.getListStyle()) {
                for (int i = 0, size = LIST_STYLE_GROUP.size(); i < size; i++) {
                    ActionType type = LIST_STYLE_GROUP.get(i);
                    mFontStyleChangeListener.notifyFontStyleChange(type,
                                String.valueOf(type == fontStyle.getListStyle()));
                }
            }
        }

        mFontStyle = fontStyle;
    }

    public interface FontStyleChangeListener {
        void notifyFontStyleChange(ActionType type, String value);
    }

    public void setFontStyleChangeListener(@Nullable FontStyleChangeListener listener) {
        mFontStyleChangeListener = listener;
    }

    interface OnGetHtmlListener {
        void getHtml(String html);
    }

    OnGetHtmlListener getOnGetHtmlListener() {
        return onGetHtmlListener;
    }

    void setOnGetHtmlListener(OnGetHtmlListener onGetHtmlListener) {
        this.onGetHtmlListener = onGetHtmlListener;
    }
}
