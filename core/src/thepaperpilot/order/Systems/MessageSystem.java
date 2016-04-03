package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.MessageComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class MessageSystem extends IteratingSystem {

    private Stage stage;

    public MessageSystem(Stage stage) {
        super(Family.all(MessageComponent.class).get(), 15);
        this.stage = stage;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MessageComponent mc = Mappers.message.get(entity);

        final Label message = new Label(mc.message, Main.skin, "large");
        message.setFontScale(2);
        message.setOrigin(Align.center);
        message.setAlignment(Align.center);
        message.setPosition(mc.x, mc.y);
        message.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 400, 2 * Constants.RUNE_EXIT_SPEED, Interpolation.pow2),
                        Actions.fadeOut(2 * Constants.RUNE_EXIT_SPEED, Interpolation.pow2)),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        message.remove();
                    }
                })
        ));
        stage.addActor(message);
        getEngine().removeEntity(entity);
    }
}
