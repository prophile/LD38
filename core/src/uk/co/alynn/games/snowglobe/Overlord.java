package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Overlord {
    public static Overlord s_instance = null;

    public final AssetManager assetManager;
    public final ShaderProgram fontShader;

    public static void init() {
        if (s_instance != null) {
            throw new RuntimeException("Double-init of Overlord");
        }

        s_instance = new Overlord();

        s_instance.initSprites();
        s_instance.initScreens();
        s_instance.initFont();
        s_instance.initSFX();
    }

    private void initSFX() {
        for (SFX fx : SFX.values()) {
            assetManager.load(fx.getFilename(), Sound.class);
        }
    }

    private Overlord() {
        assetManager = new AssetManager();
        fontShader = new ShaderProgram(Gdx.files.internal("text.vert"), Gdx.files.internal("text.frag"));
    }

    private void initSprites() {
        TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
        params.genMipMaps = true;
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.MipMapLinearLinear;

        assetManager.load("badlogic.jpg", Texture.class, params);
        assetManager.load("eraser.png", Texture.class, params);
    }

    private void initScreens() {
        TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
        params.genMipMaps = false;
        params.minFilter = Texture.TextureFilter.Nearest;
        params.magFilter = Texture.TextureFilter.Nearest;

        assetManager.load("gs_win.png", Texture.class, params);
        assetManager.load("gs_lose.png", Texture.class, params);
        assetManager.load("gs_intro.png", Texture.class, params);
    }

    private void initFont() {
        BitmapFontLoader.BitmapFontParameter param = new BitmapFontLoader.BitmapFontParameter();
        param.genMipMaps = true;
        param.magFilter = Texture.TextureFilter.Linear;
        param.minFilter = Texture.TextureFilter.MipMapLinearLinear;

        assetManager.load("bitstream.fnt", BitmapFont.class, param);
    }
}
