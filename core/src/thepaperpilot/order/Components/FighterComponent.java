package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Dialogue;
import thepaperpilot.order.DialogueScreen;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

import java.util.ArrayList;
import java.util.Iterator;

public class FighterComponent implements Component {
    Drawable empty = new Image(Main.getTexture("UICircleEmpty")).getDrawable();

    public String portrait = "PortraitPlayer";

    public float exp = 0;
    public float health = 10;
    public float poison = 0;
    public float surprise = 0;
    public float mortal = 0;
    public float steam = 0;
    public float mason = 0;
    public float maxExp = 4;
    public float maxHealth = 10;
    public float maxPoision = 20;
    public float maxSurprise = 20;
    public float maxMortal = 20;
    public float maxSteam = 20;
    public float maxMason = 20;
    public ArrayList<Entity> spells = new ArrayList<Entity>();

    public ProgressBar experience;
    public ProgressBar healthBar;
    public ProgressBar poisonBar;
    public ProgressBar surpriseBar;
    public ProgressBar mortalBar;
    public ProgressBar steamBar;
    public ProgressBar masonBar;

    public Label experienceLabel;
    public Label healthLabel;
    public Label poisonLabel;
    public Label surpriseLabel;
    public Label mortalLabel;
    public Label steamLabel;
    public Label masonLabel;

    public DialogueComponent victory;
    public DialogueComponent loss;

    public void add(RuneComponent rc, PuzzleSystem puzzle) {
        poison = Math.min(maxPoision, poison + rc.poison);
        surprise = Math.min(maxSurprise, surprise + rc.surprise);
        mortal = Math.min(maxMortal, mortal + rc.mortal);
        steam = Math.min(maxSteam, steam + rc.steam);
        mason = Math.min(maxMason, mason + rc.mason);
        exp = Math.min(maxExp, exp + rc.exp);

        updateProgressBars();

        if (exp == maxExp) {
            levelUp(puzzle);
        }
    }

    public void updateProgressBars() {
        poisonBar.setValue(poison);
        surpriseBar.setValue(surprise);
        mortalBar.setValue(mortal);
        steamBar.setValue(steam);
        masonBar.setValue(mason);
        experience.setValue(exp);
    }

    public void hit(float damage, PuzzleSystem puzzle) {
        if (Constants.UNDYING) return;
        for (Entity entity : puzzle.getEngine().getEntitiesFor(Family.all(StatusEffectComponent.class, DamageMultiplierComponent.class).get())) {
            StatusEffectComponent sc = Mappers.statusEffect.get(entity);
            DamageMultiplierComponent dc = Mappers.damageMultiplier.get(entity);

            if (sc.target != this) {
                damage *= dc.multiplier;
            }
        }

        health = Math.max(0, health - damage);

        healthBar.setValue(health);

        Entity message = new Entity();
        MessageComponent mc = new MessageComponent("-" + (int) damage);
        mc.color = Color.RED;
        mc.large = false;
        Vector2 coords = experience.localToStageCoordinates(new Vector2(experience.getX(), experience.getY()));
        coords.y += 100;
        coords.add(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
        mc.x = coords.x;
        mc.y = coords.y;
        message.add(mc);
        puzzle.getEngine().addEntity(message);

        if (health == 0) {
            message = new Entity();
            if (puzzle.enemy == this) {
                message.add(new MessageComponent("[GOLD]You Are Victorious"));
                if (victory != null) puzzle.transition(new DialogueScreen(victory));
                else puzzle.transition(Main.instance);
            } else if (puzzle.player == this) {
                message.add(new MessageComponent("[FIREBRICK]You Have Been Defeated"));
                if (puzzle.enemy.loss != null) puzzle.transition(new DialogueScreen(puzzle.enemy.loss));
                else puzzle.transition(Main.instance);
            }
            puzzle.getEngine().addEntity(message);
        }
    }

    public void add(Entity spell) {
        spells.add(spell);

        // TODO sub to existing spell sheet?
    }

    public void sub(SpellComponent sc) {
        poison = Math.max(0, poison - sc.poison);
        surprise = Math.max(0, surprise - sc.surprise);
        mortal = Math.max(0, mortal - sc.mortal);
        steam = Math.max(0, steam - sc.steam);
        mason = Math.max(0, mason - sc.mason);

        updateProgressBars();
        updateSpellCosts();
    }

    private void updateSpellCosts() {
        for (Entity spell : spells) {
            SpellComponent sc = Mappers.spell.get(spell);

            if (sc.poison != 0 && poison < sc.poison) sc.poisonDisplay.setDrawable(empty);
            if (sc.surprise != 0 && surprise < sc.surprise) sc.surpriseDisplay.setDrawable(empty);
            if (sc.mortal != 0 && mortal < sc.mortal) sc.mortalDisplay.setDrawable(empty);
            if (sc.steam != 0 && steam < sc.steam) sc.steamDisplay.setDrawable(empty);
            if (sc.mason != 0 && mason < sc.mason) sc.masonDisplay.setDrawable(empty);
        }
    }

    public boolean canCast(Entity entity, PuzzleSystem puzzle) {
        SpellComponent sc = Mappers.spell.get(entity);

        if (poison < sc.poison) return false;
        if (surprise < sc.surprise) return false;
        if (mortal < sc.mortal) return false;
        if (steam < sc.steam) return false;
        if (mason < sc.mason) return false;

        if (!puzzle.isStable()) return false;
        if (puzzle.turn != this && !Constants.PLAYERLESS) return false;
        // TODO find someway to check the spells themselves if they are castable

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
        puzzle.takeTurn(this);
        sub(Mappers.spell.get(entity));
    }

    public void levelUp(final PuzzleSystem puzzle) {
        exp -= maxExp;
        maxExp *= 2;
        experience.setValue(exp);
        experience.setRange(0, maxExp);

        // this is all arbitrary atm
        health += 1;
        maxHealth += 1;
        healthBar.setValue(health);
        healthBar.setRange(0, maxHealth);
        // TODO give player choice to increase stats of some sort, doing things like increasing damage, mana collection, or mana storage

        Entity message = new Entity();
        MessageComponent mc = new MessageComponent("Level up!");
        mc.color = Color.GREEN;
        mc.large = false;
        Vector2 coords = experience.localToStageCoordinates(new Vector2(experience.getX(), experience.getY()));
        mc.x = coords.x;
        mc.y = coords.y;
        message.add(mc);
        puzzle.getEngine().addEntity(message);

        if (puzzle.player == this) {
            final ArrayList<Entity> spells = new ArrayList<Entity>();
            String classSpell = Mappers.spell.get(this.spells.get(1)).name;
            spells.add(SpellComponent.getRefreshSpell());
            if (classSpell.equals("Truth")) { //alchemist

            } else if (classSpell.equals("Condense")) { //rogue

            } else if (classSpell.equals("Antidote")) { //ranger
                spells.add(SpellComponent.getCommandSpell());
            } else if (classSpell.equals("Immortality")) { //paladin

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
            DialogueScreen ds = new DialogueScreen(dc);
            dc.lines = new Dialogue.Line[1];
            dc.lines[0] = new Dialogue.Line("I can learn a new spell, sweet! But which one?");
            dc.lines[0].options = new Dialogue.Option[spells.size()];
            for (int i = 0; i < spells.size(); i++) {
                final Entity spell = spells.get(i);
                SpellComponent sc = Mappers.spell.get(spell);
                dc.lines[0].options[i] = new Dialogue.Option(sc.name);
                dc.lines[0].options[i].events = new Runnable() {
                    @Override
                    public void run() {
                        FighterComponent.this.spells.add(spell);
                    }
                };
            }
            dc.lines[0].events = new Runnable() {
                @Override
                public void run() {
                    Main.changeScreen(screen);
                    puzzle.turn = temp;
                    puzzle.updateFighters();
                }
            };
            puzzle.transition(ds);
        }
    }
}
