package com.gmail.ooad.flashcards.utils;

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

    static public ColorPalette GetNearest(int color) {
        final ColorPalette[] Palettes = GetAllPallettes();
        ColorPalette nearest = ColorPalette.RED;
        int min = 255*255 * 3;
        for (ColorPalette palette:
                Palettes){
            int r = (color >> 16) & 0xff;
            int g = (color >>  8) & 0xff;
            int b = (color      ) & 0xff;

            int  paletteColor = palette.getPrimary();
            int paletteR = (paletteColor >> 16) & 0xff;
            int paletteG = (paletteColor >>  8) & 0xff;
            int paletteB = (paletteColor      ) & 0xff;

            int distance = (paletteR - r) * (paletteR - r) +
                    (paletteG - g) * (paletteG - g) +
                    (paletteB - b) * (paletteB - b);

            if (distance < min) {
                min = distance;
                nearest = palette;
            }
        }

        return nearest;
    }
}