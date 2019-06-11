package de.uniks.se1ss19teamb.rbsg.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	
	protected boolean keepInRAM;
	
	protected Object sound;
	
	protected Sound(String classPath, boolean keepInRAM) {
		String url = this.getClass().getResource(classPath).toExternalForm();
		this.keepInRAM = keepInRAM;
		
		if(keepInRAM) {
			sound = new AudioClip(url);
		}
		else {
			sound = new Media(url);			
		}
	}
	
	public void play(float balance) {
		if(keepInRAM) {
			((AudioClip) sound).setBalance(balance);
			((AudioClip) sound).play();
		}
		else {
			MediaPlayer player = new MediaPlayer((Media) sound);
			player.setBalance(balance);
			player.play();
		}
	}
	
}
