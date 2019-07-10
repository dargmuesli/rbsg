package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.util.RandomUtil;
import java.util.Random;
import javafx.application.Application;

public class TestUtil {
    public static void initJfx() throws InterruptedException {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(Main.class);
            }
        };
        t.setDaemon(true);
        t.start();
        Thread.sleep(500);

        SoundManager.init();
    }

    public static void initRandom() {
        RandomUtil.RANDOM = new Random(123);
    }
}
