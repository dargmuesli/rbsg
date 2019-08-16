package de.uniks.se1ss19teamb.rbsg.sound;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class SoundManagerTest {

    @Test
    void loadSounds() {
        Assert.assertTrue(SoundManager.getLoaded("panzer"));
        Assert.assertFalse(SoundManager.getLoaded("bgm"));
    }

    @Test
    void checkLoadedSounds() throws InterruptedException {
        //Actually plays the sound and hopes for no exception
        SoundManager.playSound("panzer", 0, 0);
        Thread.sleep(1000);
        SoundManager.playSound("panzer", 0, -1);
        Thread.sleep(1000);

        SoundManager.playSound("bgm", 0, 0);
        Thread.sleep(1000);
    }
}
