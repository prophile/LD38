package uk.co.alynn.games.snowglobe;

public interface GameMode {
    void tick(double dt);

    void preActivate();
    void postActivate();
    void preDeactivate();
    void postDeactivate();
}
