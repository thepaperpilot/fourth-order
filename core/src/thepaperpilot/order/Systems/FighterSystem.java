package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class FighterSystem extends IteratingSystem {
    Drawable filled = new Image(Main.getTexture("UICircleFilled")).getDrawable();

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

        for (Entity spell : fc.spells) {
            SpellComponent sc = Mappers.spell.get(spell);

            if (sc.poison != 0 && fc.poison >= sc.poison) sc.poisonDisplay.setDrawable(filled);
            if (sc.surprise != 0 && fc.surprise >= sc.surprise) sc.surpriseDisplay.setDrawable(filled);
            if (sc.mortal != 0 && fc.mortal >= sc.mortal) sc.mortalDisplay.setDrawable(filled);
            if (sc.steam != 0 && fc.steam >= sc.steam) sc.steamDisplay.setDrawable(filled);
            if (sc.mason != 0 && fc.mason >= sc.mason) sc.masonDisplay.setDrawable(filled);
        }
    }
}
