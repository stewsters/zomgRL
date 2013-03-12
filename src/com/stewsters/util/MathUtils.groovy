package com.stewsters.util


class MathUtils {

    private static Random random

    public static synchronized init() {
        if (!random)
            random = new Random()
    }

/**
 * @param low inclusive
 * @param high inclusive
 * @return
 */
    public static int getIntInRange(int low, int high) {
        if (low == high) return low
        if (!random) init()
        return random.nextInt(high + 1 - low) + low
    }


    public static boolean getBoolean() {
        if (!random) init()
        return random.nextBoolean()
    }

    public static int manhattanDistance(int x1, int y1, int x2, int y2) {
        Math.abs(x1 - x2) + Math.abs(y1 - y2)
    }

    public static def rand(ArrayList source) {
        int id = getIntInRange(0, source.size() - 1)
        return source[id]
    }

    public static int limit(int number, int low, int high) {
        return Math.max(low, Math.min(high, number))
    }


    public static double getGauss( double  stdDeviation=1){
        if (!random) init()
        random.nextGaussian() * stdDeviation
    }
}
