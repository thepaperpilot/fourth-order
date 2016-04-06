package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Util.Mappers;

public class UIListener implements EntityListener {
    private Stage ui;

    public UIListener(Stage ui) {
        this.ui = ui;
    }

    @Override
    public void entityAdded(Entity entity) {
        ActorComponent ac = Mappers.actor.get(entity);

        ui.addActor(ac.actor);
        if (!ac.front) ac.actor.toBack();
    }

    @Override
    public void entityRemoved(Entity entity) {
        ActorComponent ac = Mappers.actor.get(entity);

        ac.actor.remove();
    }
}
