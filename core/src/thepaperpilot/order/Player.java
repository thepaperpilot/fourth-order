package thepaperpilot.order;

import com.badlogic.gdx.Preferences;

public class Player {
    private static Preferences save;

    public static void setPreferences(Preferences preferences) {
        save = preferences;
    }

    public static void save() {

        save.flush();
    }

    public static void load() {

    }

    public static void reset() {

        load();
    }
}
