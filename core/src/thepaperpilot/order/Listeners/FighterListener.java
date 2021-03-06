package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class FighterListener implements EntityListener {
    private Engine engine;
    private static Drawable circle = new Image(Main.getTexture("UICircleEmpty")).getDrawable();

    public FighterListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void entityAdded(final Entity entity) {
        final FighterComponent fc = Mappers.fighter.get(entity);
        ActorComponent ac = Mappers.actor.get(entity);

        Table table = new Table(Main.skin);
        ac.actor = table;
        table.top().pad(20);
        table.setSize(Constants.UI_WIDTH, Constants.WORLD_HEIGHT);
        if (Mappers.playerControlled.has(entity)) table.setPosition(0, 0);
        else table.setPosition(Constants.WORLD_WIDTH - Constants.UI_WIDTH, 0);
        Table left = new Table(Main.skin);
        Table portrait = new Table(Main.skin);
        portrait.add(new Image(Main.getTexture(fc.portrait))).expand().fill().pad(2);
        portrait.setBackground(Main.skin.getDrawable("default-round"));
        left.add(portrait).size(150, 200).padBottom(2).row();

        ProgressBar experience = new ProgressBar(0, fc.maxRunes.get(Rune.EXP), 1, false, Main.skin);
        experience.setColor(Color.GREEN);
        experience.setAnimateDuration(1f);
        Label exp = new Label("Exp:", Main.skin);
        exp.setColor(0, 1, 0, .75f);
        left.add(exp).left().padBottom(2).row();
        left.add(experience).minWidth(1).expandX().fill().row();
        Label experienceLabel = new Label(fc.runes.get(Rune.EXP).intValue() + "/" + fc.maxRunes.get(Rune.EXP).intValue(), Main.skin);
        experienceLabel.setColor(0, 1, 0, 1);
        left.add(experienceLabel).padTop(2).right();
        fc.bars.put(Rune.EXP, experience);
        fc.labels.put(Rune.EXP, experienceLabel);

        Table right = new Table(Main.skin);
        ProgressBar healthBar = new ProgressBar(0, fc.maxRunes.get(Rune.DAMAGE), 1, false, Main.skin);
        healthBar.setValue(fc.runes.get(Rune.DAMAGE));
        healthBar.setColor(Color.RED);
        healthBar.setAnimateDuration(1f);
        Label hp = new Label("hp:", Main.skin);
        hp.setColor(1, 0, 0, .75f);
        right.add(hp).left().padBottom(2).colspan(5).row();
        right.add(healthBar).minWidth(1).colspan(5).padBottom(2).expandX().fill().row();
        Label healthLabel = new Label(fc.runes.get(Rune.DAMAGE).intValue() + "/" + fc.maxRunes.get(Rune.DAMAGE).intValue(), Main.skin);
        healthLabel.setColor(1, 0, 0, 1);
        right.add(healthLabel).padBottom(2).colspan(5).right().row();
        fc.bars.put(Rune.DAMAGE, healthBar);
        fc.labels.put(Rune.DAMAGE, healthLabel);

        for (Rune rune : Rune.elementRunes) {
            ProgressBar bar = new ProgressBar(0, fc.maxRunes.get(rune), 1, true, Main.skin);
            bar.setColor(rune.color);
            bar.setAnimateDuration(1f);
            fc.bars.put(rune, bar);

            Label label = new Label("0", Main.skin);
            label.setColor(rune.color);
            fc.labels.put(rune, label);
        }
        fc.updateProgressBars();
        for (Rune rune : Rune.elementRunes) {
            right.add(fc.bars.get(rune)).height(180).pad(2).width(16);
        }
        right.row();
        for (Rune rune : Rune.elementRunes) {
            right.add(fc.labels.get(rune));
        }

        table.add(left).padRight(4);
        table.add(right).expandX().fill().padBottom(2).row();
        for (final Entity spell : fc.spells) {
            createSpell(engine, entity, spell);
        }
    }

    public static void createSpell(final Engine engine, Entity fighter, final Entity spell) {
        final FighterComponent fc = Mappers.fighter.get(fighter);
        ActorComponent ac = Mappers.actor.get(fighter);

        Table button = createSpellTable(spell);
        if (engine.getSystem(PuzzleSystem.class).player == fc) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (fc.canCast(spell, engine.getSystem(PuzzleSystem.class))) {
                        fc.cast(spell, engine.getSystem(PuzzleSystem.class));
                    }
                }
            });
        }
        ((Table) ac.actor).add(button).expandX().fill().height(60).colspan(2).pad(2).row();
    }

    public static Table createSpellTable(final Entity spell) {
        final SpellComponent sc = Mappers.spell.get(spell);

        Table table = new Button(Main.skin);
        table.setBackground(Main.skin.getDrawable("default-round"));
        table.left().add(new Label(" " + sc.name, Main.skin)).expandY().top().pad(2).colspan(5).row();
        for (Rune rune : Rune.elementRunes) {
            if (sc.cost.get(rune) != 0) table.add(createDisplay(sc.cost.get(rune).intValue(), sc.displays.get(rune), rune.color)).expandX();
        }
        return table;
    }

    private static Actor createDisplay(int element, Image display, Color color) {
        Table table = new Table(Main.skin);
        display.setDrawable(circle);
        display.setColor(color);
        table.add(display).padBottom(2).row();
        Label label = new Label("" + element, Main.skin);
        label.setColor(color);
        table.add(label).padBottom(2);
        return table;
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
