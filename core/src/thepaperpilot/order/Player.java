package thepaperpilot.order;

import com.badlogic.gdx.Preferences;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;

public class Player {
    // TODO?
    private static Preferences save;
    private static FighterComponent player;

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

    public static FighterComponent getPlayer() {
        if (player == null) {
            player = new FighterComponent();
            player.add(SpellComponent.getStrikeSpell());
        }
        return player;
    }

    public static boolean hasPlayer() {
        return player != null;
    }
}
