package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Mappers;

import java.util.ArrayList;

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
    public float maxExp = 20;
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

    public void add(RuneComponent rc) {
        poison = Math.min(maxPoision, poison + rc.poison);
        surprise = Math.min(maxSurprise, surprise + rc.surprise);
        mortal = Math.min(maxMortal, mortal + rc.mortal);
        steam = Math.min(maxSteam, steam + rc.steam);
        mason = Math.min(maxMason, mason + rc.mason);
        exp = Math.min(maxExp, exp + rc.exp);

        updateProgressBars();

        // TODO implement leveling up
    }

    private void updateProgressBars() {
        poisonBar.setValue(poison);
        surpriseBar.setValue(surprise);
        mortalBar.setValue(mortal);
        steamBar.setValue(steam);
        masonBar.setValue(mason);
        experience.setValue(exp);
    }

    public void hit(float damage) {
        health = Math.max(0, health - damage);

        healthBar.setValue(health);

        // TODO implement dying
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
        if (puzzle.turn != this) return false;

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
}
