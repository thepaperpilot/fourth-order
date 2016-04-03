package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Spells.RefreshComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class RefreshSystem extends SpellSystem {

    public RefreshSystem(Batch batch) {
        super(batch, Family.all(RefreshComponent.class).get());
    }

    protected void castSpell(Entity entity) {
        RefreshComponent rc = Mappers.refresh.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        for (int i = 0; i < rc.number; i++) {
            Entity random = pc.puzzle.randomRune();
            random.add(new DestroyComponent(DestroyComponent.Target.PLAYER));
        }
    }
}
