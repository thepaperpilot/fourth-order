package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Mappers;

public abstract class GlyphSystem<T extends GlyphComponent> extends SpellSystem {
    private Class<? extends T> componentClass;

    private Image glyph;

    public GlyphSystem(Batch batch, T component) {
        super(batch, Family.all(component.getClass()).get());
        this.componentClass = (Class<? extends T>) component.getClass();

        glyph = new Image(Main.getTexture("Glyph" + component.glyph));
        glyph.setOrigin(Align.center);
    }

    protected void destroyRune(Entity entity) {
        FighterComponent fc = Mappers.glyph.get(entity).caster;

        if (fc == PuzzleSystem.NULL_FIGHTER) return;

        runEffect(entity);

        entity.add(new ElectrifiedComponent());
    }

    protected void renderRuneEffect(Entity entity) {
        ActorComponent ac = Mappers.actor.get(entity);

        glyph.setScale(ac.actor.getScaleX(), ac.actor.getScaleY());
        glyph.setPosition(ac.actor.getX(), ac.actor.getY());
        glyph.draw(batch, batch.getColor().a);
    }

    protected void castSpell(Entity entity) {
        T c;
        try {
            c = componentClass.newInstance();
        } catch (InstantiationException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        }
        T entityComponent = entity.getComponent(componentClass);
        FighterComponent fc = Mappers.fighter.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        copyFields(c, entityComponent);
        GlyphComponent gc = new GlyphComponent();
        gc.caster = fc;

        while(true) {
            Entity random = pc.puzzle.randomRune();
            if (random != null && canCastRune(random)) {
                ActorComponent ac = Mappers.actor.get(random);
                zoom(ac.actor);
                random.add(c);
                random.add(gc);
                break;
            }
        }
    }

    protected boolean canCast(Entity entity) {
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        for (int i = 0; i < pc.puzzle.size; i++) {
            for (int j = 0; j < pc.puzzle.size; j++) {
                if (pc.puzzle.runes[i][j] == null) continue;
                if (canCastRune(pc.puzzle.runes[i][j]) && !Mappers.totem.has(pc.puzzle.runes[i][j]) && !Mappers.glyph.has(pc.puzzle.runes[i][j])) return true;
            }
        }
        return false;
    }

    abstract protected void runEffect(Entity entity);

    abstract void copyFields(T to, T from);

    protected boolean canCastRune(Entity entity) {
        return true;
    }
}
