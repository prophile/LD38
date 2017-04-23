package uk.co.alynn.games.snowglobe;

import com.badlogic.gdx.utils.viewport.Viewport;

public interface GameMode {
    GameMode tick(double dt);
    void render();

    void preActivate();
    void postActivate();
    void preDeactivate();
    void postDeactivate();

    Viewport getViewport();

    void click(float x, float y);
    boolean usesCenteredCamera();
}
