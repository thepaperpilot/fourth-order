package thepaperpilot.order;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.ManaComponent;
import thepaperpilot.order.Components.PlayerControlledComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Listeners.FighterListener;

public class Battle implements Screen {
    public final Stage ui;
    public final Engine engine;

    public Battle() {
        /* Create Stuff */
        ui = new Stage(new StretchViewport(1280, 720));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(UIComponent.class, FighterComponent.class).get(), new FighterListener(this));

        /* Add Systems to Engine */

        /* Add Initial Entities to Engine */
        // Player Side
        Entity player = new Entity();
        player.add(new FighterComponent());
        player.add(new UIComponent());
        player.add(new ManaComponent());
        player.add(new PlayerControlledComponent());
        engine.addEntity(player);
        // Enemy Side
        Entity enemy = new Entity();
        enemy.add(new FighterComponent());
        enemy.add(new UIComponent());
        enemy.add(new ManaComponent());
        engine.addEntity(enemy);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
        ui.act();
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        ui.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        ui.dispose();
    }
}
