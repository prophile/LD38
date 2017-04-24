package uk.co.alynn.games.snowglobe;

public class MenuGameMode extends SingleTextureGameMode {
    public MenuGameMode() {
        super("am_start.png", new SingleTextureGameMode.Receiver() {

            @Override
            public GameMode receive() {
                return new MainGameMode();
            }
        });
    }
}
