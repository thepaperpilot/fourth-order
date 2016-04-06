package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Main;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Systems.Spells.SpellSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class DestroyRuneSystem extends IteratingSystem {
    public DestroyRuneSystem() {
        super(Family.all(DestroyComponent.class, RuneComponent.class, PuzzleComponent.class, ActorComponent.class).get(), 25);
    }

    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        DestroyComponent dc = Mappers.destroy.get(entity);
        RuneComponent rc = Mappers.rune.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);
        ActorComponent ac = Mappers.actor.get(entity);

        Runnable remove = new Runnable() {
            @Override
            public void run() {
                getEngine().removeEntity(entity);
            }
        };

        ac.actor.toFront();
        if (dc.collector == PuzzleSystem.NULL_FIGHTER) {
            ac.actor.addAction(
                    Actions.sequence(
                            Actions.parallel(
                                    Actions.moveBy(0, -1000, Constants.RUNE_EXIT_TIME, Interpolation.swingIn),
                                    Actions.moveBy(MathUtils.random(-200, 200), 0, Constants.RUNE_EXIT_TIME, Interpolation.pow2)),
                            Actions.run(remove)));
        } else {
            if (Mappers.totem.has(entity)) {
                TotemComponent tc = Mappers.totem.get(entity);

                if (!tc.damaged) {
                    tc.damaged = true;
                    SpellSystem.zoom(ac.actor);
                    entity.remove(DestroyComponent.class);
                    return;
                }
            }
            Action left = Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(-ac.actor.getX(), Constants.WORLD_HEIGHT - ac.actor.getY() + 200, Constants.RUNE_EXIT_TIME, Interpolation.circleIn),
                            Actions.moveBy(MathUtils.random(-200, 200), -200, Constants.RUNE_EXIT_TIME, Interpolation.pow2)),
                    Actions.run(remove));
            Action right = Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(Constants.WORLD_WIDTH - ac.actor.getX(), Constants.WORLD_HEIGHT - ac.actor.getY() + 200, Constants.RUNE_EXIT_TIME, Interpolation.circleIn),
                            Actions.moveBy(MathUtils.random(-200, 200), -200, Constants.RUNE_EXIT_TIME, Interpolation.pow2)),
                    Actions.run(remove));
            Action zoom = Actions.sequence(
                    Actions.scaleBy(2, 2, Constants.RUNE_EXIT_TIME / 2f, Interpolation.pow2),
                    Actions.scaleTo(0, 0, Constants.RUNE_EXIT_TIME / 2f, Interpolation.pow2In));

            if (dc.collector == pc.puzzle.player) {
                if (rc.rune == Rune.DAMAGE) {
                    pc.puzzle.enemy.hit(1, pc.puzzle);
                    ac.actor.addAction(Actions.parallel(right, zoom));
                    entity.add(new ElectrifiedComponent());
                } else {
                    ac.actor.addAction(Actions.parallel(left, zoom));
                    pc.puzzle.player.add(rc, pc.puzzle);
                }
            } else if (dc.collector == pc.puzzle.enemy){
                if (rc.rune == Rune.DAMAGE) {
                    pc.puzzle.player.hit(1, pc.puzzle);
                    ac.actor.addAction(Actions.parallel(left, zoom));
                    entity.add(new ElectrifiedComponent());
                } else {
                    ac.actor.addAction(Actions.parallel(right, zoom));
                    pc.puzzle.enemy.add(rc, pc.puzzle);
                }
            }
        }

        Main.playSound("rune.wav");

        if (dc.destroyRune)
            pc.puzzle.runes[rc.x][rc.y] = null;
        entity.remove(RuneComponent.class);
    }
}
