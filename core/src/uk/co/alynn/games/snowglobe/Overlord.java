package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

public class Overlord {
    public static Overlord s_instance = null;

    public final AssetManager assetManager;

    public static void init() {
        if (s_instance != null) {
            throw new RuntimeException("Double-init of Overlord");
        }

        s_instance = new Overlord();

        s_instance.initSprites();
    }

    private Overlord() {
        assetManager = new AssetManager();
    }

    private void initSprites() {
        TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
        params.genMipMaps = true;
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.MipMapLinearLinear;

        assetManager.load("badlogic.jpg", Texture.class, params);
    }
}
