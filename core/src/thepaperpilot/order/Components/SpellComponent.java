package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.Spells.*;

public class SpellComponent implements Component {

    // TODO do this in a better way
    // poison, surprise, mortal, steam, mason

    // how to implement a spell:
    // add it to this list (ugh)
    // create a component for the spell, extending GlyphComponent or TotemComponent, or Component (for generic spells)
    // create a system for the spell- extend GlyphSystem or TotemSystem or SpellSystem
    // if its a totem, also create an effect component, and check for that effect wherever it should apply
    // add the spell to the fightercomponents of all enemies with the spell, and to any/all class spelllists (currently in fightercomponent, ugh)
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
        spell.add(new TotemComponent());
        return spell;
    }

    public static Entity getSustainSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Sustain", 0, 0, 10, 20, 0));
        spell.add(new HealingComponent());
        spell.add(new TotemComponent());
        return spell;
    }

    public static Entity getCollectSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Collect", 0, 20, 0, 10, 0));
        spell.add(new CollectComponent());
        spell.add(new TotemComponent());
        return spell;
    }

    /* TODO what does a disrupt totem do?
    public static Entity getDisruptSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Disrupt", 0, 10, 0, 0, 20));
        spell.add(new DisruptComponent());
        spell.add(new TotemComponent());
        return spell;
    }*/

    public static Entity getWitherSpell() {
        Entity spell = new Entity();
        spell.add(new SpellComponent("Wither", 20, 0, 0, 0, 10));
        spell.add(new DamageComponent());
        spell.add(new TotemComponent());
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
