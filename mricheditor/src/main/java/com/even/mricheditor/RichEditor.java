package com.even.mricheditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Rich Editor Action
 * Created by even.wu on 8/8/17.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class RichEditor extends WebView {
    private RichEditorCallback mRichEditorCallback;

    private volatile String mHtml = null;

    private volatile boolean mHtmlGot = false;

    private final Queue<String> mEvalQueue = new LinkedList<>();

    private boolean mLoaded;

    public RichEditor(Context context) {
        super(context);
        init();
    }

    public RichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        setWebChromeClient(new CustomWebChromeClient());
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);

        mRichEditorCallback = new RichEditorCallback();

        addJavascriptInterface(mRichEditorCallback, "MRichEditor");
        loadUrl("file:///android_asset/richEditor.html");

        setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                synchronized (mEvalQueue) {
                    for (String js : mEvalQueue) {
                        evaluateJavascript(js, null);
                    }
                    mEvalQueue.clear();

                    mLoaded = true;
                }
            }
        });
    }

    private class CustomWebChromeClient extends WebChromeClient {

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
//                insertText("Hello World");
            }
        }

        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    private void evaluateCommand(String script) {
        synchronized (mEvalQueue) {
            if (mLoaded) {
                super.evaluateJavascript(script, null);
            } else {
                mEvalQueue.add(script);
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        evaluateCommand("javascript:updateCurrentStyle()");
    }

    public void removeRichEditorCallback() {
        mRichEditorCallback.setFontStyleChangeListener(null);
    }

    public void setRichEditorCallback(RichEditorCallback.FontStyleChangeListener callback) {
        mRichEditorCallback.setFontStyleChangeListener(callback);
    }

    public void undo() {
        evaluateCommand("javascript:undo()");
    }

    public void redo() {
        evaluateCommand("javascript:redo()");
    }

    public void focus() {
        evaluateCommand("javascript:focus()");
    }

    public void disable() {
        evaluateCommand("javascript:disable()");
    }

    public void enable() {
        evaluateCommand("javascript:enable()");
    }

    /******************** Font ********************/
    public void bold() {
        evaluateCommand("javascript:bold()");
    }

    public void italic() {
        evaluateCommand("javascript:italic()");
    }

    public void underline() {
        evaluateCommand("javascript:underline()");
    }

    public void strikethrough() {
        evaluateCommand("javascript:strikethrough()");
    }

    public void superscript() {
        evaluateCommand("javascript:superscript()");
    }

    public void subscript() {
        evaluateCommand("javascript:subscript()");
    }

    public void backColor(String color) {
        evaluateCommand("javascript:backColor('" + color + "')");
    }

    public void foreColor(String color) {
        evaluateCommand("javascript:foreColor('" + color + "')");
    }

    public void fontName(String fontName) {
        evaluateCommand("javascript:fontName('" + fontName + "')");
    }

    public void fontSize(double fontSize) {
        evaluateCommand("javascript:fontSize(" + fontSize + ")");
    }

    /******************** Paragraph ********************/
    public void justifyLeft() {
        evaluateCommand("javascript:justifyLeft()");
    }

    public void justifyRight() {
        evaluateCommand("javascript:justifyRight()");
    }

    public void justifyCenter() {
        evaluateCommand("javascript:justifyCenter()");
    }

    public void justifyFull() {
        evaluateCommand("javascript:justifyFull()");
    }

    public void insertOrderedList() {
        evaluateCommand("javascript:insertOrderedList()");
    }

    public void insertUnorderedList() {
        evaluateCommand("javascript:insertUnorderedList()");
    }

    public void indent() {
        evaluateCommand("javascript:indent()");
    }

    public void outdent() {
        evaluateCommand("javascript:outdent()");
    }

    public void formatPara() {
        evaluateCommand("javascript:formatPara()");
    }

    public void formatH1() {
        evaluateCommand("javascript:formatH1()");
    }

    public void formatH2() {
        evaluateCommand("javascript:formatH2()");
    }

    public void formatH3() {
        evaluateCommand("javascript:formatH3()");
    }

    public void formatH4() {
        evaluateCommand("javascript:formatH4()");
    }

    public void formatH5() {
        evaluateCommand("javascript:formatH5()");
    }

    public void formatH6() {
        evaluateCommand("javascript:formatH6()");
    }

    public void lineHeight(double lineHeight) {
        evaluateCommand("javascript:lineHeight(" + lineHeight + ")");
    }

    public void insertImageUrl(String imageUrl) {
        evaluateCommand("javascript:insertImageUrl('" + imageUrl + "')");
    }

    public void insertImageData(String fileName, String base64Str) {
        String imageUrl = "data:image/" + fileName.split("\\.")[1] + ";base64," + base64Str;
        evaluateCommand("javascript:insertImageUrl('" + imageUrl + "')");
    }

    public void insertText(String text) {
        evaluateCommand("javascript:insertText('" + text + "')");
    }

    public void createLink(String linkText, String linkUrl) {
        evaluateCommand("javascript:createLink('" + linkText + "','" + linkUrl + "')");
    }

    public void unlink() {
        evaluateCommand("javascript:unlink()");
    }

    public void codeView() {
        evaluateCommand("javascript:codeView()");
    }

    public void insertTable(int colCount, int rowCount) {
        evaluateCommand("javascript:insertTable('" + colCount + "x" + rowCount + "')");
    }

    public void insertHorizontalRule() {
        evaluateCommand("javascript:insertHorizontalRule()");
    }

    public void formatBlockquote() {
        evaluateCommand("javascript:formatBlock('blockquote')");
    }

    public void formatBlockCode() {
        evaluateCommand("javascript:formatBlock('pre')");
    }

    public void insertHtml(String html) {
        evaluateCommand("javascript:pasteHTML('" + html + "')");
    }

    public void empty() {
        evaluateCommand("javascript:empty()");
    }

    private void refreshHtml(@NonNull RichEditorCallback.OnGetHtmlListener onGetHtmlListener) {
        mRichEditorCallback.setOnGetHtmlListener(onGetHtmlListener);
        evaluateCommand("javascript:refreshHTML()");
    }

    public void refreshHtml() {
        refreshHtml(onGetHtmlListener);
    }

    private RichEditorCallback.OnGetHtmlListener onGetHtmlListener =
            html -> {
                mHtml = html;
                mHtmlGot = true;
            };

    public String getHtml() {

        refreshHtml(onGetHtmlListener);
        String text;
        try {
            while (!mHtmlGot) {
                Thread.sleep(50);   // We have to wait callback
            }
            text = mHtml;
        } catch (Exception e) {
            text = "Unable to get html";
        } finally {
            mHtml = null;
            mHtmlGot = false;
        }

        return text;

    }
}