package thepaperpilot.order;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Listeners.FighterListener;
import thepaperpilot.order.Listeners.IdleAnimationListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Systems.IdleAnimationSystem;
import thepaperpilot.order.Systems.PuzzleSystem;
import thepaperpilot.order.Util.Constants;

public class Battle implements Screen {
    public final Stage ui;
    public final Engine engine;

    public Battle() {
        /* Create Stuff */
        ui = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(UIComponent.class, FighterComponent.class).get(), 10, new FighterListener());
        engine.addEntityListener(Family.all(UIComponent.class, IdleAnimationComponent.class).get(), 10, new IdleAnimationListener());
        engine.addEntityListener(Family.all(UIComponent.class).get(), 11, new UIListener(ui));

        /* Add Systems to Engine */
        engine.addSystem(new PuzzleSystem(9));
        engine.addSystem(new IdleAnimationSystem());

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

        /* Input Processing */
        ui.addListener(new InputListener() {
            public boolean keyDown (InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    Main.changeScreen(new Battle());
                }
                return false;
            }
        });
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
