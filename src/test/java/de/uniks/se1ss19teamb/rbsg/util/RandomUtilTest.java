package de.uniks.se1ss19teamb.rbsg.util;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RandomUtilTest {
    @Before
    public void setupTests() {
        RandomUtil.RANDOM = new Random(123);
    }

    @Test
    public void distributedPositionTest() {
        Assert.assertEquals(50, RandomUtil.distributedPosition(100, 49), 1);
        Assert.assertEquals(28, RandomUtil.distributedPosition(100, 10), 1);
    }

    @Test
    public void inRangeTest() {
        Assert.assertEquals(1, RandomUtil.inRange(0, 1));
        Assert.assertEquals(0, RandomUtil.inRange(0, 1));
        Assert.assertEquals(72, RandomUtil.inRange(0, 100));
    }
}
