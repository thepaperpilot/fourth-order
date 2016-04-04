package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thepaperpilot.order.Components.ScreenShakeComponent;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class ScreenShakeSystem extends IteratingSystem {
    private Stage stage;
    float shake;

    public ScreenShakeSystem(Stage stage) {
        super(Family.all(ScreenShakeComponent.class).get(), 9);
        this.stage = stage;
    }

    public void update (float deltaTime) {
        shake = 0;
        super.update(deltaTime);

        stage.getCamera().position.set(Constants.WORLD_WIDTH / 2f + MathUtils.random(-shake, shake), Constants.WORLD_HEIGHT / 2f + MathUtils.random(-shake, shake), 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScreenShakeComponent sc = Mappers.screenShake.get(entity);

        shake += sc.shake;

        sc.shake /= 1.5f;
        if (sc.shake < 1) {
            entity.remove(ScreenShakeComponent.class);
        }
    }
}
