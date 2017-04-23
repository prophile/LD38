package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AbstractGameMode implements GameMode {
    protected Viewport viewport;

    public AbstractGameMode() {
        viewport = createViewport();
    }

    protected Viewport createViewport() {
        return new FitViewport(1024.0f, 640.0f);
    }

    @Override
    public GameMode tick(double dt) {
        return this;
    }

    @Override
    public void render() {
    }

    @Override
    public void preActivate() {
    }

    @Override
    public void postActivate() {
    }

    @Override
    public void preDeactivate() {
    }

    @Override
    public void postDeactivate() {
    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public void click(float x, float y) {

    }
}
