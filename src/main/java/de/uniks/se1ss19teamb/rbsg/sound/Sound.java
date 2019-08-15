package de.uniks.se1ss19teamb.rbsg.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class Sound {

    boolean keepInRam;

    private Object sound;

    Sound(String classPath, boolean keepInRam) {
        String url = this.getClass().getResource(classPath).toExternalForm();
        this.keepInRam = keepInRam;

        if (keepInRam) {
            sound = new AudioClip(url);
        } else {
            sound = new Media(url);
        }
    }

    void play(float volume, float balance) {
        if (keepInRam) {
            ((AudioClip) sound).setVolume(volume);
            ((AudioClip) sound).setBalance(balance);
            ((AudioClip) sound).play();
        } else {
            MediaPlayer player = new MediaPlayer((Media) sound);
            player.setVolume(volume);
            player.setBalance(balance);
            player.play();
        }
    }
}
