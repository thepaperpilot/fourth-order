package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.Spells.StrikeComponent;

public class SpellComponent implements Component {

    public static Entity strike = new Entity();

    static {
        strike.add(new SpellComponent("Strike", 0, 0, 6, 0, 0));
        strike.add(new StrikeComponent());
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
