package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Spells.CommandComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class CommandSystem extends SpellSystem {

    private Image glyph = new Image(Main.getTexture("GlyphUltiRed"));
    private Image damage = new Image(Main.getTexture("OverlayDamageRed"));

    public CommandSystem(Batch batch) {
        super(batch, Family.all(CommandComponent.class).get());
        glyph.setOrigin(Align.center);
        damage.setOrigin(Align.center);
    }

    protected void destroyRune(Entity entity) {
        CommandComponent cc = Mappers.command.get(entity);

        if (!cc.damaged) return;
        entity.remove(CommandComponent.class);
        ElectrifiedComponent ec = new ElectrifiedComponent();
        ec.color = "Red";
        entity.add(ec);
    }

    protected void renderRuneEffect(Entity entity) {
        UIComponent uc = Mappers.ui.get(entity);
        CommandComponent cc = Mappers.command.get(entity);

        if (cc.damaged) {
            damage.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
            damage.setPosition(uc.actor.getX(), uc.actor.getY());
            damage.draw(batch, 1);
        }

        glyph.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
        glyph.setPosition(uc.actor.getX(), uc.actor.getY());
        glyph.draw(batch, 1);
    }

    protected void castSpell(Entity entity) {
        CommandComponent cc = new CommandComponent();
        FighterComponent fc = Mappers.fighter.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        // TODO it should also make sure there are red runes without glyphs
        if (pc.puzzle.countRedRunes() == 0) return; // :(

        cc.mulDamage = Mappers.command.get(entity).mulDamage;
        cc.damaged = Mappers.command.get(entity).damaged;
        cc.caster = fc;

        while(true) {
            Entity random = pc.puzzle.randomRune();
            if (random != null && Mappers.rune.get(random).mortal != 0) {
                UIComponent uc = Mappers.ui.get(random);
                zoom(uc.actor);
                random.add(cc);
                break;
            }
        }
    }
}
