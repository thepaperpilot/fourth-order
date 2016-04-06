package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.IdleAnimationComponent;
import thepaperpilot.order.Util.Mappers;

public class IdleAnimationSystem extends IteratingSystem {
    public IdleAnimationSystem() {
        super(Family.all(IdleAnimationComponent.class, ActorComponent.class).get(), 5);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        IdleAnimationComponent ic = Mappers.idleAnimation.get(entity);
        ActorComponent ac = Mappers.actor.get(entity);

        if (ic.animating) {
            ic.time += deltaTime;
            if (ic.animation.isAnimationFinished(ic.time)) {
                ic.animating = false;
                ic.time = 0;
            }
            ((Image) ac.actor).setDrawable(new TextureRegionDrawable(ic.animation.getKeyFrame(ic.time)));
        } else if (MathUtils.randomBoolean(ic.chance)) {
            ic.animating = true;
        }
    }
}
