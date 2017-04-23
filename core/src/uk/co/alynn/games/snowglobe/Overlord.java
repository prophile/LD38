package uk.co.alynn.games.snowglobe;

public class Overlord {
    public static Overlord s_instance = null;

    public static void init() {
        if (s_instance != null) {
            throw new RuntimeException("Double-init of Overlord");
        }

        s_instance = new Overlord();
    }

    private Overlord() {
    }
}
