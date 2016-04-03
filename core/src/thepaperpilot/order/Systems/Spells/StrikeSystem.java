package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Spells.StrikeComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class StrikeSystem extends SpellSystem {

    private Image strike = new Image(Main.getTexture("GlyphStrike"));

    public StrikeSystem(Batch batch) {
        super(batch, Family.all(StrikeComponent.class, CasterComponent.class).get());
        strike.setOrigin(Align.center);
    }

    protected void destroyRune(Entity entity) {
        DestroyComponent dc = Mappers.destroy.get(entity);
        StrikeComponent sc = Mappers.strike.get(entity);
        CasterComponent cc = Mappers.caster.get(entity);

        if (cc.caster == DestroyComponent.Target.NULL) return;

        if (cc.caster == DestroyComponent.Target.PLAYER) {
            FighterComponent fc = Mappers.fighter.get(getEngine().getEntitiesFor(Family.all(FighterComponent.class).exclude(PlayerControlledComponent.class).get()).first());
            fc.hit(sc.damage);
        } else if (dc.target == DestroyComponent.Target.ENEMY) {
            FighterComponent fc = Mappers.fighter.get(getEngine().getEntitiesFor(Family.all(FighterComponent.class, PlayerControlledComponent.class).get()).first());
            fc.hit(sc.damage);
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
        CasterComponent cc = Mappers.caster.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        Entity random = pc.puzzle.randomRune();
        if (random != null) {
            UIComponent uc = Mappers.ui.get(random);
            super.zoom(uc.actor);
            random.add(sc);
            random.add(cc);
        }
    }
}
