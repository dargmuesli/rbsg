package de.uniks.se1ss19teamb.rbsg.sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static SoundManager instance = null;
    private Map<String, Sound> sounds = new HashMap<>();

    private SoundManager() {
    }

    public static void init() {
        instance = new SoundManager();

        instance.sounds.put("meme_bad_bitch", new Sound("bad_bitch.wav", false));
        instance.sounds.put("meme_beer", new Sound("beer.wav", false));

        Sound test = new Sound("panzer.wav", true);
        instance.sounds.put("panzer", test);

        //https://www.youtube.com/watch?v=VZec_PqXXzo
        //If anyone feels offended: Fight me
        Sound bgm = new Sound("katyusha.mp3", false);
        instance.sounds.put("bgm", bgm);

        //TODO Proper Missing sound
        Sound missing = new Sound("panzer.wav", true);
        instance.sounds.put("missing", missing);
    }

    public static void playSound(String toFetch, float balance) {
        instance.fetchSound(toFetch).play(balance);
    }

    public static boolean getLoaded(String toFetch) {
        Sound sound = instance.fetchSound(toFetch);
        return sound.keepInRam;
    }

    private Sound fetchSound(String toFetch) {
        Sound sound = sounds.get(toFetch);

        if (sound == null) {
            sound = sounds.get("missing");
        }

        return sound;
    }
}
