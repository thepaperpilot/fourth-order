package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.IdleAnimationComponent;
import thepaperpilot.order.Components.PuzzleComponent;
import thepaperpilot.order.Components.RuneComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class RuneListener implements EntityListener {

    @Override
    public void entityAdded(final Entity entity) {
        IdleAnimationComponent ic = Mappers.idleAnimation.get(entity);
        ActorComponent ac = Mappers.actor.get(entity);
        final PuzzleComponent pc = Mappers.puzzle.get(entity);
        RuneComponent rc = Mappers.rune.get(entity);

        TextureRegion texture = new TextureRegion(Main.getTexture(ic.file));
        int size = texture.getRegionHeight();
        TextureRegion[] frames = texture.split(size, size)[0];
        ic.animation = new Animation(.1f, frames);

        float x = ac.actor.getX();
        float y = ac.actor.getY();
        ac.actor = new Image(frames[0]);
        ac.actor.setPosition(x, y);

        if (Mappers.destroy.has(entity)) {
            ac.actor.setPosition(Constants.UI_WIDTH + (rc.x + .375f) * pc.puzzle.getRuneSize(), Constants.WORLD_HEIGHT - (rc.y + .625f) * pc.puzzle.getRuneSize());
            ac.actor.setScale(pc.puzzle.getRuneSize() / ac.actor.getHeight() * .5f);
        } else {
            ac.actor.setPosition(Constants.UI_WIDTH + rc.x * pc.puzzle.getRuneSize(), Constants.WORLD_HEIGHT);
            ac.actor.setScale(pc.puzzle.getRuneSize() / ac.actor.getHeight() * .75f);
            pc.puzzle.runes[rc.x][rc.y] = entity;
            pc.puzzle.updateRune(entity);
        }
        ac.actor.setOrigin(Align.center);

        ac.actor.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                pc.puzzle.select(entity);
            }
        });
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
