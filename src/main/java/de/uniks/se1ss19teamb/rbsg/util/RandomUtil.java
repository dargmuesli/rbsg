package de.uniks.se1ss19teamb.rbsg.util;

import java.util.Random;

public class RandomUtil {
    protected static Random RANDOM = new Random();

    public static double distributedPosition(double range, double cutOff) {
        return (range - (2 * cutOff)) * RANDOM.nextFloat() + cutOff;
    }

    public static int inRange(int min, int max) {
        return min + RANDOM.nextInt(max - min + 1);
    }
}
