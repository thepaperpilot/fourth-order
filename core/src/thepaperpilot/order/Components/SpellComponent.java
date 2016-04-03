package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.Spells.DestroyColorComponent;
import thepaperpilot.order.Components.Spells.RefreshComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;

public class SpellComponent implements Component {

    // TODO probably a better way to do this
    public static Entity strike = new Entity();
    public static Entity antidote = new Entity();
    public static Entity premonition = new Entity();
    public static Entity immortality = new Entity();
    public static Entity condense = new Entity();
    public static Entity truth = new Entity();
    public static Entity refresh = new Entity();

    static {
        // new SpellComponent(name, poison, surprise, mortal, steam, mason)
        strike.add(new SpellComponent("Strike", 0, 0, 6, 0, 0));
        strike.add(new StrikeComponent());
        antidote.add(new SpellComponent("Antidote", 0, 4, 0, 4, 4));
        antidote.add(new DestroyColorComponent(0));
        premonition.add(new SpellComponent("Premonition", 4, 0, 4, 4, 0));
        premonition.add(new DestroyColorComponent(1));
        immortality.add(new SpellComponent("Immortality", 4, 4, 0, 0, 4));
        immortality.add(new DestroyColorComponent(2));
        condense.add(new SpellComponent("Condense", 4, 0, 4, 0, 4));
        condense.add(new DestroyColorComponent(3));
        truth.add(new SpellComponent("Truth", 0, 4, 4, 4, 0));
        truth.add(new DestroyColorComponent(4));
        refresh.add(new SpellComponent("Refresh", 3, 3, 3, 3, 3));
        refresh.add(new RefreshComponent());
    }

    public String name = "";
    public int poison = 0;
    public int surprise = 0;
    public int mortal = 0;
    public int steam = 0;
    public int mason = 0;

    public Image poisonDisplay = new Image();
    public Image surpriseDisplay = new Image();
    public Image mortalDisplay = new Image();
    public Image steamDisplay = new Image();
    public Image masonDisplay = new Image();

    public SpellComponent(String name, int poison, int surprise, int mortal, int steam, int mason) {
        this.name = name;
        this.poison = poison;
        this.surprise = surprise;
        this.mortal = mortal;
        this.steam = steam;
        this.mason = mason;
    }
}
