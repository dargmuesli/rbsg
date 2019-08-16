package de.uniks.se1ss19teamb.rbsg.sound;

import de.uniks.se1ss19teamb.rbsg.TestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SoundManagerTest {

    @Before
    public void prepareTest() throws ExceptionInInitializerError, InterruptedException {
        TestUtil.initJfx();
    }

    @Test
    public void loadSounds() {
        Assert.assertTrue(SoundManager.getLoaded("panzer"));
        Assert.assertFalse(SoundManager.getLoaded("bgm"));
    }

    @Test
    public void checkLoadedSounds() throws InterruptedException {
        TestUtil.setupHeadlessMode();
        TestUtil.initJfx();

        //Actually plays the sound and hopes for no exception
        SoundManager.playSound("panzer", 0, 0);
        Thread.sleep(1000);
        SoundManager.playSound("panzer", 0, -1);
        Thread.sleep(1000);

        SoundManager.playSound("bgm", 0, 0);
        Thread.sleep(1000);
    }
}
