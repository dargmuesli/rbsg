package de.uniks.se1ss19teamb.rbsg.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SoundManager {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<SoundManager> instance;
    private Map<String, Sound> sounds = new HashMap<>();

    private SoundManager() {
    }

    public static void init() {
        instance = Optional.of(new SoundManager());

        instance.ifPresent(soundManager -> {
            soundManager.sounds.put("meme_bad_bitch", new Sound("bad_bitch.wav", true));
            soundManager.sounds.put("meme_beer", new Sound("beer.wav", true));

            soundManager.sounds.put("BazookaTrooper", new Sound("bazooka1.wav",true));
            soundManager.sounds.put("BazookaTrooper2", new Sound("bazooka2.wav",true));
            //instance.sounds.put("BazookaTrooper_Move", new Sound(".wav",true));

            soundManager.sounds.put("Chopper", new Sound("chooper.wav",true));
            //instance.sounds.put("Chopper_Move", new Sound(.wav",true));


            soundManager.sounds.put("Jeep", new Sound("jeep.wav",true));
            //instance.sounds.put("Jeep_Move", new Sound(".wav",true));

            soundManager.sounds.put("LightTank", new Sound("lightTank.wav",true));
            //instance.sounds.put("LightTank_Move", new Sound(".wav",true));

            soundManager.sounds.put("Infantry", new Sound("infantry_fire.wav",true));
            soundManager.sounds.put("Infantry_Move", new Sound("infantry_move.wav",true));

            soundManager.sounds.put("HeavyTank",new Sound("panzer.wav", true));
            //instance.sounds.put("HeavyTank_Move",new Sound(".wav", true));

            soundManager.sounds.put("nani", new Sound("nani.wav", true));
            soundManager.sounds.put("Omae", new Sound("Omae_wa_mou_shindeiru.wav", true));
            soundManager.sounds.put("Omae_nani", new Sound("Omae_wa_mou_shindeiru_nani.wav", true));

            //https://www.youtube.com/watch?v=VZec_PqXXzo
            //If anyone feels offended: Fight me
            Sound bgm = new Sound("katyusha.mp3", false);
            soundManager.sounds.put("bgm", bgm);

            soundManager.sounds.put("missing", new Sound("silence.wav", true));
            soundManager.sounds.put("missing2", new Sound("fail-trombone-01.wav", true));
        });
    }

    public static void playSound(String toFetch, float balance) {
        instance.ifPresent(soundManager -> soundManager.fetchSound(toFetch).play(balance));
    }

    public static boolean getLoaded(String toFetch) {
        return instance.map(soundManager -> soundManager.fetchSound(toFetch).keepInRam).orElse(false);
    }

    private Sound fetchSound(String toFetch) {
        return sounds.getOrDefault(toFetch, sounds.get("missing"));
    }
}
