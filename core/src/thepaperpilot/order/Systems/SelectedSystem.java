package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
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
        selected.setOrigin(Align.center);
    }

    @Override
    public void update(float deltaTime) {
        if (getEntities().size() == 0) return;
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        UIComponent uc = Mappers.ui.get(entity);

        selected.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
        selected.setPosition(uc.actor.getX(), uc.actor.getY());
        selected.draw(batch, 1);
    }
}
