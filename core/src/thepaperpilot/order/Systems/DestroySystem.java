package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Util.Mappers;

public class DestroySystem extends IteratingSystem {
    public DestroySystem() {
        super(Family.all(DestroyComponent.class, RuneComponent.class, PuzzleComponent.class, UIComponent.class).get(), 25);
    }

    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        DestroyComponent dc = Mappers.destroy.get(entity);
        RuneComponent rc = Mappers.rune.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);
        UIComponent uc = Mappers.ui.get(entity);

        Runnable remove = new Runnable() {
            @Override
            public void run() {
                getEngine().removeEntity(entity);
            }
        };

        FighterComponent player = Mappers.fighter.get(getEngine().getEntitiesFor(Family.all(FighterComponent.class, PlayerControlledComponent.class).get()).first());
        FighterComponent enemy = Mappers.fighter.get(getEngine().getEntitiesFor(Family.all(FighterComponent.class).exclude(PlayerControlledComponent.class).get()).first());
        uc.actor.toFront();
        switch (dc.target) {
            case NULL:default:
                uc.actor.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, -1000, 2, Interpolation.swingIn), Actions.moveBy(MathUtils.random(-400, 400), 0, 2, Interpolation.pow2)), Actions.run(remove)));
                break;
            case PLAYER:
                if (rc.damage == 0) {
                    uc.actor.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 1000, 2, Interpolation.swingIn), Actions.moveBy(MathUtils.random(-1200, -800), 0, 2, Interpolation.pow2)), Actions.run(remove)));
                    player.add(rc);
                } else {
                    enemy.hit(rc.damage);
                    uc.actor.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 1000, 2, Interpolation.swingIn), Actions.moveBy(MathUtils.random(800, 1200), 0, 2, Interpolation.pow2)), Actions.run(remove)));
                    entity.add(new ElectrifiedComponent());
                }
                break;
            case ENEMY:
                if (rc.damage == 0) {
                    uc.actor.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 1000, 2, Interpolation.swingIn), Actions.moveBy(MathUtils.random(800, 1200), 0, 2, Interpolation.pow2)), Actions.run(remove)));
                    enemy.add(rc);
                } else {
                    player.hit(rc.damage);
                    uc.actor.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0, 1000, 2, Interpolation.swingIn), Actions.moveBy(MathUtils.random(-1200, -800), 0, 2, Interpolation.pow2)), Actions.run(remove)));
                    entity.add(new ElectrifiedComponent());
                }
                break;
        }

        pc.puzzle.runes[pc.x][pc.y] = null;
        entity.remove(RuneComponent.class);
    }
}
