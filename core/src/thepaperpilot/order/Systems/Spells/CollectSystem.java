package thepaperpilot.order.Systems.Spells;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Components.Spells.CollectComponent;
import thepaperpilot.order.Components.Spells.CommandComponent;
import thepaperpilot.order.Util.Mappers;

public class CollectSystem extends TotemSystem {

    public CollectSystem(Batch batch) {
        super(batch, new CollectComponent());
    }

    protected void updateTotem(Entity entity) {
        FighterComponent fc = Mappers.totem.get(entity).caster;
        PuzzleComponent pc = Mappers.puzzle.get(entity);
        RuneComponent rc = Mappers.rune.get(entity);

        Entity rune = new Entity();
        RuneComponent rc2 = new RuneComponent();
        IdleAnimationComponent ic = new IdleAnimationComponent();
        DestroyComponent dc = new DestroyComponent(fc);
        dc.destroyRune = false;
        rc2.x = rc.x;
        rc2.y = rc.y;
        switch (MathUtils.random(4)) {
            case 0:
                rc2.poison = 1;
                ic.file = "PurpleGem";
                break;
            case 1:
                rc2.surprise = 1;
                ic.file = "YellowGem";
                break;
            case 2:
                rc2.mortal = 1;
                ic.file = "RedGem";
                break;
            case 3:
                rc2.steam = 1;
                ic.file = "BlueGem";
                break;
            case 4:
                rc2.mason = 1;
                ic.file = "GreenGem";
                break;
        }
        rune.add(pc);
        rune.add(rc2);
        rune.add(ic);
        rune.add(dc);
        rune.add(new UIComponent());
        getEngine().addEntity(rune);
    }

    @Override
    void addStatusEffect(Entity entity, TotemComponent c) {

    }

    @Override
    void copyFields(TotemComponent to, TotemComponent from) {
        ((CollectComponent) to).amount = ((CollectComponent) from).amount;
    }

    @Override
    protected boolean canCastRune(Entity spell, Entity rune) {
        return Mappers.rune.get(rune).surprise != 0;
    }
}
