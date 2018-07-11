package com.gmail.ooad.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.PopupWindow;

/*
 * Created by akarpovskii on 06.07.18.
 */
public class Utilities {
    private static final int DO_NOT_UPDATE_FLAG = -1;

    public static AppCompatActivity asActivity(@NonNull final Context context) {
        Context result = context;

        while (result instanceof ContextWrapper) {
            if (result instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }

            result = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalArgumentException("The passed Context is not an Activity.");
    }

    public static int screenHeight(@NonNull final Activity context) {
        final Point size = new Point();

        context.getWindowManager().getDefaultDisplay().getSize(size);

        return size.y;
    }

    @NonNull
    public static Rect windowVisibleDisplayFrame(@NonNull final Activity context) {
        final Rect result = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(result);
        return result;
    }

    @NonNull
    private static Point locationOnScreen(@NonNull final View view) {
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    public static void fixPopupLocation(@NonNull final PopupWindow popupWindow, @NonNull final Point desiredLocation) {
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

                popupWindow.update(fixedOffsetX, fixedOffsetY, DO_NOT_UPDATE_FLAG, DO_NOT_UPDATE_FLAG);
            }
        });
    }
}
