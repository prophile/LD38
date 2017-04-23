package uk.co.alynn.games.snowglobe;

public class MenuGameMode extends SingleTextureGameMode {
    public MenuGameMode() {
        super("gs_intro.png", new SingleTextureGameMode.Receiver() {

            @Override
            public GameMode receive() {
                return new MainGameMode();
            }
        });
    }
}
