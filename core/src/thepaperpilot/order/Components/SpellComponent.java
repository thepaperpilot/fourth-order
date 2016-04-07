package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.Spells.*;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Util.Mappers;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SpellComponent implements Component {

    private static Map<String, Entity> spells = new HashMap<String, Entity>();

    static {
        Entity spell = new Entity();
        SpellComponent sc = new SpellComponent("Strike");
        sc.cost.put(Rune.MORTAL, 6f);
        spell.add(sc);
        spell.add(new StrikeComponent());
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Antidote");
        sc.cost.put(Rune.SURPRISE, 4f);
        sc.cost.put(Rune.STEAM, 4f);
        sc.cost.put(Rune.MASON, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.POISON));
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Premonition");
        sc.cost.put(Rune.POISON, 4f);
        sc.cost.put(Rune.MORTAL, 4f);
        sc.cost.put(Rune.STEAM, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.SURPRISE));
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Immortality");
        sc.cost.put(Rune.POISON, 4f);
        sc.cost.put(Rune.SURPRISE, 4f);
        sc.cost.put(Rune.MASON, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.MORTAL));
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Condense");
        sc.cost.put(Rune.POISON, 4f);
        sc.cost.put(Rune.MORTAL, 4f);
        sc.cost.put(Rune.MASON, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.STEAM));
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Truth");
        sc.cost.put(Rune.SURPRISE, 4f);
        sc.cost.put(Rune.MORTAL, 4f);
        sc.cost.put(Rune.STEAM, 4f);
        spell.add(sc);
        spell.add(new DestroyColorComponent(Rune.MASON));
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Refresh");
        sc.cost.put(Rune.POISON, 3f);
        sc.cost.put(Rune.SURPRISE, 3f);
        sc.cost.put(Rune.MORTAL, 3f);
        sc.cost.put(Rune.STEAM, 3f);
        sc.cost.put(Rune.MASON, 3f);
        spell.add(sc);
        spell.add(new RefreshComponent());
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Command");
        sc.cost.put(Rune.MORTAL, 20f);
        sc.cost.put(Rune.POISON, 10f);
        spell.add(sc);
        spell.add(new CommandComponent());
        spell.add(new TotemComponent());
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Sustain");
        sc.cost.put(Rune.STEAM, 20f);
        sc.cost.put(Rune.MORTAL, 10f);
        spell.add(sc);
        spell.add(new HealingComponent());
        spell.add(new TotemComponent());
        spells.put(sc.name, spell);

        spell = new Entity();
        sc = new SpellComponent("Collect");
        sc.cost.put(Rune.SURPRISE, 20f);
        sc.cost.put(Rune.STEAM, 10f);
        spell.add(sc);
        spell.add(new CollectComponent());
        spell.add(new TotemComponent());
        spells.put(sc.name, spell);

        /* TODO prevents other fighter from casting spells
        spell = new Entity();
        spell.add(new SpellComponent("Disrupt", 0, 10, 0, 0, 20));
        spell.add(new DisruptComponent());
        spell.add(new TotemComponent());
        spells.put(sc.name, spell);
         */

        spell = new Entity();
        sc = new SpellComponent("Wither");
        sc.cost.put(Rune.POISON, 20f);
        sc.cost.put(Rune.MASON, 10f);
        spell.add(sc);
        spell.add(new DamageComponent());
        spell.add(new TotemComponent());
        spells.put(sc.name, spell);
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

    public static Entity getSpell(String spell) {
        // the SpellComponent has displays that can only be used in one place, so each instance of the spell needs its own SpellComponent
        // the rest of the components should be fine? if there's an issue it can be fixed here
        Entity clone = new Entity();
        Entity entity = spells.get(spell);
        for (Component component : entity.getComponents()) {
            clone.add(component);
        }
        SpellComponent sc = new SpellComponent(spell);
        for (Rune rune : Rune.values()) {
            sc.cost.put(rune, Mappers.spell.get(entity).cost.get(rune));
        }
        clone.add(sc);
        return clone;
    }
}
