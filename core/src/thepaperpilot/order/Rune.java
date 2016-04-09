package thepaperpilot.order;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;

public enum Rune {
    // admittedly having exp and damage in here makes some parts a bit weird, but it's alright (or at least, better than previously, with all its hard coding)
    POISON("Purple", "Poison Mastery", Color.PURPLE),
    SURPRISE("Yellow", "Surprise Mastery", Color.YELLOW),
    MORTAL("Red", "Mortal Mastery", Color.FIREBRICK),
    STEAM("Blue", "Steam Mastery", Color.TEAL),
    MASON("Green", "Mason Mastery", Color.LIME),
    EXP("Exp", "Morale", Color.WHITE),
    DAMAGE("Skull", "Battle", Color.WHITE);

    public String colorName;
    public String skill;
    public Color color;

    public static final Rune[] elementRunes;

    static {
        ArrayList<Rune> runes = new ArrayList<Rune>();
        runes.addAll(Arrays.asList(values()));
        runes.remove(EXP);
        runes.remove(DAMAGE);
        elementRunes = runes.toArray(new Rune[runes.size()]);
    }

    Rune(String colorName, String skill, Color color) {
        this.colorName = colorName;
        this.skill = skill;
        this.color = color;
    }

    public static Rune randomRune(boolean inclusive) {
        if (inclusive) return values()[MathUtils.random(values().length - 1)];
        return elementRunes[MathUtils.random(elementRunes.length - 1)];
    }
}
