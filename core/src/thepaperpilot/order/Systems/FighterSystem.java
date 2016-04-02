package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.UIComponent;

public class FighterSystem extends IteratingSystem {

    public FighterSystem() {
        super(Family.all(FighterComponent.class, UIComponent.class).get(), 5);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
