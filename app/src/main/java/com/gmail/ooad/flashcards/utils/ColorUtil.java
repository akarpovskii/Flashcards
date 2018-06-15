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
}