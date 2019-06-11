package de.uniks.se1ss19teamb.rbsg.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    
    protected boolean keepInRam;
    
    protected Object sound;
    
    protected Sound(String classPath, boolean keepInRam) {
        String url = this.getClass().getResource(classPath).toExternalForm();
        this.keepInRam = keepInRam;
        
        if (keepInRam) {
            sound = new AudioClip(url);
        } else {
            sound = new Media(url);            
        }
    }
    
    public void play(float balance) {
        if (keepInRam) {
            ((AudioClip) sound).setBalance(balance);
            ((AudioClip) sound).play();
        } else {
            MediaPlayer player = new MediaPlayer((Media) sound);
            player.setBalance(balance);
            player.play();
        }
    }
    
}
