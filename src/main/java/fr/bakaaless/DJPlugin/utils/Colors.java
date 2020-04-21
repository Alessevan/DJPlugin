package fr.bakaaless.DJPlugin.utils;

public enum Colors {

    ROUGE(0),
    ORANGE(1),
    JAUNE(2),
    VERT_CLAIR(3),
    CYAN(4),
    BLEU_CLAIR(5),
    BLEU_FONCE(6),
    VIOLET(7),
    SILVER(8);

    private final int value;

    Colors(final int value) {
        this.value = value;
    }

    public static Colors getColorFromValue(final int value) {
        for (final Colors colors : Colors.values()) {
            if (colors.getValue() == value) return colors;
        }
        return Colors.SILVER;
    }

    public static int[] getRGBFromValue(final int value) {
        final int[] rgb = new int[3];
        if (value == 0) {
            rgb[0] = 255;
        } else if (value == 1) {
            rgb[0] = 255;
            rgb[1] = 165;
        } else if (value == 2) {
            rgb[0] = 255;
            rgb[1] = 215;
        } else if (value == 3) {
            rgb[0] = 50;
            rgb[1] = 205;
            rgb[2] = 50;
        } else if (value == 4) {
            rgb[0] = 32;
            rgb[1] = 178;
            rgb[2] = 170;
        } else if (value == 5) {
            rgb[1] = 151;
            rgb[2] = 205;
        } else if (value == 6) {
            rgb[2] = 255;
        } else if (value == 7) {
            rgb[0] = 75;
            rgb[2] = 130;
        } else if (value == 8) {
            rgb[0] = 119;
            rgb[1] = 136;
            rgb[2] = 153;
        }
        return rgb;
    }

    public int getValue() {
        return this.value;
    }
}
