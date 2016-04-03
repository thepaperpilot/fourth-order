package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class DestroyRuneSystem extends IteratingSystem {
    public DestroyRuneSystem() {
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

        uc.actor.toFront();
        if (dc.target == DestroyComponent.Target.NULL) {
            uc.actor.addAction(
                    Actions.sequence(
                            Actions.parallel(
                                    Actions.moveBy(0, -1000, Constants.RUNE_EXIT_SPEED, Interpolation.swingIn),
                                    Actions.moveBy(MathUtils.random(-400, 400), 0, Constants.RUNE_EXIT_SPEED, Interpolation.pow2)),
                            Actions.run(remove)));
        } else {
            FighterComponent player = Mappers.fighter.get(getEngine().getEntitiesFor(Family.all(FighterComponent.class, PlayerControlledComponent.class).get()).first());
            FighterComponent enemy = Mappers.fighter.get(getEngine().getEntitiesFor(Family.all(FighterComponent.class).exclude(PlayerControlledComponent.class).get()).first());

            Action left = Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(0, 1000, Constants.RUNE_EXIT_SPEED, Interpolation.swingIn),
                            Actions.moveBy(MathUtils.random(-1200, -800), 0, Constants.RUNE_EXIT_SPEED, Interpolation.pow2)),
                    Actions.run(remove));
            Action right = Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(0, 1000, Constants.RUNE_EXIT_SPEED, Interpolation.swingIn),
                            Actions.moveBy(MathUtils.random(800, 1200), 0, Constants.RUNE_EXIT_SPEED, Interpolation.pow2)),
                    Actions.run(remove));

            switch (dc.target) {
                case PLAYER:
                    if (rc.damage == 0) {
                        uc.actor.addAction(left);
                        player.add(rc);
                    } else {
                        enemy.hit(rc.damage);
                        uc.actor.addAction(right);
                        entity.add(new ElectrifiedComponent());
                    }
                    break;
                case ENEMY:
                    if (rc.damage == 0) {
                        uc.actor.addAction(right);
                        enemy.add(rc);
                    } else {
                        player.hit(rc.damage);
                        uc.actor.addAction(left);
                        entity.add(new ElectrifiedComponent());
                    }
                    break;
            }
        }

        pc.puzzle.runes[rc.x][rc.y] = null;
        entity.remove(RuneComponent.class);
    }
}
