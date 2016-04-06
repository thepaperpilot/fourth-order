package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Util.Mappers;

public class FighterSystem extends IteratingSystem {

    public FighterSystem() {
        super(Family.all(FighterComponent.class, ActorComponent.class).get(), 5);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FighterComponent fc = Mappers.fighter.get(entity);

        for (Rune rune : Rune.values()) {
            String text = "" + fc.runes.get(rune).intValue();
            if (rune == Rune.DAMAGE || rune == Rune.EXP) text += "/" + fc.maxRunes.get(rune).intValue();
            fc.labels.get(rune).setText(text);
        }

        fc.updateSpellCosts();
    }
}
