package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.order.Class;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

import java.util.*;

public class FighterComponent implements Component {
    Drawable empty = new Image(Main.getTexture("UICircleEmpty")).getDrawable();
    Drawable filled = new Image(Main.getTexture("UICircleFilled")).getDrawable();

    public String portrait = "PortraitPlayer";

    public Map<Rune, Float> runes = new EnumMap<Rune, Float>(Rune.class);
    public Map<Rune, Float> maxRunes = new EnumMap<Rune, Float>(Rune.class);
    public Map<Rune, ProgressBar> bars = new EnumMap<Rune, ProgressBar>(Rune.class);
    public Map<Rune, Label> labels = new EnumMap<Rune, Label>(Rune.class);
    public ArrayList<Entity> spells = new ArrayList<Entity>();
    public ArrayList<Entity> knownSpells = new ArrayList<Entity>();
    public Map<Rune, Float> skills = new EnumMap<Rune, Float>(Rune.class);
    public Class fighterClass;
    public int skillPoints = 0;
    public int level = 0;

    public FighterComponent(Class fighterClass, int level) {
        this.fighterClass = fighterClass;
        this.level = level;
        for (Rune rune : Rune.values()) {
            switch (rune) {
                case DAMAGE:
                    runes.put(rune, 20f);
                    maxRunes.put(rune, 20f);
                    break;
                case EXP:
                    runes.put(rune, (float) (Constants.BASE_EXP * (level == 0 ? 0 : Math.pow(Constants.EXP_CURVE, level - 1))));
                    maxRunes.put(rune, (float) (Constants.BASE_EXP * Math.pow(Constants.EXP_CURVE, level)));
                    break;
                default:
                    runes.put(rune, 0f);
                    maxRunes.put(rune, 40f);
                    break;
            }
        }
    }

    public void reset(PuzzleSystem puzzle) {
        for (Rune rune : Rune.values()) {
            switch (rune) {
                case DAMAGE:
                    runes.put(Rune.DAMAGE, (float) (Constants.BASE_HEALTH * Math.pow(Constants.HEALTH_CURVE, skills.get(Rune.DAMAGE))));
                    maxRunes.put(Rune.DAMAGE, runes.get(Rune.DAMAGE));
                    break;
                case EXP:
                    // don't reset experience
                    break;
                default:
                    runes.put(rune, 0f);
                    break;
            }
        }
        while (runes.get(Rune.EXP) >= maxRunes.get(Rune.EXP)) {
            levelUp(puzzle);
        }
    }

    public void add(RuneComponent rc, PuzzleSystem puzzle) {
        if (rc.rune == Rune.EXP)
            runes.put(Rune.EXP, runes.get(Rune.EXP) + 1);
        else
            runes.put(rc.rune, Math.min(runes.get(rc.rune) + 1, maxRunes.get(rc.rune)));

        updateProgressBars();

        while (runes.get(Rune.EXP).equals(maxRunes.get(Rune.EXP))) {
            levelUp(puzzle);
        }
    }

    public void updateProgressBars() {
        for (Rune rune : Rune.values()) {
            bars.get(rune).setValue(runes.get(rune));
        }
    }

    public void hit(float damage, final PuzzleSystem puzzle) {
        if (Constants.UNDYING) return;
        for (Entity entity : puzzle.getEngine().getEntitiesFor(Family.all(StatusEffectComponent.class, DamageMultiplierComponent.class).get())) {
            StatusEffectComponent sc = Mappers.statusEffect.get(entity);
            DamageMultiplierComponent dc = Mappers.damageMultiplier.get(entity);

            if (sc.target != this) {
                damage *= dc.multiplier;
            }
        }

        runes.put(Rune.DAMAGE, Math.max(0, Math.min(runes.get(Rune.DAMAGE) - damage, maxRunes.get(Rune.DAMAGE))));
        bars.get(Rune.DAMAGE).setValue(runes.get(Rune.DAMAGE));

        Entity message = new Entity();
        MessageComponent mc = new MessageComponent((damage < 0 ? "+" : "-") + (int) Math.abs(damage));
        mc.color = damage < 0 ? Color.GREEN : Color.RED;
        mc.large = false;
        Vector2 coords = bars.get(Rune.EXP).localToStageCoordinates(new Vector2(bars.get(Rune.EXP).getX(), bars.get(Rune.EXP).getY()));
        coords.y += 100;
        coords.add(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
        mc.x = coords.x;
        mc.y = coords.y;
        message.add(mc);
        puzzle.getEngine().addEntity(message);

        if (runes.get(Rune.DAMAGE) < 1 && puzzle.turn != PuzzleSystem.NULL_FIGHTER) {
            puzzle.end(this);
        }
    }

    public void add(Entity spell) {
        spells.add(spell);
    }

    public void sub(SpellComponent sc) {
        for (Rune rune : Rune.values()) {
            if (rune == Rune.DAMAGE || rune == Rune.EXP) continue;
            runes.put(rune, Math.max(0, runes.get(rune) - sc.cost.get(rune)));
        }

        updateProgressBars();
        updateSpellCosts();
    }

    public void updateSpellCosts() {
        for (Entity spell : spells) {
            SpellComponent sc = Mappers.spell.get(spell);

            for (Rune rune : Rune.values()) {
                if (rune == Rune.DAMAGE || rune == Rune.EXP) continue;
                if (runes.get(rune) < sc.cost.get(rune)) sc.displays.get(rune).setDrawable(empty);
                else sc.displays.get(rune).setDrawable(filled);
            }
        }
    }

    public boolean canCast(Entity entity, PuzzleSystem puzzle) {
        SpellComponent sc = Mappers.spell.get(entity);

        if (!puzzle.isStable()) return false;
        if (puzzle.turn != this && !Constants.PLAYERLESS) return false;

        for (Rune rune : Rune.values()) {
            if (rune == Rune.DAMAGE || rune == Rune.EXP) continue;
            if (runes.get(rune) < sc.cost.get(rune)) return false;
        }

        return true;
    }

    public void cast(Entity entity, PuzzleSystem puzzle) {
        Entity spell = new Entity();
        for (Component component : entity.getComponents()) {
            spell.add(component);
        }
        spell.add(new PuzzleComponent(puzzle));
        spell.add(this); // caster
        puzzle.getEngine().addEntity(spell);
    }

    public void levelUp(final PuzzleSystem puzzle) {
        maxRunes.put(Rune.EXP, (float) (Constants.BASE_EXP * Math.pow(Constants.EXP_CURVE, level)));
        level++;
        skillPoints += Constants.POINTS_PER_LEVEL;
        if (bars.get(Rune.EXP) != null) {
            bars.get(Rune.EXP).setRange(0, maxRunes.get(Rune.EXP));
        }
        for (Entity spell : fighterClass.spells.keySet()) {
            if (knownSpells.contains(spell)) continue;
            if (fighterClass.spells.get(spell) > level) break; // I can do this because the spell list maintains order (its a LinkedHashMap)
            knownSpells.add(spell);
            if (spells.size() < Constants.MAX_SPELLS)
                spells.add(spell); // won't take place until next battle, since I'm not updating the spell list UI
            break;
        }

        for (Rune rune : Rune.values())
            if (fighterClass.proficiency.get(rune) != 0 && level % (Constants.PROFICIENCY_BONUS_FREQUENCY / fighterClass.proficiency.get(rune)) == 0)
                skills.put(rune, skills.get(rune) + 1);

        float oldMax = maxRunes.get(Rune.DAMAGE);
        maxRunes.put(Rune.DAMAGE, (float) (Constants.BASE_HEALTH * Math.pow(Constants.HEALTH_CURVE, skills.get(Rune.DAMAGE))));
        runes.put(Rune.DAMAGE, runes.get(Rune.DAMAGE) + maxRunes.get(Rune.DAMAGE) - oldMax);

        bars.get(Rune.DAMAGE).setValue(runes.get(Rune.DAMAGE));
        bars.get(Rune.DAMAGE).setRange(0, maxRunes.get(Rune.DAMAGE));

        if (bars.get(Rune.EXP) != null) {
            Entity message = new Entity();
            MessageComponent mc = new MessageComponent("Level up!");
            mc.color = Color.GREEN;
            mc.large = false;
            Vector2 coords = bars.get(Rune.EXP).localToStageCoordinates(new Vector2(bars.get(Rune.EXP).getX(), bars.get(Rune.EXP).getY()));
            mc.x = coords.x;
            mc.y = coords.y;
            message.add(mc);
            puzzle.getEngine().addEntity(message);
            Main.playSound("level.wav");
        }
    }

    public static FighterComponent getEnemy(Class fighterClass, int level) {
        FighterComponent fc = new FighterComponent(fighterClass, level);
        LinkedList<Entity> spells = new LinkedList<Entity>(fighterClass.spells.keySet());
        Iterator<Entity> iterator = spells.descendingIterator();
        while (iterator.hasNext()) {
            Entity spell = iterator.next();
            if (fighterClass.spells.get(spell) > level) continue;
            fc.knownSpells.add(spell);
            if (fc.spells.size() < Constants.MAX_SPELLS)
                fc.spells.add(spell);
            else break;
        }

        for (Rune rune : Rune.values()) {
            fc.skills.put(rune, Float.valueOf(fighterClass.proficiency.get(rune)));
        }

        for (int i = 0; i < level; i++) {
            for (Rune rune : Rune.values())
                if (fighterClass.proficiency.get(rune) != 0 && i % (Constants.PROFICIENCY_BONUS_FREQUENCY / fighterClass.proficiency.get(rune)) == 0) {
                    fc.skills.put(rune, fc.skills.get(rune) + 1);
                }
        }

        fc.skillPoints = level * Constants.POINTS_PER_LEVEL;
        while (fc.skillPoints > 0) { // TODO weighted average?
            Rune rune = Rune.randomRune(true);
            if (fc.skillPoints >= 5 - fighterClass.proficiency.get(rune)) {
                fc.skillPoints -= 5 - fighterClass.proficiency.get(rune);
                fc.skills.put(rune, fc.skills.get(rune) + 1);
            }
        }

        fc.runes.put(Rune.DAMAGE, (float) (Constants.BASE_HEALTH * Math.pow(Constants.HEALTH_CURVE, fc.skills.get(Rune.DAMAGE))));
        fc.maxRunes.put(Rune.DAMAGE, fc.runes.get(Rune.DAMAGE));
        return fc;
    }
}
