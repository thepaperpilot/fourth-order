package thepaperpilot.order.Screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Listeners.DialogueListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Main;
import thepaperpilot.order.Player;
import thepaperpilot.order.Systems.DialogueSystem;
import thepaperpilot.order.Systems.RenderStageSystem;
import thepaperpilot.order.Util.Constants;

public class IntroScreen implements Screen {
    public final Stage stage;
    public final Engine engine;

    public IntroScreen() {
        /* Create Stuff */
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(ActorComponent.class, DialogueComponent.class).get(), 10, new DialogueListener(stage, engine));
        engine.addEntityListener(Family.all(ActorComponent.class).get(), 11, new UIListener(stage));

        /* Add Systems to Engine */
        engine.addSystem(new DialogueSystem()); //priority 5
        engine.addSystem(new RenderStageSystem(stage)); //priority 10

        /* Add the Intro Dialogue */
        DialogueComponent dc = DialogueComponent.read("dialogue/intro.json");
        dc.start = "first";
        dc.events.put("end", new Runnable() {
            @Override
            public void run() {
                Main.changeScreen(new MapScreen());
            }
        });
        dc.events.put("alchemist", new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getTruthSpell());
            }
        });
        dc.events.put("rogue", new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getCondenseSpell());
            }
        });
        dc.events.put("ranger", new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getAntidoteSpell());
            }
        });
        dc.events.put("paladin", new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getImmortalitySpell());
            }
        });
        dc.events.put("wizard", new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getPremonitionSpell());
            }
        });
        Entity dialogue = new Entity();
        dialogue.add(dc);
        dialogue.add(new ActorComponent());
        engine.addEntity(dialogue);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
