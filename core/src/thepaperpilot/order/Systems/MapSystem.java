package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thepaperpilot.order.Util.Constants;

public class MapSystem extends EntitySystem {
    ShapeRenderer tempMapRenderer = new ShapeRenderer();
    Stage stage;

    public MapSystem(Stage stage) {
        super(9);
        this.stage = stage;
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
