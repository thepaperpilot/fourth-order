package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import thepaperpilot.order.Components.Spells.HealingComponent;
import thepaperpilot.order.Components.TotemComponent;

public class HealingSystem extends DamageSystem {
    public HealingSystem(Batch batch) {
        super(batch, new HealingComponent());
    }

    @Override
    void addStatusEffect(Entity entity, TotemComponent c) {
        super.addStatusEffect(entity, c);
        super.switchTarget(entity);
    }
}
