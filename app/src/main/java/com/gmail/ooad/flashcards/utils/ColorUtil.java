package com.gmail.ooad.flashcards.utils;

import android.support.v4.graphics.ColorUtils;

/**
 * A utility class for darkening and lightening colors in the same way as
 * material design color palettes
 * Created by Ammar Mardawi on 12/4/16.
 * https://stackoverflow.com/a/40964456
 */

public class ColorUtil {

    /**
     * Darkens a given color
     * @param base base color
     * @param amount amount between 0 and 100
     * @return darken color
     */
    public static int darken(int base, int amount) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(base, hsl);
        hsl[2] -= amount / 100f;
        if (hsl[2] < 0)
            hsl[2] = 0f;
        return ColorUtils.HSLToColor(hsl);
    }

    /**
     * lightens a given color
     * @param base base color
     * @param amount amount between 0 and 100
     * @return lightened
     */
    public static int lighten(int base, int amount) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(base, hsl);
        hsl[2] += amount / 100f;
        if (hsl[2] > 1)
            hsl[2] = 1f;
        return ColorUtils.HSLToColor(hsl);
    }
}