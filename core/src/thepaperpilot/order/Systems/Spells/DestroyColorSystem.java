package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Batch;
import thepaperpilot.order.Components.DestroyComponent;
import thepaperpilot.order.Components.PuzzleComponent;
import thepaperpilot.order.Components.RuneComponent;
import thepaperpilot.order.Components.Spells.DestroyColorComponent;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Mappers;

public class DestroyColorSystem extends SpellSystem {

    public DestroyColorSystem(Batch batch) {
        super(batch, Family.all(DestroyColorComponent.class).get());
    }

    protected void castSpell(Entity entity) {
        DestroyColorComponent dc = Mappers.destroyColor.get(entity);
        PuzzleComponent pc = Mappers.puzzle.get(entity);

        for (int i = 0; i < pc.puzzle.size; i++) {
            for (int j = 0; j < pc.puzzle.size; j++) {
                RuneComponent rc = Mappers.rune.get(pc.puzzle.runes[i][j]);
                switch (dc.color) {
                    case 0:
                        if (rc.poison != 0) pc.puzzle.runes[i][j].add(new DestroyComponent(PuzzleSystem.NULL_FIGHTER));
                        break;
                    case 1:
                        if (rc.surprise != 0) pc.puzzle.runes[i][j].add(new DestroyComponent(PuzzleSystem.NULL_FIGHTER));
                        break;
                    case 2:
                        if (rc.mortal != 0) pc.puzzle.runes[i][j].add(new DestroyComponent(PuzzleSystem.NULL_FIGHTER));
                        break;
                    case 3:
                        if (rc.steam != 0) pc.puzzle.runes[i][j].add(new DestroyComponent(PuzzleSystem.NULL_FIGHTER));
                        break;
                    case 4:
                        if (rc.mason != 0) pc.puzzle.runes[i][j].add(new DestroyComponent(PuzzleSystem.NULL_FIGHTER));
                        break;

                }
            }
        }
    }
}

