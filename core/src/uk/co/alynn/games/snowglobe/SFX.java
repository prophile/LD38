package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.audio.Sound;

import java.util.Random;

public enum SFX {
    SELECT("select", 0.4f),
    UNSELECT("unselect", 0.4f),
    CAPTURE("capture", 0.4f, 0.2f),
    TRANSFER("transfer", 0.4f, 0.2f),
    MEEPMERP("meepmerp", 0.4f),
    SKIP("skip", 0.3f);

    SFX(String slug) {
        this(slug, 1.0f, 0.0f);
    }

    SFX(String slug, float volumeModifier) {
        this(slug, volumeModifier, 0.0f);
    }

    SFX(String slug, float volumeModifier, float pitchVariance) {
        this.slug = slug;
        this.volumeModifier = volumeModifier;
        this.pitchVariance = pitchVariance;
    }

    public final String slug;
    public final float volumeModifier;
    public final float pitchVariance;

    private static final Random rng = new Random();

    public String getFilename() {
        return "sfx/" + slug + ".ogg";
    }

    public void play(float volume) {;
        Sound snd = Overlord.s_instance.assetManager.get(getFilename(), Sound.class);
        float pitch;
        if (pitchVariance == 0.0f) {
            pitch = 1.0f;
        } else {
            pitch = (float)Math.pow(2.0, pitchVariance * rng.nextGaussian());
        }
        snd.play(volume * volumeModifier, pitch, 0.0f);
    }

    public void play() {
        play(1.0f);
    }
}
