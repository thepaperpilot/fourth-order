package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Player;
import thepaperpilot.order.Util.Constants;

public class HUDSystem extends EntitySystem {

    public HUDSystem() {
        super(12);
    }

    public void addedToEngine(Engine engine) {
        Table table = new Table(Main.skin);
        table.setPosition(Constants.WORLD_WIDTH / 2, Constants.MAP_MARGIN, Align.center);
        TextButton skillsButton = new TextButton("Skills", Main.skin);
        TextButton itemsButton = new TextButton("Items", Main.skin);
        TextButton spellsButton = new TextButton("Spells", Main.skin);
        table.add(skillsButton).uniform().fill().padRight(4);
        table.add(itemsButton).uniform().fill().padRight(4);
        table.add(spellsButton).uniform().fill();

        engine.addEntity(getSkillsEntity(skillsButton));
        engine.addEntity(getItemsEntity(itemsButton));
        engine.addEntity(getSpellsEntity(spellsButton));

        Entity entity = new Entity();
        ActorComponent ac = new ActorComponent();
        ac.actor = table;
        entity.add(ac);
        engine.addEntity(entity);
    }

    public Entity getSkillsEntity(TextButton skillsButton) {
        Table table = getTable(skillsButton);

        FighterComponent fc = Player.getPlayer();
        table.add(new Image(Main.getTexture(fc.portrait))).left().top().expand().pad(8);
        // TODO this is in the middle of implementation
        // I really want a radar graph, but I have no idea how best to render it
        // also I need to decide when to update this information. show()?

        Entity skillsEntity = new Entity();
        ActorComponent skillsAC = new ActorComponent();
        skillsAC.actor = table;
        skillsEntity.add(skillsAC);
        return skillsEntity;
    }

    public Entity getItemsEntity(TextButton itemsButton) {
        Table table = getTable(itemsButton);

        table.add("Not Yet Implemented");

        Entity skillsEntity = new Entity();
        ActorComponent skillsAC = new ActorComponent();
        skillsAC.actor = table;
        skillsEntity.add(skillsAC);
        return skillsEntity;
    }

    public Entity getSpellsEntity(TextButton spellsButton) {
        Table table = getTable(spellsButton);

        table.add("Not Yet Implemented");

        Entity skillsEntity = new Entity();
        ActorComponent skillsAC = new ActorComponent();
        skillsAC.actor = table;
        skillsEntity.add(skillsAC);
        return skillsEntity;
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
}
