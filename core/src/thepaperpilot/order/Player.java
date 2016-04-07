package thepaperpilot.order;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Preferences;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Screens.IntroScreen;
import thepaperpilot.order.Screens.MapScreen;
import thepaperpilot.order.Util.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {
    private static Preferences save;
    private static FighterComponent player;
    private static ArrayList<String> attributes = new ArrayList<String>();

    public static void setPreferences(Preferences preferences) {
        save = preferences;
    }

    public static void save() {
        String attributes = Arrays.toString(Player.attributes.toArray(new String[Player.attributes.size()])).replaceAll(", ", ",");
        save.putString("attributes", attributes.substring(1, attributes.length() - 1));

        String spellString = "";
        for (Entity spell : player.spells) {
            spellString += Mappers.spell.get(spell).name + ",";
        }
        save.putString("spells", spellString);

        save.putInteger("level", player.level);
        save.putFloat("exp", player.runes.get(Rune.EXP));

        save.flush();
    }

    public static void load() {
        player = new FighterComponent();

        Player.attributes.clear();
        String[] attributes = save.getString("attributes", "").split(",");
        Collections.addAll(Player.attributes, attributes);

        String[] spells = save.getString("spells", "Strike").split(",");
        for (String spell : spells) {
            player.spells.add(SpellComponent.getSpell(spell));
        }

        player.level = save.getInteger("level", 1);
        player.runes.put(Rune.EXP, save.getFloat("exp", 0));

        Main.changeScreen(getAttribute("intro") ? new MapScreen() : new IntroScreen());
    }

    public static void reset() {
        save.remove("attributes");
        save.remove("spells");
        save.remove("level");
        save.remove("exp");

        load();
    }

    public static FighterComponent getPlayer() {
        return player;
    }

    public static boolean getAttribute(String attribute) {
        return attributes.contains(attribute);
    }

    public static void addAttribute(String attribute) {
        attributes.add(attribute);
    }
}
