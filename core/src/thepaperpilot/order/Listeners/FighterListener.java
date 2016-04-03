package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class FighterListener implements EntityListener {
    private Engine engine;
    private Drawable circle = new Image(Main.getTexture("UICircleEmpty")).getDrawable();

    public FighterListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void entityAdded(final Entity entity) {
        final FighterComponent fc = Mappers.fighter.get(entity);
        UIComponent uc = Mappers.ui.get(entity);

        Table ui = new Table(Main.skin);
        ui.setFillParent(true);
        if (Mappers.playerControlled.has(entity)) ui.left().top();
        else ui.right().bottom();

        Table table = new Table(Main.skin);
        table.top().pad(20);
        Table left = new Table(Main.skin);
        Table portrait = new Table(Main.skin);
        portrait.add(new Image(Main.getTexture(fc.portrait))).expand().fill().pad(2);
        portrait.setBackground(Main.skin.getDrawable("default-round"));
        left.add(portrait).size(150, 200).padBottom(2).row();

        fc.experience = new ProgressBar(0, fc.maxExp, 1, false, Main.skin);
        fc.experience.setColor(Color.GREEN);
        fc.experience.setAnimateDuration(1f);
        Label exp = new Label("Exp:", Main.skin);
        exp.setColor(0, 1, 0, .75f);
        left.add(exp).left().padBottom(2).row();
        left.add(fc.experience).minWidth(1).expandX().fill().row();
        fc.experienceLabel = new Label((int) fc.exp + "/" + (int) fc.maxExp, Main.skin);
        fc.experienceLabel.setColor(0, 1, 0, 1);
        left.add(fc.experienceLabel).padTop(2).right();

        Table right = new Table(Main.skin);
        fc.healthBar = new ProgressBar(0, fc.maxHealth, 1, false, Main.skin);
        fc.healthBar.setValue(fc.health);
        fc.healthBar.setColor(Color.RED);
        fc.healthBar.setAnimateDuration(1f);
        Label hp = new Label("hp:", Main.skin);
        hp.setColor(1, 0, 0, .75f);
        right.add(hp).left().padBottom(2).colspan(5).row();
        right.add(fc.healthBar).minWidth(1).colspan(5).padBottom(2).expandX().fill().row();
        fc.healthLabel = new Label((int) fc.health + "/" + (int) fc.maxHealth, Main.skin);
        fc.healthLabel.setColor(1, 0, 0, 1);
        right.add(fc.healthLabel).padBottom(2).colspan(5).right().row();

        fc.poisonBar = new ProgressBar(0, fc.maxPoision, 1, true, Main.skin);
        fc.poisonBar.setColor(Color.PURPLE);
        fc.poisonBar.setAnimateDuration(1f);
        fc.poisonLabel = new Label("0", Main.skin);
        fc.poisonLabel.setColor(Color.PURPLE);
        fc.surpriseBar = new ProgressBar(0, fc.maxSurprise, 1, true, Main.skin);
        fc.surpriseBar.setColor(Color.YELLOW);
        fc.surpriseBar.setAnimateDuration(1f);
        fc.surpriseLabel = new Label("0", Main.skin);
        fc.surpriseLabel.setColor(Color.YELLOW);
        fc.mortalBar = new ProgressBar(0, fc.maxMortal, 1, true, Main.skin);
        fc.mortalBar.setColor(Color.FIREBRICK);
        fc.mortalBar.setAnimateDuration(1f);
        fc.mortalLabel = new Label("0", Main.skin);
        fc.mortalLabel.setColor(Color.FIREBRICK);
        fc.steamBar = new ProgressBar(0, fc.maxSteam, 1, true, Main.skin);
        fc.steamBar.setColor(Color.TEAL);
        fc.steamBar.setAnimateDuration(1f);
        fc.steamLabel = new Label("0", Main.skin);
        fc.steamLabel.setColor(Color.TEAL);
        fc.masonBar = new ProgressBar(0, fc.maxMason, 1, true, Main.skin);
        fc.masonBar.setColor(Color.LIME);
        fc.masonBar.setAnimateDuration(1f);
        fc.masonLabel = new Label("0", Main.skin);
        fc.masonLabel.setColor(Color.LIME);
        right.add(fc.poisonBar).minWidth(1).height(180).pad(2);
        right.add(fc.surpriseBar).minWidth(1).height(180).pad(2);
        right.add(fc.mortalBar).minWidth(1).height(180).pad(2);
        right.add(fc.steamBar).minWidth(1).height(180).pad(2);
        right.add(fc.masonBar).minWidth(1).height(180).pad(2).row();
        right.add(fc.poisonLabel);
        right.add(fc.surpriseLabel);
        right.add(fc.mortalLabel);
        right.add(fc.steamLabel);
        right.add(fc.masonLabel);

        table.add(left).padRight(4);
        table.add(right).expandX().fill().padBottom(2).row();
        for (final Entity spell : fc.spells) {
            final SpellComponent sc = Mappers.spell.get(spell);

            Table button = new Button(Main.skin);
            button.setBackground(Main.skin.getDrawable("default-round"));
            button.left().add(new Label(" " + sc.name, Main.skin)).expandY().top().pad(2).colspan(5).row();
            if (sc.poison != 0) button.add(createDisplay(sc.poison, sc.poisonDisplay, Color.PURPLE));
            if (sc.surprise != 0) button.add(createDisplay(sc.surprise, sc.surpriseDisplay, Color.YELLOW));
            if (sc.mortal != 0) button.add(createDisplay(sc.mortal, sc.mortalDisplay, Color.RED));
            if (sc.steam != 0) button.add(createDisplay(sc.steam, sc.steamDisplay, Color.TEAL));
            if (sc.mason != 0) button.add(createDisplay(sc.mason, sc.masonDisplay, Color.GREEN));
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    PuzzleSystem puzzle = engine.getSystem(PuzzleSystem.class);
                    if (!(fc.poison >= sc.poison && fc.surprise >= sc.surprise && fc.mortal >= sc.mortal && fc.steam >= sc.steam && fc.mason >= sc.mason && puzzle.isStable() && puzzle.turn != DestroyComponent.Target.PLAYER)) return;

                    Entity spellEntity = new Entity();
                    for (Component component : spell.getComponents()) {
                        spellEntity.add(component);
                    }
                    spellEntity.add(new PuzzleComponent(puzzle));
                    spellEntity.add(new CasterComponent(DestroyComponent.Target.PLAYER));
                    engine.addEntity(spellEntity);
                    puzzle.turn = DestroyComponent.Target.PLAYER;
                    puzzle.stableTimer = 0;
                    fc.sub(sc);
                    if (sc.poison != 0 && fc.poison < sc.poison) sc.poisonDisplay.setDrawable(circle);
                    if (sc.surprise != 0 && fc.surprise < sc.surprise) sc.surpriseDisplay.setDrawable(circle);
                    if (sc.mortal != 0 && fc.mortal < sc.mortal) sc.mortalDisplay.setDrawable(circle);
                    if (sc.steam != 0 && fc.steam < sc.steam) sc.steamDisplay.setDrawable(circle);
                    if (sc.mason != 0 && fc.mason < sc.mason) sc.masonDisplay.setDrawable(circle);
                }
            });
            table.add(button).expandX().fill().height(60).colspan(2).pad(2).row();
        }
        ui.add(table).expandY().fill().width(Constants.UI_WIDTH);
        uc.actor = ui;
    }

    private Actor createDisplay(int element, Image display, Color color) {
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
