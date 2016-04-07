package thepaperpilot.order.Screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Listeners.DialogueListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.DialogueSystem;
import thepaperpilot.order.Systems.MapButtonsSystem;
import thepaperpilot.order.Systems.RenderStageSystem;
import thepaperpilot.order.Util.Constants;

public class MapScreen implements Screen {
    public final Stage stage;
    public final Engine engine;

    public MapScreen() {
        /* Create Stuff */
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        engine = new Engine();

        /* Add Listeners to Engine */
        engine.addEntityListener(Family.all(ActorComponent.class, DialogueComponent.class).get(), 10, new DialogueListener(stage, engine));
        engine.addEntityListener(Family.all(ActorComponent.class).get(), 11, new UIListener(stage));

        /* Add Systems to Engine */
        engine.addSystem(new DialogueSystem()); //priority 5
        engine.addSystem(new MapButtonsSystem(stage)); //priority 12
        engine.addSystem(new RenderStageSystem(stage)); //priority 10

        /* Add the the temporary battle button */
        final TextButton button = new TextButton("temporary\nbattle button", Main.skin);
        button.setPosition(200, Constants.WORLD_HEIGHT - 200, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DialogueComponent dc = DialogueComponent.read("dialogue/opponent.json");
                dc.start = "first";
                dc.events.put("alchemist", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = new FighterComponent();
                        enemy.portrait = "PortraitAlchemist";
                        enemy.add(SpellComponent.getSpell("Strike"));
                        enemy.add(SpellComponent.getSpell("Truth"));
                        enemy.add(SpellComponent.getSpell("Wither"));
                        enemy.victory = "alchemist";
                        enemy.defeat = "alchemist";
                        Main.changeScreen(new Battle(enemy, MapScreen.this));
                    }
                });
                dc.events.put("rogue", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = new FighterComponent();
                        enemy.portrait = "PortraitRogue";
                        enemy.add(SpellComponent.getSpell("Strike"));
                        enemy.add(SpellComponent.getSpell("Condense"));
                        enemy.add(SpellComponent.getSpell("Collect"));
                        enemy.victory = "rogue";
                        enemy.defeat = "rogue";
                        Main.changeScreen(new Battle(enemy, MapScreen.this));
                    }
                });
                dc.events.put("ranger", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = new FighterComponent();
                        enemy.portrait = "PortraitRanger";
                        enemy.add(SpellComponent.getSpell("Strike"));
                        enemy.add(SpellComponent.getSpell("Premonition"));
                        // TODO distort totem spell
                        enemy.victory = "ranger";
                        enemy.defeat = "ranger";
                        Main.changeScreen(new Battle(enemy, MapScreen.this));
                    }
                });
                dc.events.put("paladin", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = new FighterComponent();
                        enemy.portrait = "PortraitPaladin";
                        enemy.add(SpellComponent.getSpell("Strike"));
                        enemy.add(SpellComponent.getSpell("Immortality"));
                        enemy.add(SpellComponent.getSpell("Sustain"));
                        enemy.victory = "paladin";
                        enemy.defeat = "paladin";
                        Main.changeScreen(new Battle(enemy, MapScreen.this));
                    }
                });
                dc.events.put("wizard", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = new FighterComponent();
                        enemy.portrait = "PortraitWizard";
                        enemy.add(SpellComponent.getSpell("Strike"));
                        enemy.add(SpellComponent.getSpell("Antidote"));
                        enemy.add(SpellComponent.getSpell("Command"));
                        enemy.victory = "wizard";
                        enemy.defeat = "wizard";
                        Main.changeScreen(new Battle(enemy, MapScreen.this));
                    }
                });
                Entity dialogue = new Entity();
                dialogue.add(dc);
                dialogue.add(new ActorComponent());
                engine.addEntity(dialogue);
                button.toBack();
            }
        });
        Entity entity = new Entity();
        ActorComponent ac = new ActorComponent();
        ac.actor = button;
        entity.add(ac);
        engine.addEntity(entity);
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
