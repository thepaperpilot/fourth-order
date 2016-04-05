package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public abstract class TotemSystem<T extends TotemComponent> extends SpellSystem {
    private T component;
    private Class<? extends T> componentClass;

    private Image glyph;
    private Image damage;

    public TotemSystem(Batch batch, T component) {
        super(batch, Family.all(component.getClass()).get());
        this.component = component;
        this.componentClass = (Class<? extends T>) component.getClass();

        glyph = new Image(Main.getTexture("GlyphUlti" + component.rune.colorName));
        glyph.setOrigin(Align.center);

        damage = new Image(Main.getTexture("OverlayDamage" + component.rune.colorName));
        damage.setOrigin(Align.center);
    }

    protected void updateTotem(Entity entity) {
        T c = entity.getComponent(componentClass);

        Entity status = new Entity();
        StatusEffectComponent sec = new StatusEffectComponent();
        sec.target = Mappers.totem.get(entity).caster;
        sec.turns = 1;
        status.add(sec);
        addStatusEffect(status, c);
        getEngine().addEntity(status);
    }

    protected void destroyRune(Entity entity) {
        T c = entity.getComponent(componentClass);

        if (!c.damaged) return;
        ElectrifiedComponent ec = new ElectrifiedComponent();
        ec.color = component.rune.colorName;
        entity.add(ec);
    }

    protected void renderRuneEffect(Entity entity) {
        UIComponent uc = Mappers.ui.get(entity);
        TotemComponent tc = Mappers.totem.get(entity);

        if (tc != null && tc.damaged) {
            damage.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
            damage.setPosition(uc.actor.getX(), uc.actor.getY());
            damage.draw(batch, batch.getColor().a);
        }

        glyph.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
        glyph.setPosition(uc.actor.getX(), uc.actor.getY());
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
        TotemComponent tc = new TotemComponent();
        tc.damaged = entityComponent.damaged;
        tc.caster = fc;

        while(true) {
            Entity random = pc.puzzle.randomRune();
            if (random != null && canCastRune(entity, random)) {
                UIComponent uc = Mappers.ui.get(random);
                zoom(uc.actor);
                random.add(c);
                random.add(tc);
                break;
            }
        }
    }

    protected boolean canCast(Entity entity) {
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        for (int i = 0; i < pc.puzzle.size; i++) {
            for (int j = 0; j < pc.puzzle.size; j++) {
                if (pc.puzzle.runes[i][j] == null) continue;
                if (canCastRune(entity, pc.puzzle.runes[i][j]) && !Mappers.totem.has(pc.puzzle.runes[i][j]) && !Mappers.glyph.has(pc.puzzle.runes[i][j])) return true;
            }
        }
        return false;
    }

    abstract void addStatusEffect(Entity entity, T c);

    abstract void copyFields(T to, T from);

    protected boolean canCastRune(Entity spell, Entity rune) {
        return Mappers.rune.get(rune).rune == Mappers.totem.get(spell).rune;
    }
}
