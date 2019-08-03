package de.uniks.se1ss19teamb.rbsg;

import de.uniks.se1ss19teamb.rbsg.sound.SoundManager;
import de.uniks.se1ss19teamb.rbsg.util.RandomUtil;
import java.util.Random;
import javafx.application.Application;

public class TestUtil {

    public static void initJfx() throws InterruptedException {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                try {
                    Application.launch(Main.class);
                } catch (IllegalStateException e) {
                    // Application launch was already called
                }
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

    public static void setupHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
    }
}
