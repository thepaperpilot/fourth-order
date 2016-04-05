package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Components.Spells.CommandComponent;
import thepaperpilot.order.Components.TotemComponent;
import thepaperpilot.order.Util.Mappers;

public class CommandSystem extends TotemSystem {

    public CommandSystem(Batch batch) {
        super(batch, new CommandComponent());
    }

    @Override
    void addStatusEffect(Entity entity, TotemComponent c) {
        DamageMultiplierComponent dc = new DamageMultiplierComponent();
        dc.multiplier = ((CommandComponent) c).mulDamage;
        entity.add(dc);
    }

    @Override
    void copyFields(TotemComponent to, TotemComponent from) {
        ((CommandComponent) to).mulDamage = ((CommandComponent) from).mulDamage;
    }
}
