package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RenderStageSystem extends EntitySystem {
    public Stage stage;

    public RenderStageSystem(Stage stage) {
        super(10);
        this.stage = stage;
    }

    public void update (float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }
}
