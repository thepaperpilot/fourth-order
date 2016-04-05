package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Components.Spells.CollectComponent;
import thepaperpilot.order.Components.Spells.CommandComponent;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Util.Mappers;

public class CollectSystem extends TotemSystem {

    public CollectSystem(Batch batch) {
        super(batch, new CollectComponent());
    }

    protected void updateTotem(Entity entity) {
        FighterComponent fc = Mappers.totem.get(entity).caster;
        PuzzleComponent pc = Mappers.puzzle.get(entity);
        RuneComponent rc = Mappers.rune.get(entity);

        Entity rune = pc.puzzle.getRune(false);
        RuneComponent rc2 = Mappers.rune.get(rune);
        DestroyComponent dc = new DestroyComponent(fc);
        rc2.x = rc.x;
        rc2.y = rc.y;
        dc.destroyRune = false;
        rune.add(dc);
        getEngine().addEntity(rune);
    }

    @Override
    void addStatusEffect(Entity entity, TotemComponent c) {

    }

    @Override
    void copyFields(TotemComponent to, TotemComponent from) {
        ((CollectComponent) to).amount = ((CollectComponent) from).amount;
    }
}
