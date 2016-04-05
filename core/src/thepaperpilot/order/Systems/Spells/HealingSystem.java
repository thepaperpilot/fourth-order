package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import thepaperpilot.order.Components.Spells.HealingComponent;
import thepaperpilot.order.Components.TotemComponent;
import thepaperpilot.order.Util.Mappers;

public class HealingSystem extends DamageSystem {
    public HealingSystem(Batch batch) {
        super(batch, new HealingComponent());
    }

    @Override
    void addStatusEffect(Entity entity, TotemComponent c) {
        super.addStatusEffect(entity, c);
        super.switchTarget(entity);
    }

    @Override
    protected boolean canCastRune(Entity spell, Entity rune) {
        return Mappers.rune.get(rune).steam != 0;
    }
}
