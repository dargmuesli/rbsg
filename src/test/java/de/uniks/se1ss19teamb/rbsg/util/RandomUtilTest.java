package de.uniks.se1ss19teamb.rbsg.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class RandomUtilTest {
    @Before
    public void setupTests() {
        RandomUtil.RANDOM = new Random(123);
    }

    @Test
    public void inRangeTest() {
        Assert.assertEquals(98, RandomUtil.inRange(0, 100));
        Assert.assertEquals(19, RandomUtil.inRange(0, 100));
    }
}
