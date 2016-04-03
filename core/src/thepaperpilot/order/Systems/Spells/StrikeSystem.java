package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Spells.StrikeComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Mappers;

public class StrikeSystem extends SpellSystem {

    private Image strike = new Image(Main.getTexture("GlyphStrike"));

    public StrikeSystem(Batch batch) {
        super(batch, Family.all(StrikeComponent.class).get());
        strike.setOrigin(Align.center);
    }

    protected void destroyRune(Entity entity) {
        StrikeComponent sc = Mappers.strike.get(entity);
        FighterComponent fc = sc.caster;

        if (fc == PuzzleSystem.NULL_FIGHTER) return;

        PuzzleSystem puzzle = getEngine().getSystem(PuzzleSystem.class);
        if (fc == puzzle.player) {
            puzzle.enemy.hit(sc.damage);
        } else if (fc == puzzle.enemy) {
            puzzle.player.hit(sc.damage);
        }

        entity.remove(StrikeComponent.class);
        entity.add(new ElectrifiedComponent());
    }

    protected void renderRuneEffect(Entity entity) {
        UIComponent uc = Mappers.ui.get(entity);

        strike.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
        strike.setPosition(uc.actor.getX(), uc.actor.getY());
        strike.draw(batch, 1);
    }

    protected void castSpell(Entity entity) {
        StrikeComponent sc = Mappers.strike.get(entity);
        FighterComponent fc = Mappers.fighter.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        Entity random = pc.puzzle.randomRune();
        if (random != null) {
            UIComponent uc = Mappers.ui.get(random);
            super.zoom(uc.actor);
            sc.caster = fc;
            random.add(sc);
        }
    }
}
