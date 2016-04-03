package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import thepaperpilot.order.Components.SpellComponent;

public class DestroySpellSystem extends IteratingSystem {
    public DestroySpellSystem() {
        super(Family.all(SpellComponent.class).get(), 25);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity.getComponents().size() == 2) { // spell component, puzzle component
            getEngine().removeEntity(entity);
        }
    }
}
