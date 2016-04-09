package thepaperpilot.order.Screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Listeners.DialogueListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Systems.DialogueSystem;
import thepaperpilot.order.Systems.HUDSystem;
import thepaperpilot.order.Systems.RenderStageSystem;
import thepaperpilot.order.Util.Constants;

public class Location implements Screen {
    public final Stage stage;
    public final Engine engine;

    public Location() {
        /* Create Stuff */
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(ActorComponent.class, DialogueComponent.class).get(), 10, new DialogueListener(stage, engine));
        engine.addEntityListener(Family.all(ActorComponent.class).get(), 11, new UIListener(stage));

        /* Add Systems to Engine */
        engine.addSystem(new HUDSystem()); //priority 12
        engine.addSystem(new DialogueSystem()); //priority 5
        engine.addSystem(new RenderStageSystem(stage)); //priority 10
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.getRoot().getColor().a = 0;
        stage.addAction(Actions.fadeIn(Constants.TRANSITION_TIME));
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
