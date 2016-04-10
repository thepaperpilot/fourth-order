package thepaperpilot.order.Screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Listeners.DialogueListener;
import thepaperpilot.order.Listeners.FighterListener;
import thepaperpilot.order.Listeners.RuneListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.*;
import thepaperpilot.order.Systems.Spells.*;
import thepaperpilot.order.Util.Constants;

public class Battle implements Screen {
    public final Stage stage;
    public final Engine engine;

    public Battle(final PuzzleSystem puzzle) {
        /* Create Stuff */
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(ActorComponent.class, DialogueComponent.class).get(), 10, new DialogueListener(stage, engine));
        engine.addEntityListener(Family.all(ActorComponent.class, FighterComponent.class).get(), 10, new FighterListener(engine));
        engine.addEntityListener(Family.all(ActorComponent.class, IdleAnimationComponent.class, PuzzleComponent.class).get(), 10, new RuneListener());
        engine.addEntityListener(Family.all(ActorComponent.class).get(), 11, new UIListener(stage));

        /* Add Systems to Engine */
        engine.addSystem(new DestroyRuneSystem()); //priority 25
        engine.addSystem(new DialogueSystem()); //priority 5
        engine.addSystem(new ElectrifiedSystem(stage.getBatch())); //priority 20
        engine.addSystem(new FighterSystem()); //priority 5
        engine.addSystem(new MessageSystem(stage)); //priority 15
        engine.addSystem(new ParticleEffectSystem(stage.getBatch())); //priority 11
        engine.addSystem(puzzle); //priority 5
        engine.addSystem(new IdleAnimationSystem()); //priority 5
        engine.addSystem(new RenderStageSystem(stage)); //priority 10
        engine.addSystem(new ScreenShakeSystem(stage)); //priority 9
        engine.addSystem(new SelectedSystem(stage.getBatch())); //priority 20
        engine.addSystem(new StatusEffectSystem()); //priority 15

        engine.addSystem(new CollectSystem(stage.getBatch())); //priority 20
        engine.addSystem(new CommandSystem(stage.getBatch())); //priority 20
        engine.addSystem(new DamageSystem(stage.getBatch())); //priority 20
        engine.addSystem(new DestroyColorSystem(stage.getBatch())); //priority 20
        engine.addSystem(new HealingSystem(stage.getBatch())); //priority 20
        engine.addSystem(new RefreshSystem(stage.getBatch())); //priority 20
        engine.addSystem(new StrikeSystem(stage.getBatch())); //priority 20

        if (Constants.DEBUG) {
            stage.addListener(new InputListener() {
                public boolean keyDown (InputEvent event, int keycode) {
                    switch (keycode) {
                        case Input.Keys.P:
                            Constants.PLAYERLESS = !Constants.PLAYERLESS;
                            break;
                        case Input.Keys.I:
                            Constants.UNDYING = !Constants.UNDYING;
                            break;
                        case Input.Keys.LEFT:
                            Constants.DELTA_MOD -= .5f;
                            if (Constants.DELTA_MOD < 0) Constants.DELTA_MOD = 0;
                            break;
                        case Input.Keys.RIGHT:
                            Constants.DELTA_MOD += .5f;
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Actions.sequence(Actions.fadeOut(0), Actions.fadeIn(Constants.TRANSITION_TIME)));
        engine.getSystem(PuzzleSystem.class).stableTimer = -Constants.TRANSITION_TIME;
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
