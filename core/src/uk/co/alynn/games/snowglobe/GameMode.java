package uk.co.alynn.games.snowglobe;

public interface GameMode {
    GameMode tick(double dt);
    void render();

    void preActivate();
    void postActivate();
    void preDeactivate();
    void postDeactivate();
}
