package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.SelectedComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class SelectedSystem extends IteratingSystem {

    private Image selected = new Image(Main.getTexture("OverlaySelected"));
    private Batch batch;

    public SelectedSystem(Batch batch) {
        super(Family.all(UIComponent.class, SelectedComponent.class).get(), 20);
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        UIComponent uc = Mappers.ui.get(entity);

        selected.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
        selected.setPosition(uc.actor.getX(), uc.actor.getY());
        batch.begin();
        selected.draw(batch, 1);
        batch.end();
    }
}
