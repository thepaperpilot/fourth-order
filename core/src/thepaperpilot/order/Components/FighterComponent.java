package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import thepaperpilot.order.Util.TextProgressBar;

public class FighterComponent implements Component {
    public float exp = 0;
    public float health = 10;
    public float poison = 0;
    public float surprise = 0;
    public float mortal = 0;
    public float steam = 0;
    public float mason = 0;
    public float maxHealth = 10;
    public float maxPoision = 20;
    public float maxSurprise = 20;
    public float maxMortal = 20;
    public float maxSteam = 20;
    public float maxMason = 20;
    public Entity[] spells = new Entity[]{};

    public TextProgressBar healthBar;
    public TextProgressBar poisonBar;
    public TextProgressBar surpriseBar;
    public TextProgressBar mortalBar;
    public TextProgressBar steamBar;
    public TextProgressBar masonBar;
    public TextProgressBar experience;
}
