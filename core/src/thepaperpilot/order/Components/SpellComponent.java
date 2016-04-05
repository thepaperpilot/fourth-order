package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.Spells.*;
import thepaperpilot.order.Rune;

import java.util.EnumMap;
import java.util.Map;

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
        SpellComponent sc = new SpellComponent("Strike");
        sc.cost.put(Rune.MORTAL, 6f);
        spell.add(sc);
        spell.add(new StrikeComponent());
        return spell;
    }

    public static Entity getAntidoteSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Antidote");
        sc.cost.put(Rune.SURPRISE, 4f);
        sc.cost.put(Rune.STEAM, 4f);
        sc.cost.put(Rune.MASON, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.POISON));
        return spell;
    }

    public static Entity getPremonitionSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Premonition");
        sc.cost.put(Rune.POISON, 4f);
        sc.cost.put(Rune.MORTAL, 4f);
        sc.cost.put(Rune.STEAM, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.SURPRISE));
        return spell;
    }

    public static Entity getImmortalitySpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Immortality");
        sc.cost.put(Rune.POISON, 4f);
        sc.cost.put(Rune.SURPRISE, 4f);
        sc.cost.put(Rune.MASON, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.MORTAL));
        return spell;
    }

    public static Entity getCondenseSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Condense");
        sc.cost.put(Rune.POISON, 4f);
        sc.cost.put(Rune.MORTAL, 4f);
        sc.cost.put(Rune.MASON, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.STEAM));
        return spell;
    }

    public static Entity getTruthSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Truth");
        sc.cost.put(Rune.SURPRISE, 4f);
        sc.cost.put(Rune.MORTAL, 4f);
        sc.cost.put(Rune.STEAM, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.MASON));
        return spell;
    }

    public static Entity getRefreshSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Refresh");
        sc.cost.put(Rune.POISON, 3f);
        sc.cost.put(Rune.SURPRISE, 3f);
        sc.cost.put(Rune.MORTAL, 3f);
        sc.cost.put(Rune.STEAM, 3f);
        sc.cost.put(Rune.MASON, 3f);
        spell.add(sc);
        spell.add(new RefreshComponent());
        return spell;
    }

    public static Entity getCommandSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Command");
        sc.cost.put(Rune.MORTAL, 20f);
        sc.cost.put(Rune.POISON, 10f);
        spell.add(sc);
        spell.add(new CommandComponent());
        spell.add(new TotemComponent());
        return spell;
    }

    public static Entity getSustainSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Sustain");
        sc.cost.put(Rune.STEAM, 20f);
        sc.cost.put(Rune.MORTAL, 10f);
        spell.add(sc);
        spell.add(new HealingComponent());
        spell.add(new TotemComponent());
        return spell;
    }

    public static Entity getCollectSpell() {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Collect");
        sc.cost.put(Rune.SURPRISE, 20f);
        sc.cost.put(Rune.STEAM, 10f);
        spell.add(sc);
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
        SpellComponent sc = new SpellComponent("Wither");
        sc.cost.put(Rune.POISON, 20f);
        sc.cost.put(Rune.MASON, 10f);
        spell.add(sc);
        spell.add(new DamageComponent());
        spell.add(new TotemComponent());
        return spell;
    }

    public String name = "";
    public Map<Rune, Float> cost = new EnumMap<Rune, Float>(Rune.class);
    public Map<Rune, Image> displays = new EnumMap<Rune, Image>(Rune.class);

    public SpellComponent(String name) {
        this.name = name;
        for (Rune rune : Rune.values()) {
            cost.put(rune, 0f);
            displays.put(rune, new Image());
        }
    }
}
