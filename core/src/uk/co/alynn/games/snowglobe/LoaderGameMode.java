package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    protected Viewport createViewport() {
        return new ScreenViewport();
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
            return new MenuGameMode();
        }

        return this;
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(logo, Gdx.graphics.getWidth() / 2.0f - logo.getWidth() / 2.0f, 100);
        batch.end();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        shapeRenderer.rect(0.0f, 0.0f, Gdx.graphics.getWidth() * Overlord.s_instance.assetManager.getProgress(),30.0f);
        shapeRenderer.end();
    }
}
