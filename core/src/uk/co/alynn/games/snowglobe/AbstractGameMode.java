package uk.co.alynn.games.snowglobe;

public abstract class AbstractGameMode implements GameMode {
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
}
