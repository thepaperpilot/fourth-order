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
import java.util.Map;

public class Player {
    private static Preferences save;
    private static FighterComponent player;
    private static ArrayList<String> attributes = new ArrayList<String>();

    public static boolean sound;
    public static boolean music;

    public static void saveSound() {
        save.putBoolean("sound", sound);
        save.putBoolean("music", music);

        save.flush();
    }

    public static void setPreferences(Preferences preferences) {
        save = preferences;
        sound = save.getBoolean("sound", true);
        music = save.getBoolean("music", true);
    }

    public static void save() {
        String attributes = Arrays.toString(Player.attributes.toArray(new String[Player.attributes.size()])).replaceAll(", ", ",");
        save.putString("attributes", attributes.substring(1, attributes.length() - 1));

        String spellString = "";
        for (Entity spell : player.spells) {
            spellString += Mappers.spell.get(spell).name + ",";
        }
        save.putString("spells", spellString);

        String knownSpellString = "";
        for (Entity spell : player.knownSpells) {
            knownSpellString += Mappers.spell.get(spell).name + ",";
        }
        save.putString("knownspells", knownSpellString);

        String skillsString = "";
        for (Rune rune : Rune.values()) {
            skillsString += player.skills.get(rune) + ",";
        }
        save.putString("skills", skillsString);

        save.putInteger("skillpoints", player.skillPoints);
        save.putInteger("level", player.level);
        save.putFloat("exp", player.runes.get(Rune.EXP));
        save.putString("class", player.fighterClass.name());

        save.flush();
    }

    public static void load() {
        player = new FighterComponent(Class.valueOf(save.getString("class", "PALADIN")), save.getInteger("level", 0)); // paladin as default is completely arbitrary :)

        Player.attributes.clear();
        String[] attributes = save.getString("attributes", "").split(",");
        Collections.addAll(Player.attributes, attributes);

        String[] spells = save.getString("spells", "Strike").split(",");
        for (String spell : spells) {
            if (spell == null || !SpellComponent.spells.containsKey(spell)) continue;
            player.spells.add(SpellComponent.getSpell(spell));
        }

        String[] knownspells = save.getString("knownspells", "Strike").split(",");
        for (String spell : knownspells) {
            player.knownSpells.add(SpellComponent.getSpell(spell));
        }

        Map<Rune, Integer> baseStats = Class.PALADIN.proficiency;
        String defaultSkills = "";
        for (Rune rune : Rune.values()) {
            defaultSkills += baseStats.get(rune) + ",";
        }
        String[] skills = save.getString("skills", defaultSkills).split(",");
        for (int i = 0; i < skills.length; i++) {
            player.skills.put(Rune.values()[i], Float.parseFloat(skills[i]));
        }

        player.skillPoints = save.getInteger("skillpoints");
        player.runes.put(Rune.EXP, save.getFloat("exp", 0));

        Main.changeScreen(getAttribute("intro") ? new MapScreen() : new IntroScreen());
    }

    public static void reset() {
        save.remove("attributes");
        save.remove("knownspells");
        save.remove("spells");
        save.remove("skills");
        save.remove("level");
        save.remove("skillpoints");
        save.remove("exp");
        save.remove("class");

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
