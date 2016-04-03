package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class RuneListener implements EntityListener {

    @Override
    public void entityAdded(final Entity entity) {
        IdleAnimationComponent ic = Mappers.idleAnimation.get(entity);
        UIComponent uc = Mappers.ui.get(entity);
        final PuzzleComponent pc = Mappers.puzzle.get(entity);
        RuneComponent rc = Mappers.rune.get(entity);

        TextureRegion texture = new TextureRegion(Main.getTexture(ic.file));
        int size = texture.getRegionHeight();
        TextureRegion[] frames = texture.split(size, size)[0];
        ic.animation = new Animation(.1f, frames);

        float x = uc.actor.getX();
        float y = uc.actor.getY();
        uc.actor = new Image(frames[0]);
        uc.actor.setPosition(x, y);

        uc.actor.setPosition(Constants.UI_WIDTH + rc.x * pc.puzzle.getRuneSize(), Constants.WORLD_HEIGHT);
        uc.actor.setScale(pc.puzzle.getRuneSize() / uc.actor.getHeight() * .75f);
        uc.actor.setOrigin(Align.center);
        pc.puzzle.runes[rc.x][rc.y] = entity;
        pc.puzzle.updateRune(entity);

        uc.actor.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                if (pc.puzzle.isStable() && pc.puzzle.turn != DestroyComponent.Target.PLAYER)
                    pc.puzzle.select(entity);
            }
        });
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
