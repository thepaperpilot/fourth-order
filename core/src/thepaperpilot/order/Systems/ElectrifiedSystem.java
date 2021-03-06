package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.ElectrifiedComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;

public class ElectrifiedSystem extends IteratingSystem {

    private Animation skull = getAnimation("Skull");
    private Animation blue = getAnimation("Blue");
    private Animation green = getAnimation("Green");
    private Animation purple = getAnimation("Purple");
    private Animation red = getAnimation("Red");
    private Animation yellow = getAnimation("Yellow");

    private Animation getAnimation(String color) {
        return new Animation(.1f, new Array<TextureRegion>(new TextureRegion(Main.getTexture("OverlayLightning" + color)).split(16, 16)[0]), Animation.PlayMode.LOOP);
    }

    private Batch batch;

    public ElectrifiedSystem(Batch batch) {
        super(Family.all(ActorComponent.class, ElectrifiedComponent.class).get(), 20);
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        if (getEntities().size() == 0) return;
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActorComponent ac = Mappers.actor.get(entity);
        ElectrifiedComponent ec = Mappers.electrified.get(entity);

        ec.time += deltaTime;

        Animation animation = skull;
        if (ec.color.equals("Blue")) animation = blue;
        else if (ec.color.equals("Green")) animation = green;
        else if (ec.color.equals("Purple")) animation = purple;
        else if (ec.color.equals("Red")) animation = red;
        else if (ec.color.equals("Yellow")) animation = yellow;

        batch.draw(animation.getKeyFrame(ec.time), ac.actor.getX(), ac.actor.getY(), ac.actor.getOriginX(), ac.actor.getOriginY(), ac.actor.getWidth(), ac.actor.getHeight(), ac.actor.getScaleX(), ac.actor.getScaleY(), ac.actor.getRotation());
    }
}
