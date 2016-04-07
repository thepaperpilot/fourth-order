package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Constants;

public class MapButtonsSystem extends EntitySystem {
    ShapeRenderer tempMapRenderer = new ShapeRenderer();
    Stage stage;

    public MapButtonsSystem(Stage stage) {
        this.stage = stage;
    }

    public void addedToEngine (Engine engine) {
        Table table = new Table(Main.skin);
        table.setPosition(Constants.WORLD_WIDTH / 2, Constants.MAP_MARGIN, Align.center);
        table.add(new TextButton("Skills", Main.skin)).uniform().fill().padRight(4);
        table.add(new TextButton("Items", Main.skin)).uniform().fill().padRight(4);
        table.add(new TextButton("Spells", Main.skin)).uniform().fill();

        Entity entity = new Entity();
        ActorComponent ac = new ActorComponent();
        ac.actor = table;
        entity.add(ac);
        engine.addEntity(entity);
    }

    public void update (float deltaTime) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        tempMapRenderer.setProjectionMatrix(stage.getCamera().combined);
        tempMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
        tempMapRenderer.setColor(Color.TAN.r, Color.TAN.g, Color.TAN.b, stage.getRoot().getColor().a);
        tempMapRenderer.rect(Constants.MAP_MARGIN, Constants.MAP_MARGIN, Constants.WORLD_WIDTH - 2 * Constants.MAP_MARGIN, Constants.WORLD_HEIGHT - 2 * Constants.MAP_MARGIN);
        tempMapRenderer.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, stage.getRoot().getColor().a);
        tempMapRenderer.line(Constants.MAP_MARGIN, Constants.MAP_MARGIN, Constants.WORLD_WIDTH - Constants.MAP_MARGIN, Constants.MAP_MARGIN);
        tempMapRenderer.line(Constants.MAP_MARGIN, Constants.MAP_MARGIN, Constants.MAP_MARGIN, Constants.WORLD_HEIGHT - Constants.MAP_MARGIN);
        tempMapRenderer.line(Constants.WORLD_WIDTH - Constants.MAP_MARGIN, Constants.MAP_MARGIN, Constants.WORLD_WIDTH - Constants.MAP_MARGIN, Constants.WORLD_HEIGHT - Constants.MAP_MARGIN);
        tempMapRenderer.line(Constants.MAP_MARGIN, Constants.WORLD_HEIGHT - Constants.MAP_MARGIN, Constants.WORLD_WIDTH - Constants.MAP_MARGIN, Constants.WORLD_HEIGHT - Constants.MAP_MARGIN);
        tempMapRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
