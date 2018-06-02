package com.gmail.ooad.symbolskeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

final class Utils {
    private static final int DONT_UPDATE_FLAG = -1;

    static void removeOnGlobalLayoutListener(final View v, final ViewTreeObserver.OnGlobalLayoutListener listener) {
        v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    @NonNull static <T> T checkNotNull(@Nullable final T reference, final String message) {
        if (reference == null) {
            throw new IllegalArgumentException(message);
        }

        return reference;
    }

    static int dpToPx(@NonNull final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    static int screenHeight(@NonNull final Activity context) {
        final Point size = new Point();

        context.getWindowManager().getDefaultDisplay().getSize(size);

        return size.y;
    }

    @NonNull
    private static Point locationOnScreen(@NonNull final View view) {
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    @NonNull static Rect windowVisibleDisplayFrame(@NonNull final Activity context) {
        final Rect result = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(result);
        return result;
    }

    static Activity asActivity(@NonNull final Context context) {
        Context result = context;

        while (result instanceof ContextWrapper) {
            if (result instanceof Activity) {
                return (Activity) context;
            }

            result = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalArgumentException("The passed Context is not an Activity.");
    }

    static void fixPopupLocation(@NonNull final PopupWindow popupWindow, @NonNull final Point desiredLocation) {
        popupWindow.getContentView().post(() -> {
            final Point actualLocation = locationOnScreen(popupWindow.getContentView());

            if (!(actualLocation.x == desiredLocation.x && actualLocation.y == desiredLocation.y)) {
                final int differenceX = actualLocation.x - desiredLocation.x;
                final int differenceY = actualLocation.y - desiredLocation.y;

                final int fixedOffsetX;
                final int fixedOffsetY;

                if (actualLocation.x > desiredLocation.x) {
                    fixedOffsetX = desiredLocation.x - differenceX;
                } else {
                    fixedOffsetX = desiredLocation.x + differenceX;
                }

                if (actualLocation.y > desiredLocation.y) {
                    fixedOffsetY = desiredLocation.y - differenceY;
                } else {
                    fixedOffsetY = desiredLocation.y + differenceY;
                }

                popupWindow.update(fixedOffsetX, fixedOffsetY, DONT_UPDATE_FLAG, DONT_UPDATE_FLAG);
            }
        });
    }

    private Utils() {
        throw new AssertionError("No instances.");
    }
}
