package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.Spells.CommandComponent;
import thepaperpilot.order.Components.Spells.DestroyColorComponent;
import thepaperpilot.order.Components.Spells.RefreshComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;

public class SpellComponent implements Component {

    // TODO do this in a better way
    // poison, surprise, mortal, steam, mason
    public static Entity getStrikeSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Strike", 0, 0, 6, 0, 0));
        spell.add(new StrikeComponent());
        return spell;
    }

    public static Entity getAntidoteSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Antidote", 0, 4, 0, 4, 4));
        spell.add(new DestroyColorComponent(0));
        return spell;
    }

    public static Entity getPremonitionSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Premonition", 4, 0, 4, 4, 0));
        spell.add(new DestroyColorComponent(1));
        return spell;
    }

    public static Entity getImmortalitySpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Immortality", 4, 4, 0, 0, 4));
        spell.add(new DestroyColorComponent(2));
        return spell;
    }

    public static Entity getCondenseSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Condense", 4, 0, 4, 0, 4));
        spell.add(new DestroyColorComponent(3));
        return spell;
    }

    public static Entity getTruthSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Truth", 0, 4, 4, 4, 0));
        spell.add(new DestroyColorComponent(4));
        return spell;
    }

    public static Entity getRefreshSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Refresh", 3, 3, 3, 3, 3));
        spell.add(new RefreshComponent());
        return spell;
    }

    public static Entity getCommandSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Command", 10, 0, 20, 0, 0));
        spell.add(new CommandComponent());
        return spell;
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
