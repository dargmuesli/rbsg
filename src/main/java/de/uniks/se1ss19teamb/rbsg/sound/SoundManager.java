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

        instance.sounds.put("meme_bad_bitch", new Sound("bad_bitch.wav", true));
        instance.sounds.put("meme_beer", new Sound("beer.wav", true));
        instance.sounds.put("BazookaV1", new Sound("bazooka1.wav",true));
        instance.sounds.put("BazookaV1.1", new Sound("bazooka2.wav",true));
        instance.sounds.put("HelicopterV1", new Sound("chooper.wav",true));
        instance.sounds.put("JeepV1", new Sound("jeep",true));
        instance.sounds.put("lightTankV1", new Sound("lightTank",true));
        instance.sounds.put("InfantryV1_Fire", new Sound("infantry_fire",true));
        instance.sounds.put("InfantryV1_Move", new Sound("infantry_move",true));
        instance.sounds.put("panzer",new Sound("panzer.wav", true));

        //https://www.youtube.com/watch?v=VZec_PqXXzo
        //If anyone feels offended: Fight me
        Sound bgm = new Sound("katyusha.mp3", false);
        instance.sounds.put("bgm", bgm);

        instance.sounds.put("missing", new Sound("error.wav", true));
        instance.sounds.put("missing2", new Sound("fail-trombone-01.wav", true));
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
