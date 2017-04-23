package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SingleTextureGameMode extends AbstractGameMode {
    public interface Receiver {
        GameMode receive();
    }

    public SingleTextureGameMode(String textureName, Receiver receiver) {
        texture = Overlord.s_instance.assetManager.get(textureName, Texture.class);
        this.receiver = receiver;
    }

    private final Texture texture;
    private SpriteBatch batch;
    private GameMode nextMode = null;
    private final Receiver receiver;

    @Override
    public GameMode tick(double dt) {
        if (nextMode != null) {
            return nextMode;
        } else {
            return this;
        }
    }

    @Override
    public void preActivate() {
        batch = new SpriteBatch();
    }

    @Override
    public void postDeactivate() {
        batch.dispose();
        batch = null;
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(texture, 0.0f, 0.0f, 1024.0f, 640.0f);
        batch.end();
    }

    @Override
    public void click(float x, float y) {
        nextMode = receiver.receive();
    }
}
