package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.order.Components.IdleAnimationComponent;
import thepaperpilot.order.Components.PuzzleComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class IdleAnimationListener implements EntityListener {

    @Override
    public void entityAdded(Entity entity) {
        IdleAnimationComponent ic = Mappers.idleAnimation.get(entity);
        UIComponent uc = Mappers.ui.get(entity);

        TextureRegion texture = new TextureRegion(Main.getTexture(ic.file));
        int size = texture.getRegionHeight();
        TextureRegion[] frames = texture.split(size, size)[0];
        ic.animation = new Animation(.1f, frames);
        float x = uc.actor.getX();
        float y = uc.actor.getY();
        uc.actor = new Image(frames[0]);
        uc.actor.setPosition(x, y);

        if (Mappers.puzzle.has(entity)) {
            PuzzleComponent pc = Mappers.puzzle.get(entity);

            uc.actor.setPosition(Constants.UI_WIDTH + (pc.x + .125f) * pc.puzzle.getRuneSize(), Constants.WORLD_HEIGHT);
            uc.actor.setScale(pc.puzzle.getRuneSize() / uc.actor.getHeight() * .75f);
            pc.puzzle.runes[pc.x][pc.y] = entity;
            pc.puzzle.lowerRune(entity);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
