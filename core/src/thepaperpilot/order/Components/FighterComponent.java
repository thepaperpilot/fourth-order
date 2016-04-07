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
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Player;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class FighterComponent implements Component {
    Drawable empty = new Image(Main.getTexture("UICircleEmpty")).getDrawable();
    Drawable filled = new Image(Main.getTexture("UICircleFilled")).getDrawable();

    public String portrait = "PortraitPlayer";

    public Map<Rune, Float> runes = new EnumMap<Rune, Float>(Rune.class);
    public Map<Rune, Float> maxRunes = new EnumMap<Rune, Float>(Rune.class);
    public Map<Rune, ProgressBar> bars = new EnumMap<Rune, ProgressBar>(Rune.class);
    public Map<Rune, Label> labels = new EnumMap<Rune, Label>(Rune.class);
    public ArrayList<Entity> spells = new ArrayList<Entity>();
    public int level = 1;

    public String victory;
    public String defeat;

    public FighterComponent() {
        // TODO skills
        for (Rune rune : Rune.values()) {
            switch (rune) {
                case DAMAGE:
                    runes.put(rune, 20f);
                    maxRunes.put(rune, 20f);
                    break;
                case EXP:
                    runes.put(rune, 0f);
                    maxRunes.put(rune, 7f);
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
                    runes.put(rune, maxRunes.get(rune));
                    break;
                case EXP:
                    // don't reset experience
                    maxRunes.put(rune, 7f);
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

        if (runes.get(Rune.DAMAGE) <= 0 && puzzle.turn != PuzzleSystem.NULL_FIGHTER) {
            message = new Entity();
            if (puzzle.enemy == this) {
                Main.playSound("victory.wav");
                message.add(new MessageComponent("[GOLD]You Are Victorious"));
                if (victory != null) {
                    Entity entity = new Entity();
                    DialogueComponent dc = DialogueComponent.read("dialogue/victory.json");
                    dc.start = victory;
                    entity.add(dc);
                    entity.add(new ActorComponent());
                    puzzle.returnScreen.engine.addEntity(entity);
                }
            } else if (puzzle.player == this) {
                Main.playSound("lose.wav");
                message.add(new MessageComponent("[FIREBRICK]You Have Been Defeated"));
                if (puzzle.enemy.defeat != null) {
                    Entity entity = new Entity();
                    DialogueComponent dc = DialogueComponent.read("dialogue/defeat.json");
                    dc.start = puzzle.enemy.defeat;
                    entity.add(dc);
                    entity.add(new ActorComponent());
                    puzzle.returnScreen.engine.addEntity(entity);
                }
            }
            puzzle.getEngine().addEntity(message);
            puzzle.transition(puzzle.returnScreen);
            Player.save();
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
        runes.put(Rune.EXP, runes.get(Rune.EXP) - maxRunes.get(Rune.EXP));
        maxRunes.put(Rune.EXP, maxRunes.get(Rune.EXP) * 2);
        if (bars.get(Rune.EXP) != null) {
            bars.get(Rune.EXP).setValue(runes.get(Rune.EXP));
            bars.get(Rune.EXP).setRange(0, maxRunes.get(Rune.EXP));
        }

        // this is all arbitrary atm
        runes.put(Rune.DAMAGE, runes.get(Rune.DAMAGE) + 2);
        maxRunes.put(Rune.DAMAGE, maxRunes.get(Rune.DAMAGE) + 2);
        if (bars.get(Rune.DAMAGE) != null) {
            bars.get(Rune.DAMAGE).setValue(runes.get(Rune.DAMAGE));
            bars.get(Rune.DAMAGE).setRange(0, maxRunes.get(Rune.DAMAGE));
        }
        level++;

        if (bars.get(Rune.EXP) != null) {
            // TODO revamp level system (and level up after battle, with no max exp
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

        /*if (puzzle.player == this) {
            final ArrayList<Entity> spells = new ArrayList<Entity>();
            String classSpell = Mappers.spell.get(this.spells.get(1)).name;
            spells.add(SpellComponent.getRefreshSpell());
            if (classSpell.equals("Truth")) { //alchemist
                spells.add(SpellComponent.getWitherSpell());
            } else if (classSpell.equals("Condense")) { //rogue
                spells.add(SpellComponent.getCollectSpell());
            } else if (classSpell.equals("Antidote")) { //ranger
                spells.add(SpellComponent.getCommandSpell());
            } else if (classSpell.equals("Immortality")) { //paladin
                spells.add(SpellComponent.getSustainSpell());
            } else if (classSpell.equals("Premonition")) { //wizard

            }

            for (Iterator<Entity> iterator = spells.iterator(); iterator.hasNext(); ) {
                Entity spell = iterator.next();
                String name = Mappers.spell.get(spell).name;

                for (Entity knownSpell : this.spells) {
                    if (Mappers.spell.get(knownSpell).name.equals(name))
                        iterator.remove();
                }
            }

            if (spells.isEmpty()) return;

            final FighterComponent temp = puzzle.turn;
            puzzle.turn = PuzzleSystem.NULL_FIGHTER;
            final Screen screen = Main.instance.getScreen();
            DialogueComponent dc = new DialogueComponent();
            IntroScreen ds = new IntroScreen();
            dc.start = "first";
            Line first = new Line("I can learn a new spell, sweet! But which one?");
            first.options = new Option[spells.size()];
            for (int i = 0; i < spells.size(); i++) {
                final Entity spell = spells.get(i);
                SpellComponent sc = Mappers.spell.get(spell);
                first.options[i] = new Option(sc.name);
                first.options[i].event = sc.name;
                dc.events.put(sc.name, new Runnable() {
                    @Override
                    public void run() {
                        FighterComponent.this.spells.add(spell);
                    }
                });
            }
            first.event = "end";
            dc.events.put("end", new Runnable() {
                @Override
                public void run() {
                    Main.changeScreen(screen);
                    puzzle.turn = temp;
                    puzzle.updateFighters();
                }
            });
            dc.lines.put("first", first);
            Entity dialogue = new Entity();
            dialogue.add(dc);
            dialogue.add(new ActorComponent());
            ds.engine.addEntity(dialogue);
            puzzle.transition(ds);
        }*/
    }
}
