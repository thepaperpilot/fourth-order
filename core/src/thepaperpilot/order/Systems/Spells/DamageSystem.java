package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import thepaperpilot.order.Components.Effects.DamageOverTimeComponent;
import thepaperpilot.order.Components.Spells.DamageComponent;
import thepaperpilot.order.Components.StatusEffectComponent;
import thepaperpilot.order.Components.TotemComponent;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Mappers;

public class DamageSystem extends TotemSystem {

    public DamageSystem(Batch batch) {
        super(batch, new DamageComponent());
    }

    public DamageSystem(Batch batch, TotemComponent component) {
        super(batch, component);
    }

    @Override
    void addStatusEffect(Entity entity, TotemComponent c) {
        DamageOverTimeComponent hc = new DamageOverTimeComponent();
        hc.amount = ((DamageComponent) c).damage;
        entity.add(hc);

        switchTarget(entity);
    }

    protected void switchTarget(Entity entity) {
        StatusEffectComponent sec = Mappers.statusEffect.get(entity);
        PuzzleSystem puzzle = getEngine().getSystem(PuzzleSystem.class);
        if (sec.target == puzzle.player) {
            sec.target = puzzle.enemy;
        } else sec.target = puzzle.player;
    }

    @Override
    void copyFields(TotemComponent to, TotemComponent from) {
        ((DamageComponent) to).damage = ((DamageComponent) from).damage;
    }
}
