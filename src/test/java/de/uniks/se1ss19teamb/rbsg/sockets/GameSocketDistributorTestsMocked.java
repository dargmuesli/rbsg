package de.uniks.se1ss19teamb.rbsg.sockets;

import org.junit.Assert;
import org.junit.Test;

public class GameSocketDistributorTestsMocked {

    @Test
    public void gameSocketDistributorTest() {
        GameSocket testGameSocketOne = GameSocketDistributor.getGameSocket(2);
        Assert.assertNull(testGameSocketOne);
        GameSocketDistributor.setGameSocket(0, "123456789");
        GameSocket testGameSocketTwo = GameSocketDistributor.getGameSocket(0);
        Assert.assertNotNull(testGameSocketTwo);
    }
}
