package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.StatusEffectComponent;
import thepaperpilot.order.Util.Mappers;

public class StatusEffectSystem extends IteratingSystem {
    public StatusEffectSystem() {
        super(Family.all(StatusEffectComponent.class).get(), 15);
    }

    FighterComponent turn;

    public void update (float deltaTime) {
        if (turn == getEngine().getSystem(PuzzleSystem.class).turn) return;
        turn = getEngine().getSystem(PuzzleSystem.class).turn;
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatusEffectComponent sc = Mappers.statusEffect.get(entity);

        if (turn != sc.target) return;

        sc.turns--;
        if (sc.turns <= 0) {
            getEngine().removeEntity(entity);
        }
    }
}
