package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import thepaperpilot.order.Components.ElectrifiedComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class ElectrifiedSystem extends IteratingSystem {

    private Animation electric = new Animation(.1f, new Array<TextureRegion>(new TextureRegion(Main.getTexture("OverlayLightningRed")).split(16, 16)[0]), Animation.PlayMode.LOOP);
    private Batch batch;

    public ElectrifiedSystem(Batch batch) {
        super(Family.all(UIComponent.class, ElectrifiedComponent.class).get(), 20);
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        UIComponent uc = Mappers.ui.get(entity);
        ElectrifiedComponent ec = Mappers.electrified.get(entity);

        ec.time += deltaTime;
        Image image = new Image(electric.getKeyFrame(ec.time));
        image.setPosition(uc.actor.getX(), uc.actor.getY());
        image.setScale(uc.actor.getScaleX(), uc.actor.getScaleY());
        batch.begin();
        image.draw(batch, 1);
        batch.end();
    }
}
