package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Util.Mappers;

public class FighterSystem extends IteratingSystem {

    public FighterSystem() {
        super(Family.all(FighterComponent.class, UIComponent.class).get(), 5);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FighterComponent fc = Mappers.fighter.get(entity);

        fc.experienceLabel.setText((int) fc.exp + "/" + (int) fc.maxExp);
        fc.healthLabel.setText((int) fc.health + "/" + (int) fc.maxHealth);
        fc.poisonLabel.setText("" + (int) fc.poison);
        fc.surpriseLabel.setText("" + (int) fc.surprise);
        fc.mortalLabel.setText("" + (int) fc.mortal);
        fc.steamLabel.setText("" + (int) fc.steam);
        fc.masonLabel.setText("" + (int) fc.mason);
    }
}
