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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Listeners.FighterListener;
import thepaperpilot.order.Listeners.RuneListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Systems.*;
import thepaperpilot.order.Systems.Spells.DestroyColorSystem;
import thepaperpilot.order.Systems.Spells.RefreshSystem;
import thepaperpilot.order.Systems.Spells.StrikeSystem;
import thepaperpilot.order.Util.Constants;

public class Battle implements Screen {
    public final Stage ui;
    public final Engine engine;

    public Battle(final int size, final FighterComponent enemy) {
        /* Create Stuff */
        ui = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(UIComponent.class, FighterComponent.class).get(), 10, new FighterListener(engine));
        engine.addEntityListener(Family.all(UIComponent.class, IdleAnimationComponent.class, PuzzleComponent.class).get(), 10, new RuneListener());
        engine.addEntityListener(Family.all(UIComponent.class).get(), 11, new UIListener(ui));

        /* Add Systems to Engine */
        engine.addSystem(new DestroyRuneSystem()); //priority 25
        engine.addSystem(new DestroySpellSystem()); //priority 25
        engine.addSystem(new DialogueSystem(ui)); //priority 15
        engine.addSystem(new ElectrifiedSystem(ui.getBatch())); //priority 20
        engine.addSystem(new FighterSystem()); //priority 5
        engine.addSystem(new MessageSystem(ui)); //priority 15
        engine.addSystem(new ParticleEffectSystem(ui.getBatch())); //priority 9
        engine.addSystem(new PuzzleSystem(size, enemy)); //priority 5
        engine.addSystem(new IdleAnimationSystem()); //priority 5
        engine.addSystem(new RenderStageSystem(ui)); //priority 10
        engine.addSystem(new ScreenShakeSystem(ui)); //priority 9
        engine.addSystem(new SelectedSystem(ui.getBatch())); //priority 20

        engine.addSystem(new DestroyColorSystem(ui.getBatch())); //priority 20
        engine.addSystem(new RefreshSystem(ui.getBatch())); //priority 20
        engine.addSystem(new StrikeSystem(ui.getBatch())); //priority 20

        /* Input Processing (debug suff) */
        ui.addListener(new InputListener() {
            public boolean keyDown (InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.SPACE:
                        Main.changeScreen(new Battle(size, enemy));
                        break;
                    case Input.Keys.P:
                        Constants.PLAYERLESS = !Constants.PLAYERLESS;
                        break;
                    case Input.Keys.LEFT:
                        Constants.DELTA_MOD -= .1f;
                        break;
                    case Input.Keys.RIGHT:
                        Constants.DELTA_MOD += .1f;
                        break;
                }
                return false;
            }
        });
    }

    public Battle(FighterComponent enemy) {
        this(9, enemy);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(ui);
        ui.addAction(Actions.fadeIn(1));
        engine.getSystem(PuzzleSystem.class).stableTimer = -1;
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
