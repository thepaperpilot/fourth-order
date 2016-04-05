package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class SpellSystem extends IteratingSystem {

    protected Batch batch;
    private FighterComponent turn;
    private boolean updateTotems = false;

    public SpellSystem(Batch batch, Family family) {
        super(family, 20);
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        PuzzleSystem puzzle = getEngine().getSystem(PuzzleSystem.class);
        FighterComponent currentTurn = puzzle.turn;
        if (turn != currentTurn && puzzle.stable && puzzle.stableTimer > Constants.STABLE_TIME / 2f) {
            turn = currentTurn;
            updateTotems = true;
        }
        if (getEntities().size() == 0) return;
        batch.begin();
        super.update(deltaTime);
        batch.end();
        updateTotems = false;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Mappers.ui.has(entity)) {
            renderRuneEffect(entity);
            if (Mappers.rune.has(entity)) {
                if (Mappers.totem.has(entity) && updateTotems && turn == Mappers.totem.get(entity).caster) updateTotem(entity);
                if (Mappers.destroy.has(entity) && Mappers.destroy.get(entity).collector != PuzzleSystem.NULL_FIGHTER) {
                    destroyRune(entity);
                }
            }
        } else if (Mappers.spell.has(entity)) {
            if (canCast(entity)) {
                FighterComponent fc = Mappers.fighter.get(entity);
                Mappers.puzzle.get(entity).puzzle.takeTurn(fc);
                fc.sub(Mappers.spell.get(entity));
                castSpell(entity);
                Main.playSound("spell.wav");
            }

            entity.remove(SpellComponent.class);
        }
    }

    protected boolean canCast(Entity entity) {
        return true;
    }

    protected void updateTotem(Entity entity) {

    }

    // make sure to remove the spell component from the entity (not SpellComponent)
    protected void destroyRune(Entity entity) {

    }

    protected void renderRuneEffect(Entity entity) {

    }

    protected void castSpell(Entity entity) {

    }

    public static void zoom(Actor actor) {
        actor.addAction(Actions.sequence(Actions.scaleBy(Constants.RUNE_ZOOM, Constants.RUNE_ZOOM, .5f, Interpolation.pow2), Actions.scaleBy(-Constants.RUNE_ZOOM, -Constants.RUNE_ZOOM, .5f, Interpolation.pow2)));
    }
}

