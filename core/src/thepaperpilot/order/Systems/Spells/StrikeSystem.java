package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.GlyphComponent;
import thepaperpilot.order.Components.PuzzleComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;
import thepaperpilot.order.Util.Mappers;

public class StrikeSystem extends GlyphSystem {

    public StrikeSystem(Batch batch) {
        super(batch, new StrikeComponent());
    }

    @Override
    protected void runEffect(Entity entity) {
        StrikeComponent sc = Mappers.strike.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);
        FighterComponent fc = Mappers.glyph.get(entity).caster;

        if (fc == pc.puzzle.player) {
            pc.puzzle.enemy.hit(sc.damage, pc.puzzle);
        } else if (fc == pc.puzzle.enemy) {
            pc.puzzle.player.hit(sc.damage, pc.puzzle);
        }
    }

    @Override
    void copyFields(GlyphComponent to, GlyphComponent from) {
        ((StrikeComponent) to).damage = ((StrikeComponent) from).damage;
    }
}
