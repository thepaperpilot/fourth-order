package thepaperpilot.order;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Systems.DialogueSystem;
import thepaperpilot.order.Systems.RenderStageSystem;
import thepaperpilot.order.Util.Constants;

public class DialogueScreen implements Screen { //possible for things other than dialogues
    public final Stage ui;
    public final Engine engine;

    public DialogueScreen(DialogueComponent dc) {
        /* Create Stuff */
        ui = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */

        /* Add Systems to Engine */
        engine.addSystem(new DialogueSystem(ui)); //priority 5
        engine.addSystem(new RenderStageSystem(ui)); //priority 10

        /* Add the Dialogue */
        Entity dialogue = new Entity();
        dialogue.add(dc);
        engine.addEntity(dialogue);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
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
