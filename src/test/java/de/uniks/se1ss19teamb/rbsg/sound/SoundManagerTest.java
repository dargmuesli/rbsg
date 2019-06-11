package de.uniks.se1ss19teamb.rbsg.sound;

import de.uniks.se1ss19teamb.rbsg.Main;

import javafx.application.Application;

import org.junit.Before;
import org.junit.Test;

public class SoundManagerTest {
    
    @Before
    public void prepareTest() throws ExceptionInInitializerError, InterruptedException {
        //Sound Manager needs JFX Intitialized
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(Main.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
        Thread.sleep(500);
        
        SoundManager.init();
    }
    
    @Test
    public void checkLoadedSounds() throws InterruptedException {        
        //Actually plays the sound and hopes for no exception
        SoundManager.playSound("panzer", 0);
        Thread.sleep(1000);
        SoundManager.playSound("panzer", -1);
        Thread.sleep(1000);
        
        SoundManager.playSound("bgm", 0);
        Thread.sleep(1000);
    }
}
