package thepaperpilot.order;

import com.badlogic.ashley.core.Entity;
import thepaperpilot.order.Components.SpellComponent;

import java.util.*;

public enum Class {
    ALCHEMIST,
    ROGUE,
    RANGER,
    PALADIN,
    WIZARD;

    static {
        ALCHEMIST.proficiency.put(Rune.POISON, 4);
        ALCHEMIST.proficiency.put(Rune.SURPRISE, 2);
        ALCHEMIST.proficiency.put(Rune.MORTAL, 2);
        ALCHEMIST.proficiency.put(Rune.STEAM, 2);
        ALCHEMIST.proficiency.put(Rune.MASON, 0);
        ALCHEMIST.proficiency.put(Rune.DAMAGE, 2);
        ALCHEMIST.proficiency.put(Rune.EXP, 2);
        ALCHEMIST.spells.put(SpellComponent.getSpell("Strike"), 0);
        ALCHEMIST.spells.put(SpellComponent.getSpell("Truth"), 1);
        ALCHEMIST.spells.put(SpellComponent.getSpell("Refresh"), 2);
        ALCHEMIST.spells.put(SpellComponent.getSpell("Wither"), 3);

        ROGUE.proficiency.put(Rune.POISON, 2);
        ROGUE.proficiency.put(Rune.SURPRISE, 4);
        ROGUE.proficiency.put(Rune.MORTAL, 2);
        ROGUE.proficiency.put(Rune.STEAM, 0);
        ROGUE.proficiency.put(Rune.MASON, 2);
        ROGUE.proficiency.put(Rune.DAMAGE, 2);
        ROGUE.proficiency.put(Rune.EXP, 2);
        ROGUE.spells.put(SpellComponent.getSpell("Strike"), 0);
        ROGUE.spells.put(SpellComponent.getSpell("Condense"), 1);
        ROGUE.spells.put(SpellComponent.getSpell("Refresh"), 2);
        ROGUE.spells.put(SpellComponent.getSpell("Collect"), 3);

        RANGER.proficiency.put(Rune.POISON, 0);
        RANGER.proficiency.put(Rune.SURPRISE, 2);
        RANGER.proficiency.put(Rune.MORTAL, 4);
        RANGER.proficiency.put(Rune.STEAM, 2);
        RANGER.proficiency.put(Rune.MASON, 2);
        RANGER.proficiency.put(Rune.DAMAGE, 2);
        RANGER.proficiency.put(Rune.EXP, 2);
        RANGER.spells.put(SpellComponent.getSpell("Strike"), 0);
        RANGER.spells.put(SpellComponent.getSpell("Antidote"), 1);
        RANGER.spells.put(SpellComponent.getSpell("Refresh"), 2);
        RANGER.spells.put(SpellComponent.getSpell("Command"), 3);

        PALADIN.proficiency.put(Rune.POISON, 2);
        PALADIN.proficiency.put(Rune.SURPRISE, 2);
        PALADIN.proficiency.put(Rune.MORTAL, 0);
        PALADIN.proficiency.put(Rune.STEAM, 4);
        PALADIN.proficiency.put(Rune.MASON, 2);
        PALADIN.proficiency.put(Rune.DAMAGE, 2);
        PALADIN.proficiency.put(Rune.EXP, 2);
        PALADIN.spells.put(SpellComponent.getSpell("Strike"), 0);
        PALADIN.spells.put(SpellComponent.getSpell("Immortality"), 1);
        PALADIN.spells.put(SpellComponent.getSpell("Refresh"), 2);
        PALADIN.spells.put(SpellComponent.getSpell("Sustain"), 3);

        WIZARD.proficiency.put(Rune.POISON, 2);
        WIZARD.proficiency.put(Rune.SURPRISE, 0);
        WIZARD.proficiency.put(Rune.MORTAL, 2);
        WIZARD.proficiency.put(Rune.STEAM, 2);
        WIZARD.proficiency.put(Rune.MASON, 4);
        WIZARD.proficiency.put(Rune.DAMAGE, 2);
        WIZARD.proficiency.put(Rune.EXP, 2);
        WIZARD.spells.put(SpellComponent.getSpell("Strike"), 0);
        WIZARD.spells.put(SpellComponent.getSpell("Premonition"), 1);
        WIZARD.spells.put(SpellComponent.getSpell("Refresh"), 2);
        // NYI WIZARD.spells.put(SpellComponent.getSpell("Disrupt"), 3);
    }

    public Map<Rune, Integer> proficiency = new EnumMap<Rune, Integer>(Rune.class);
    public Map<Entity, Integer> spells = new LinkedHashMap<Entity, Integer>(); // integer is the level they get that spell
}
