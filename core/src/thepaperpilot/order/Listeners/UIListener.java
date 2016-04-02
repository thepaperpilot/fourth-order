package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Util.Mappers;

public class UIListener implements EntityListener {
    private Stage ui;

    public UIListener(Stage ui) {
        this.ui = ui;
    }

    @Override
    public void entityAdded(Entity entity) {
        UIComponent uc = Mappers.ui.get(entity);

        ui.addActor(uc.actor);
        uc.actor.toBack();
    }

    @Override
    public void entityRemoved(Entity entity) {
        UIComponent uc = Mappers.ui.get(entity);

        uc.actor.remove();
    }
}
