package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class InitialGameMode extends AbstractGameMode {
    private SpriteBatch batch;
    private Texture logo;

    @Override
    public void preActivate() {
        batch = new SpriteBatch();
        logo = new Texture("badlogic.jpg");
    }

    @Override
    public void postDeactivate() {
        batch.dispose();
        logo.dispose();

        batch = null;
        logo = null;
    }

    public void render() {
        batch.begin();
        batch.draw(logo, 0, 0);
        batch.end();
    }
}
