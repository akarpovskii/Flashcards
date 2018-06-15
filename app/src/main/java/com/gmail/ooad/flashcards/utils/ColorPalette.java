package com.gmail.ooad.flashcards.utils;

/*
 * Created by akarpovskii on 18.05.18.
 */
public enum ColorPalette implements IColorPalette {
    RED         (0xfff44336, 0xffd32f2f, 0xffff5252),
    PINK        (0xffe91e63, 0xffc2185b, 0xffff4081),
    PURPLE      (0xff9c27b0, 0xff7b1fa2, 0xffe040fb),
    DEEP_PURPLE (0xff673ab7, 0xff512da8, 0xff7c4dff),
    INDIGO      (0xff3f51b5, 0xff303f9f, 0xff536dfe),
    BLUE        (0xff2196f3, 0xff1976d2, 0xff448aff),
    LIGHT_BLUE  (0xff0288d1, 0xff03a9f4, 0xff40c4ff),
    CYAN        (0xff00bcd4, 0xff0097a7, 0xff18ffff),
    TEAL        (0xff009688, 0xff00796b, 0xff64ffda),
    GREEN       (0xff4caf50, 0xff388e3c, 0xff69f0ae),
    LIGHT_GREEN (0xff8bc34a, 0xff689f38, 0xffb2ff59),
    LIME        (0xffcddc39, 0xffafb42b, 0xffeeff41),
    YELLOW      (0xffffeb3b, 0xfffbc02d, 0xffffff00),
    AMBER       (0xffffc107, 0xffffa000, 0xffffd740),
    ORANGE      (0xffff9800, 0xfff57c00, 0xffffab40),
    DEEP_ORANGE (0xffff5722, 0xffe64a19, 0xffff6e40);

    private int mPrimary;

    private int mPrimaryDark;

    private int mAccent;

    ColorPalette(int primary, int primaryDark, int accent) {
        mPrimary = primary;
        mPrimaryDark = primaryDark;
        mAccent = accent;
    }

    public String toValue() {
        return this.name();
    }

    static public ColorPalette fromValue(String value) {
        if (value != null) {
            for (ColorPalette palette : ColorPalette.values()) {
                if (palette.toValue().equals(value)) {
                    return palette;
                }
            }
        }

        return ColorPalette.AMBER;
    }

    public int getPrimary() {
        return mPrimary;
    }

    public int getPrimaryDark() {
        return mPrimaryDark;
    }

    public int getAccent() {
        return mAccent;
    }
}
