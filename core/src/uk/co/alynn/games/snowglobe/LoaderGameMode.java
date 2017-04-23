package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LoaderGameMode extends AbstractGameMode {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture logo;

    @Override
    public void preActivate() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        logo = new Texture("badlogic.jpg");
    }

    @Override
    public void postDeactivate() {
        batch.dispose();
        shapeRenderer.dispose();
        logo.dispose();

        batch = null;
        shapeRenderer = null;
        logo = null;
    }

    @Override
    public GameMode tick(double dt) {
        boolean finishedLoading = Overlord.s_instance.assetManager.update((int)(1000 * dt));

        if (finishedLoading) {
            // Do something smart here to move to the actual game
            return new MainGameMode();
        }

        return this;
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(logo, Gdx.graphics.getWidth() / 2.0f - logo.getWidth() / 2.0f, 100);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        shapeRenderer.rect(0.0f, 0.0f, Gdx.graphics.getWidth() * Overlord.s_instance.assetManager.getProgress(),30.0f);
        shapeRenderer.end();
    }
}
