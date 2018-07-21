package com.gmail.ooad.flashcards.utils;

import android.graphics.Color;

/**
 * A utility class for darkening and lightening colors in the same way as
 * material design color palettes
 * Created by Ammar Mardawi on 12/4/16.
 * https://stackoverflow.com/a/40964456
 */

public class ColorUtil {
    static public ColorPalette[] GetAllPallettes() {
        return new ColorPalette[] {
                ColorPalette.RED,
                ColorPalette.PINK,
                ColorPalette.PURPLE,
                ColorPalette.DEEP_PURPLE,
                ColorPalette.INDIGO,
                ColorPalette.BLUE,
                ColorPalette.LIGHT_BLUE,
                ColorPalette.CYAN,
                ColorPalette.TEAL,
                ColorPalette.GREEN,
                ColorPalette.LIGHT_GREEN,
                ColorPalette.LIME,
                ColorPalette.YELLOW,
                ColorPalette.AMBER,
                ColorPalette.ORANGE,
                ColorPalette.DEEP_ORANGE
        };
    }

    static public int Alpha(int color) {
        return (color >> 24) & 0xff;
    }

    static public int Red(int color) {
        return (color >> 16) & 0xff;
    }

    static public int Green(int color) {
        return (color >>  8) & 0xff;
    }

    static public int Blue(int color) {
        return (color      ) & 0xff;
    }

    static public ColorPalette GetNearest(int color) {
        final ColorPalette[] Palettes = GetAllPallettes();
        ColorPalette nearest = ColorPalette.RED;
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);

        float min = 360.f;

        for (ColorPalette palette:
                Palettes){

            float paletteHSV[] = new float[3];
            Color.colorToHSV(palette.getPrimary(), paletteHSV);

            float diff = hsv[0] - paletteHSV[0];
            float distance = Math.min(Math.abs(diff), Math.abs(360 - diff));

            if (distance < min) {
                min = distance;
                nearest = palette;
            }
        }

        return nearest;
    }

    static public int SetAlpha(int color, int alpha) {
        color &= 0x00ffffff;
        color |= (alpha << 24);
        return color;
    }

    static public int Interpolate(int color, float alpha) {
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        int red   = (int) (ColorUtil.Red(color)   * alpha + 0xff * (1 - alpha));
        int green = (int) (ColorUtil.Green(color) * alpha + 0xff * (1 - alpha));
        int blue  = (int) (ColorUtil.Blue(color)  * alpha + 0xff * (1 - alpha));
        return Color.rgb(red, green, blue);
    }

    static public float Deinterpolate(int baseColor, int interpolated) {
        float alphaR = (float) (ColorUtil.Red(interpolated) - 0xff) / (ColorUtil.Red(baseColor) - 0xff);
        float alphaG = (float) (ColorUtil.Green(interpolated) - 0xff) / (ColorUtil.Green(baseColor) - 0xff);
        float alphaB = (float) (ColorUtil.Blue(interpolated) - 0xff) / (ColorUtil.Blue(baseColor) - 0xff);

        return (float) Math.sqrt((alphaR*alphaR + alphaG*alphaG + alphaB*alphaB) / 3);
    }
}