package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.util.RandomUtil;
import java.util.Random;

public class TestUtil {

    public static void initRandom() {
        RandomUtil.RANDOM = new Random(123);
    }
}
