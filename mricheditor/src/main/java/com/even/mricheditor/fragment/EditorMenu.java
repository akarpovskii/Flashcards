package com.even.mricheditor.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.even.mricheditor.ActionType;
import com.even.mricheditor.R;
import com.even.mricheditor.RichEditor;
import com.even.mricheditor.RichEditorCallback;
import com.even.mricheditor.ui.ActionImageView;
import com.even.mricheditor.util.FileIOUtil;
import com.even.mricheditor.widget.ColorPaletteView;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Editor Menu Fragment
 * Created by even.wu on 8/8/17.
 */

@SuppressWarnings("unused")
@SuppressLint("ViewConstructor")
public class EditorMenu extends LinearLayout {
    private TextView tvFontSize;
    private TextView tvFontName;
    private TextView tvFontSpacing;
    private ColorPaletteView cpvFontTextColor;
    private ColorPaletteView cpvHighlightColor;
    private LinearLayout llActionBarContainer;

    private static final int REQUEST_PICK_IMAGE = 137;  // truly random number

    private AppCompatActivity mContext;

    private RichEditor mRichEditor;

    private Map<Integer, ActionType> mViewTypeMap = new HashMap<Integer, ActionType>() {
        {
            put(R.id.iv_action_bold, ActionType.BOLD);
            put(R.id.iv_action_italic, ActionType.ITALIC);
            put(R.id.iv_action_underline, ActionType.UNDERLINE);
            put(R.id.iv_action_strikethrough, ActionType.STRIKETHROUGH);
            put(R.id.iv_action_justify_left, ActionType.JUSTIFY_LEFT);
            put(R.id.iv_action_justify_center, ActionType.JUSTIFY_CENTER);
            put(R.id.iv_action_justify_right, ActionType.JUSTIFY_RIGHT);
            put(R.id.iv_action_justify_full, ActionType.JUSTIFY_FULL);
            put(R.id.iv_action_subscript, ActionType.SUBSCRIPT);
            put(R.id.iv_action_superscript, ActionType.SUPERSCRIPT);
            put(R.id.iv_action_insert_numbers, ActionType.ORDERED);
            put(R.id.iv_action_insert_bullets, ActionType.UNORDERED);
            put(R.id.iv_action_indent, ActionType.INDENT);
            put(R.id.iv_action_outdent, ActionType.OUTDENT);
            put(R.id.iv_action_code_view, ActionType.CODE_VIEW);
            put(R.id.iv_action_blockquote, ActionType.BLOCK_QUOTE);
            put(R.id.iv_action_code_block, ActionType.BLOCK_CODE);
            put(R.id.ll_normal, ActionType.NORMAL);
            put(R.id.ll_h1, ActionType.H1);
            put(R.id.ll_h2, ActionType.H2);
            put(R.id.ll_h3, ActionType.H3);
            put(R.id.ll_h4, ActionType.H4);
            put(R.id.ll_h5, ActionType.H5);
            put(R.id.ll_h6, ActionType.H6);
            put(R.id.iv_action_insert_image, ActionType.IMAGE);
            put(R.id.iv_action_insert_link, ActionType.LINK);
            put(R.id.iv_action_table, ActionType.TABLE);
            put(R.id.iv_action_line, ActionType.LINE);
        }
    };

    private static final List<ActionType> ACTION_TYPE_LIST =
            Arrays.asList(ActionType.BOLD, ActionType.ITALIC, ActionType.UNDERLINE,
                    ActionType.STRIKETHROUGH, ActionType.SUBSCRIPT, ActionType.SUPERSCRIPT,
                    ActionType.NORMAL, ActionType.H1, ActionType.H2, ActionType.H3, ActionType.H4,
                    ActionType.H5, ActionType.H6, ActionType.INDENT, ActionType.OUTDENT,
                    ActionType.JUSTIFY_LEFT, ActionType.JUSTIFY_CENTER, ActionType.JUSTIFY_RIGHT,
                    ActionType.JUSTIFY_FULL, ActionType.ORDERED, ActionType.UNORDERED, ActionType.LINE,
                    ActionType.BLOCK_CODE, ActionType.BLOCK_QUOTE, ActionType.CODE_VIEW);

    private static final List<Integer> ACTION_TYPE_ICON_LIST =
            Arrays.asList(R.drawable.ic_format_bold, R.drawable.ic_format_italic,
                    R.drawable.ic_format_underlined, R.drawable.ic_format_strikethrough,
                    R.drawable.ic_format_subscript, R.drawable.ic_format_superscript,
                    R.drawable.ic_format_para, R.drawable.ic_format_h1, R.drawable.ic_format_h2,
                    R.drawable.ic_format_h3, R.drawable.ic_format_h4, R.drawable.ic_format_h5,
                    R.drawable.ic_format_h6, R.drawable.ic_format_indent_decrease,
                    R.drawable.ic_format_indent_increase, R.drawable.ic_format_align_left,
                    R.drawable.ic_format_align_center, R.drawable.ic_format_align_right,
                    R.drawable.ic_format_align_justify, R.drawable.ic_format_list_numbered,
                    R.drawable.ic_format_list_bulleted, R.drawable.ic_line,
                    R.drawable.ic_code_block, R.drawable.ic_format_quote, R.drawable.ic_code_review);

    private FontStyleChangeCallback mFontStyleChangeCallback;

    public EditorMenu(final AppCompatActivity context, @NonNull RichEditor richEditor) {
        super(context);
        mContext = context;

        View view = View.inflate(context, R.layout.fragment_editor_menu, this);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        tvFontSize = findViewById(R.id.tv_font_size);
        tvFontName = findViewById(R.id.tv_font_name);
        tvFontSpacing = findViewById(R.id.tv_font_spacing);
        cpvFontTextColor = findViewById(R.id.cpv_font_text_color);
        cpvHighlightColor = findViewById(R.id.cpv_highlight_color);
        llActionBarContainer = findViewById(R.id.ll_action_bar_container);

        mFontStyleChangeCallback = new FontStyleChangeCallback();
        setRichEditor(richEditor);

        initView();
    }

    public void setRichEditor(@NonNull RichEditor richEditor) {
        if (mRichEditor != null) {
            mRichEditor.removeRichEditorCallback();
        }
        mRichEditor = richEditor;
        mRichEditor.setRichEditorCallback(mFontStyleChangeCallback);

        for (int i = 0, size = llActionBarContainer.getChildCount(); i < size; i++) {
            View view  = llActionBarContainer.getChildAt(i);
            if (view instanceof ActionImageView) {
                ((ActionImageView) view).setRichEditorAction(mRichEditor);
            }
        }

//        mRichEditor.refreshHtml();
    }

    private void initView() {
        cpvFontTextColor.setOnColorChangeListener(color ->
                onActionPerform(ActionType.FORE_COLOR, color));

        cpvHighlightColor.setOnColorChangeListener(color ->
                onActionPerform(ActionType.BACK_COLOR, color));

        findViewById(R.id.ll_font_size).setOnClickListener(v ->
                openFontSettingFragment(FontSettingDialogFragment.TYPE_SIZE));

        findViewById(R.id.ll_line_height).setOnClickListener(v ->
                openFontSettingFragment(FontSettingDialogFragment.TYPE_LINE_HEIGHT));

        findViewById(R.id.tv_font_name).setOnClickListener(v ->
                openFontSettingFragment(FontSettingDialogFragment.TYPE_FONT_FAMILY));

        for (Map.Entry<Integer, ActionType> e : mViewTypeMap.entrySet()) {
            findViewById(e.getKey()).setOnClickListener(v -> onActionPerform(e.getValue()));
        }

        // init action bar
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                mContext.getResources().getDisplayMetrics());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9,
                mContext.getResources().getDisplayMetrics());

        for (int i = 0, size = ACTION_TYPE_LIST.size(); i < size; i++) {
            final ActionImageView actionImageView = new ActionImageView(mContext);
            //noinspection SuspiciousNameCombination
            actionImageView.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            actionImageView.setPadding(padding, padding, padding, padding);
            actionImageView.setActionType(ACTION_TYPE_LIST.get(i));
            actionImageView.setTag(ACTION_TYPE_LIST.get(i));
            actionImageView.setActivatedColor(R.color.colorAccent);
            actionImageView.setDeactivatedColor(R.color.tintColor);
            actionImageView.setRichEditorAction(mRichEditor);
            actionImageView.setBackgroundResource(R.drawable.btn_colored_material);
            actionImageView.setImageResource(ACTION_TYPE_ICON_LIST.get(i));
            actionImageView.setOnClickListener(v -> actionImageView.command());
            llActionBarContainer.addView(actionImageView);
        }
    }

    private void openFontSettingFragment(final int type) {
        FontSettingDialogFragment dialog = FontSettingDialogFragment.NewInstance(type);

        dialog.setOnResultListener(result -> {

            switch (type) {
                case FontSettingDialogFragment.TYPE_SIZE:
                    tvFontSize.setText(result);
                    onActionPerform(ActionType.SIZE, result);
                    break;
                case FontSettingDialogFragment.TYPE_LINE_HEIGHT:
                    tvFontSpacing.setText(result);
                    onActionPerform(ActionType.LINE_HEIGHT, result);
                    break;
                case FontSettingDialogFragment.TYPE_FONT_FAMILY:
                    tvFontName.setText(result);
                    onActionPerform(ActionType.FAMILY, result);
                    break;
                default:
                    break;
            }
        });

        dialog.show(mContext.getSupportFragmentManager(), "fontSetting");
    }

    private void updateActionStates(final ActionType type, final boolean isActive) {
        post(() -> {
            View view = null;
            for (Map.Entry<Integer, ActionType> e : mViewTypeMap.entrySet()) {
                Integer key = e.getKey();
                if (e.getValue() == type) {
                    view = findViewById(key);
                    break;
                }
            }

            if (view == null) {
                return;
            }

            switch (type) {
                case BOLD:
                case ITALIC:
                case UNDERLINE:
                case SUBSCRIPT:
                case SUPERSCRIPT:
                case STRIKETHROUGH:
                case JUSTIFY_LEFT:
                case JUSTIFY_CENTER:
                case JUSTIFY_RIGHT:
                case JUSTIFY_FULL:
                case ORDERED:
                case CODE_VIEW:
                case UNORDERED:
                    if (isActive) {
                        ((ImageView) view).setColorFilter(
                            ContextCompat.getColor(mContext, R.color.colorAccent));
                    } else {
                        ((ImageView) view).setColorFilter(
                            ContextCompat.getColor(mContext, R.color.tintColor));
                    }
                    break;
                case NORMAL:
                case H1:
                case H2:
                case H3:
                case H4:
                case H5:
                case H6:
                    if (isActive) {
                        view.setBackgroundResource(R.drawable.round_rectangle_blue);
                    } else {
                        view.setBackgroundResource(R.drawable.round_rectangle_white);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void updateActionStates(ActionType type, final String value) {
        switch (type) {
            case FAMILY:
                updateFontFamilyStates(value);
                break;
            case SIZE:
                updateFontStates(ActionType.SIZE, Double.valueOf(value));
                break;
            case FORE_COLOR:
            case BACK_COLOR:
                updateFontColorStates(type, value);
                break;
            case LINE_HEIGHT:
                updateFontStates(ActionType.LINE_HEIGHT, Double.valueOf(value));
                break;
            case BOLD:
            case ITALIC:
            case UNDERLINE:
            case SUBSCRIPT:
            case SUPERSCRIPT:
            case STRIKETHROUGH:
            case JUSTIFY_LEFT:
            case JUSTIFY_CENTER:
            case JUSTIFY_RIGHT:
            case JUSTIFY_FULL:
            case NORMAL:
            case H1:
            case H2:
            case H3:
            case H4:
            case H5:
            case H6:
            case ORDERED:
            case UNORDERED:
                updateActionStates(type, Boolean.valueOf(value));
                break;
            default:
                break;
        }
    }

    private void updateFontFamilyStates(final String font) {
        post(() -> tvFontName.setText(font));
    }

    private void updateFontStates(final ActionType type, final double value) {
        post(() -> {
            switch (type) {
                case SIZE:
                    tvFontSize.setText(String.valueOf((int) value));
                    break;
                case LINE_HEIGHT:
                    tvFontSpacing.setText(String.valueOf(value));
                    break;
                default:
                    break;
            }
        });
    }

    private void updateFontColorStates(final ActionType type, final String color) {
        post(() -> {
            String selectedColor = rgbToHex(color);
            if (selectedColor != null) {
                if (type == ActionType.FORE_COLOR) {
                    cpvFontTextColor.setSelectedColor(selectedColor);
                } else if (type == ActionType.BACK_COLOR) {
                    cpvHighlightColor.setSelectedColor(selectedColor);
                }
            }
        });
    }

    private static String rgbToHex(String rgb) {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(rgb);
        if (m.matches()) {
            return String.format("#%02x%02x%02x", Integer.valueOf(m.group(1)),
                Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3)));
        }
        return null;
    }

    // FIXME: add to action bar?
    public void undo() {
        mRichEditor.undo();
    }

    public void redo() {
        mRichEditor.redo();
    }

    public void insertImage() {
        Intent intent = new Intent(mContext, ImageGridActivity.class);
        mContext.startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    public void insertImage(String name, String path) {
        mRichEditor.insertImageData(name, encodeFileToBase64Binary(path));
    }

    // FIXME: move to activity
    /*
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS
            && data != null
            && requestCode == REQUEST_PICK_IMAGE) {
            @SuppressWarnings("unchecked")
            ArrayList<ImageItem> images =
                (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null && !images.isEmpty()) {

                //1.Insert the Base64 String (Base64.NO_WRAP)
                ImageItem imageItem = images.get(0);
                mRichEditor.insertImageData(imageItem.name,
                    encodeFileToBase64Binary(imageItem.path));

                //2.Insert the ImageUrl
                //mRichEditor.insertImageUrl(
                //    "https://avatars0.githubusercontent.com/u/5581118?v=4&u=b7ea903e397678b3675e2a15b0b6d0944f6f129e&s=400");
            }
        }
    }
    */

    private static String encodeFileToBase64Binary(String filePath) {
        byte[] bytes = FileIOUtil.readFile2BytesByStream(filePath);
        byte[] encoded = Base64.encode(bytes, Base64.NO_WRAP);
        return new String(encoded);
    }

    public void insertLink() {
        EditHyperlinkDialogFragment dialog = new EditHyperlinkDialogFragment();
        dialog.setOnHyperlinkListener((address, text) -> mRichEditor.createLink(text, address));

        dialog.show(mContext.getSupportFragmentManager(), "editHyperlink");
    }

    public void insertTable() {
        EditTableDialogFragment dialog = new EditTableDialogFragment();
        dialog.setOnTableListener((rows, cols) -> mRichEditor.insertTable(rows, cols));

        dialog.show(mContext.getSupportFragmentManager(), "editTable");
    }

    public void onActionPerform(ActionType type, Object... values) {
        if (mRichEditor == null) {
            return;
        }

        String value = null;
        if (values != null && values.length > 0) {
            value = (String) values[0];
        }

        switch (type) {
            case SIZE:
                mRichEditor.fontSize(Double.valueOf(value));
                break;
            case LINE_HEIGHT:
                mRichEditor.lineHeight(Double.valueOf(value));
                break;
            case FORE_COLOR:
                mRichEditor.foreColor(value);
                break;
            case BACK_COLOR:
                mRichEditor.backColor(value);
                break;
            case FAMILY:
                mRichEditor.fontName(value);
                break;
            case IMAGE:
                insertImage();
                break;
            case LINK:
                insertLink();
                break;
            case TABLE:
                insertTable();
                break;
            case BOLD:
            case ITALIC:
            case UNDERLINE:
            case SUBSCRIPT:
            case SUPERSCRIPT:
            case STRIKETHROUGH:
            case JUSTIFY_LEFT:
            case JUSTIFY_CENTER:
            case JUSTIFY_RIGHT:
            case JUSTIFY_FULL:
            case CODE_VIEW:
            case ORDERED:
            case UNORDERED:
            case INDENT:
            case OUTDENT:
            case BLOCK_QUOTE:
            case BLOCK_CODE:
            case NORMAL:
            case H1:
            case H2:
            case H3:
            case H4:
            case H5:
            case H6:
            case LINE:
                ActionImageView actionImageView = llActionBarContainer.findViewWithTag(type);
                if (actionImageView != null) {
                    actionImageView.performClick();
                }
                break;
            default:
                break;
        }
    }

    class FontStyleChangeCallback implements RichEditorCallback.FontStyleChangeListener {

        @Override public void notifyFontStyleChange(ActionType type, final String value) {
            ActionImageView actionImageView = llActionBarContainer.findViewWithTag(type);
            if (actionImageView != null) {
                actionImageView.notifyFontStyleChange(type, value);
            }

            updateActionStates(type, value);
        }
    }
}
