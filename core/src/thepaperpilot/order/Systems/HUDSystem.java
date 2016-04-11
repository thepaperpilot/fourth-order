package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Listeners.FighterListener;
import thepaperpilot.order.Main;
import thepaperpilot.order.Player;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class HUDSystem extends EntitySystem {

    private TextButton skillsButton;
    private TextButton itemsButton;
    private TextButton spellsButton;

    private Entity skillsEntity;
    private Entity itemsEntity;
    private Entity spellsEntity;

    private Table skillsTable;
    private Table itemsTable;
    private Table spellsTable;

    public HUDSystem() {
        super(12);
    }

    public void addedToEngine(Engine engine) {
        Table table = new Table(Main.skin);
        table.setPosition(Constants.WORLD_WIDTH / 2, Constants.MAP_MARGIN, Align.center);
        skillsButton = new TextButton("Skills", Main.skin);
        itemsButton = new TextButton("Items", Main.skin);
        spellsButton = new TextButton("Spells", Main.skin);
        table.add(skillsButton).uniform().fill().padRight(4);
        table.add(itemsButton).uniform().fill().padRight(4);
        table.add(spellsButton).uniform().fill();

        engine.addEntity(skillsEntity = getSkillsEntity(skillsButton));
        engine.addEntity(itemsEntity = getItemsEntity(itemsButton));
        engine.addEntity(spellsEntity = getSpellsEntity(spellsButton));

        Entity entity = new Entity();
        ActorComponent ac = new ActorComponent();
        ac.actor = table;
        entity.add(ac);
        engine.addEntity(entity);
    }

    public Entity getSkillsEntity(TextButton skillsButton) {
        Table table = skillsTable = getTable(skillsButton);

        final FighterComponent fc = Player.getPlayer();
        table.add(new Image(Main.getTexture(fc.portrait))).width(Constants.FACE_SIZE).pad(8);
        Table right = new Table(Main.skin);
        Label fighterClass = new Label(fc.fighterClass.name(), Main.skin);
        fighterClass.setAlignment(Align.center);
        right.add(fighterClass).padBottom(4).row();
        Player.getPlayer().reset();
        ProgressBar healthBar = new ProgressBar(0, fc.maxRunes.get(Rune.DAMAGE), 1, false, Main.skin);
        healthBar.setValue(fc.maxRunes.get(Rune.DAMAGE));
        healthBar.setColor(Color.RED);
        healthBar.setAnimateDuration(1f);
        Label hp = new Label("hp:", Main.skin);
        hp.setColor(1, 0, 0, .75f);
        right.add(hp).left().padBottom(2).colspan(5).row();
        right.add(healthBar).padBottom(2).expandX().fill().row();
        Label healthLabel = new Label(fc.maxRunes.get(Rune.DAMAGE).intValue() + "/" + fc.maxRunes.get(Rune.DAMAGE).intValue(), Main.skin);
        healthLabel.setColor(1, 0, 0, 1);
        right.add(healthLabel).padBottom(2).colspan(5).row();
        ProgressBar experience = new ProgressBar(0, fc.maxRunes.get(Rune.EXP), 1, false, Main.skin);
        experience.setValue(fc.runes.get(Rune.EXP));
        experience.setColor(Color.GREEN);
        experience.setAnimateDuration(1f);
        Label exp = new Label("Exp:", Main.skin);
        exp.setColor(0, 1, 0, .75f);
        right.add(exp).left().padBottom(2).row();
        right.add(experience).expandX().fill().row();
        Label experienceLabel = new Label(fc.runes.get(Rune.EXP).intValue() + "/" + fc.maxRunes.get(Rune.EXP).intValue(), Main.skin);
        experienceLabel.setColor(0, 1, 0, 1);
        right.add(experienceLabel).padTop(2);
        table.add(right).padBottom(16).row();

        Table skillPoints = new Table(Main.skin);
        skillPoints.add("Skill points remaining: ");
        final Label skillLabel = new Label("" + fc.skillPoints, Main.skin);
        skillPoints.add(skillLabel);
        table.add(skillPoints).padBottom(4).colspan(2).row();
        Table skillTable = new Table(Main.skin);
        for (final Rune rune : Rune.values()) {
            TextButton button = new TextButton("+", Main.skin);
            final Label skill = new Label("" + fc.skills.get(rune).intValue(), Main.skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (fc.skillPoints >= 5 - fc.fighterClass.proficiency.get(rune)) {
                        fc.skillPoints -= 5 - fc.fighterClass.proficiency.get(rune);
                        fc.skills.put(rune, fc.skills.get(rune) + 1);
                        skill.setText("" + fc.skills.get(rune).intValue());
                        skillLabel.setText("" + fc.skillPoints);
                        Player.save();
                    }
                }
            });
            skillTable.add(button).padRight(8);
            skillTable.add(rune.skill).left().padRight(8);
            skillTable.add(skill).padRight(8);
            skillTable.add("(cost: " + (5 - fc.fighterClass.proficiency.get(rune)) + ")").row();
        }
        table.add(skillTable).colspan(2).padBottom(4).row();
        // I really want a radar graph, but I have no idea how best to render it

        Entity skillsEntity = new Entity();
        ActorComponent skillsAC = new ActorComponent();
        skillsAC.actor = table;
        skillsEntity.add(skillsAC);
        return skillsEntity;
    }

    public Entity getItemsEntity(TextButton itemsButton) {
        Table table = itemsTable = getTable(itemsButton);

        table.add("Not Yet Implemented");

        Entity skillsEntity = new Entity();
        ActorComponent skillsAC = new ActorComponent();
        skillsAC.actor = table;
        skillsEntity.add(skillsAC);
        return skillsEntity;
    }

    public Entity getSpellsEntity(TextButton spellsButton) {
        Table table = spellsTable = getTable(spellsButton);

        final Table left = new Table(Main.skin);
        left.add("Spell List").padBottom(8).row();
        for (final Entity spell : Player.getPlayer().spells) {
            addSpell(left, spell);
        }
        Table right = new Table(Main.skin);
        right.add("Known Spells").padBottom(8).row();
        for (final Entity spell : Player.getPlayer().knownSpells) {
            final Table spellTable = FighterListener.createSpellTable(getEngine(), SpellComponent.getSpell(Mappers.spell.get(spell).name));
            right.add(spellTable).expandX().fill().padBottom(4).row();
            spellTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (Player.getPlayer().spells.size() >= Constants.MAX_SPELLS) return;
                    for (Entity aSpell : Player.getPlayer().spells) {
                        if (Mappers.spell.get(aSpell).name.equals(Mappers.spell.get(spell).name)) return;
                    }
                    Player.getPlayer().spells.add(spell);
                    addSpell(left, spell);
                    Player.save();
                }
            });
        }
        ScrollPane scroll = new ScrollPane(right);
        table.top().pad(20).add(left).width(200).padRight(20).top();
        table.add(scroll).width(200).top();

        Entity skillsEntity = new Entity();
        ActorComponent skillsAC = new ActorComponent();
        skillsAC.actor = table;
        skillsEntity.add(skillsAC);
        return skillsEntity;
    }

    private void addSpell(final Table table, final Entity spell) {
        final Table spellTable = FighterListener.createSpellTable(getEngine(), SpellComponent.getSpell(Mappers.spell.get(spell).name));
        table.add(spellTable).expandX().fill().padBottom(4).row();
        spellTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Player.getPlayer().spells.remove(spell);
                table.getCell(spellTable).padBottom(0);
                table.removeActor(spellTable);
                Player.save();
            }
        });
    }

    public Table getTable(Button button) {
        final Table table = new Table(Main.skin);
        table.align(Align.center);
        table.setBackground(Main.skin.getDrawable("default-round"));
        table.setPosition(Constants.MAP_MARGIN * 2, Constants.MAP_MARGIN * -2, Align.center);
        table.setSize(Constants.WORLD_WIDTH - Constants.MAP_MARGIN * 4, Constants.WORLD_HEIGHT - Constants.MAP_MARGIN * 4);
        table.getColor().a = 0;
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skillsTable.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(Constants.MAP_MARGIN * 2, Constants.MAP_MARGIN * -2, .25f, Interpolation.pow2), Actions.fadeOut(.25f, Interpolation.pow2))));
                itemsTable.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(Constants.MAP_MARGIN * 2, Constants.MAP_MARGIN * -2, .25f, Interpolation.pow2), Actions.fadeOut(.25f, Interpolation.pow2))));
                spellsTable.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(Constants.MAP_MARGIN * 2, Constants.MAP_MARGIN * -2, .25f, Interpolation.pow2), Actions.fadeOut(.25f, Interpolation.pow2))));

                table.clearActions();
                table.toFront();
                if (table.getColor().a == 0) {
                    table.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(Constants.MAP_MARGIN * 2, Constants.MAP_MARGIN * 2, .25f, Interpolation.pow2Out), Actions.fadeIn(.25f, Interpolation.pow2Out))));
                } else {
                    table.addAction(Actions.sequence(Actions.parallel(Actions.moveTo(Constants.MAP_MARGIN * 2, Constants.MAP_MARGIN * -2, .25f, Interpolation.pow2), Actions.fadeOut(.25f, Interpolation.pow2))));
                }
            }
        });
        return table;
    }

    public void update() {
        getEngine().removeEntity(skillsEntity);
        getEngine().removeEntity(itemsEntity);
        getEngine().removeEntity(spellsEntity);

        getEngine().addEntity(skillsEntity = getSkillsEntity(skillsButton));
        getEngine().addEntity(itemsEntity = getItemsEntity(itemsButton));
        getEngine().addEntity(spellsEntity = getSpellsEntity(spellsButton));
    }
}
