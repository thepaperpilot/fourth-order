package thepaperpilot.order;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;

public enum Rune {
    // admittedly having exp and damage in here makes some parts a bit weird, but it's alright (or at least, better than previously, with all its hard coding)
    POISON("Purple", Color.PURPLE),
    SURPRISE("Yellow", Color.YELLOW),
    MORTAL("Red", Color.FIREBRICK),
    STEAM("Blue", Color.TEAL),
    MASON("Green", Color.LIME),
    EXP("Exp", Color.WHITE),
    DAMAGE("Skull", Color.WHITE);

    public String colorName;
    public Color color;

    public static final Rune[] elementRunes;

    static {
        ArrayList<Rune> runes = new ArrayList<Rune>();
        runes.addAll(Arrays.asList(values()));
        runes.remove(EXP);
        runes.remove(DAMAGE);
        elementRunes = runes.toArray(new Rune[runes.size()]);
    }

    Rune(String colorName, Color color) {
        this.colorName = colorName;
        this.color = color;
    }

    public static Rune randomRune(boolean inclusive) {
        if (inclusive) return values()[MathUtils.random(values().length - 1)];
        return elementRunes[MathUtils.random(elementRunes.length - 1)];
    }
}
