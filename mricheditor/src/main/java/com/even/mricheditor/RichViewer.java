package com.even.mricheditor;

import android.content.Context;
import android.util.AttributeSet;

/*
 * Created by akarpovskii on 11.07.18.
 */
public class RichViewer extends RichEditor {
    private int maxLeft;
    private int maxRight;
    private int maxTop;
    private int maxBottom;

    public RichViewer(Context context) {
        super(context);
        init();
    }

    public RichViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RichViewer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        disable();
    }
}
