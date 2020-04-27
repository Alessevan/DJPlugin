package fr.bakaaless.DJPlugin.utils;

import java.util.Random;

public class MathUtils {

    public static float randomRange(final float n, final float n2) {
        return n + (float) Math.random() * (n2 - n);
    }

    public static int randomRange(final int n, final int n2) {
        return new Random().nextInt(n2 - n + 1) + n;
    }

    public static double randomRange(final double n, final double n2) {
        return (Math.random() < 0.5) ? ((1.0 - Math.random()) * (n2 - n) + n) : (Math.random() * (n2 - n) + n);
    }

}
