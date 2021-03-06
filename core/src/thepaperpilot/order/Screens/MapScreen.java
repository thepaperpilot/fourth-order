package thepaperpilot.order.Screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Class;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Listeners.DialogueListener;
import thepaperpilot.order.Listeners.UIListener;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.HUDSystem;
import thepaperpilot.order.Systems.MapSystem;
import thepaperpilot.order.Systems.PuzzleSystem;
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
        engine.addSystem(new HUDSystem()); //priority 12
        engine.addSystem(new RenderStageSystem(stage)); //priority 10
        engine.addSystem(new MapSystem(stage)); //priority 9

        /* Add the the temporary battle button */
        final TextButton button = new TextButton("temporary\nbattle button", Main.skin);
        button.setPosition(200, Constants.WORLD_HEIGHT - 200, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Location location = new Location();
                DialogueComponent dc = DialogueComponent.read("dialogue/opponent.json");
                dc.start = "first";
                dc.events.put("alchemist", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = FighterComponent.getEnemy(Class.ALCHEMIST, 0);
                        enemy.portrait = "PortraitAlchemist";
                        PuzzleSystem puzzle = new PuzzleSystem(enemy, location);
                        puzzle.victoryDialogue = "alchemist";
                        puzzle.defeatDialogue = "alchemist";
                        puzzle.victoryEvent = puzzle.defeatEvent = new Runnable() {
                            @Override
                            public void run() {
                                Main.changeScreen(MapScreen.this);
                            }
                        };
                        Main.changeScreen(new Battle(puzzle));
                    }
                });
                dc.events.put("rogue", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = FighterComponent.getEnemy(Class.ROGUE, 1);
                        enemy.portrait = "PortraitRogue";
                        PuzzleSystem puzzle = new PuzzleSystem(enemy, location);
                        puzzle.victoryDialogue = "rogue";
                        puzzle.defeatDialogue = "rogue";
                        puzzle.victoryEvent = puzzle.defeatEvent = new Runnable() {
                            @Override
                            public void run() {
                                Main.changeScreen(MapScreen.this);
                            }
                        };
                        Main.changeScreen(new Battle(puzzle));
                    }
                });
                dc.events.put("ranger", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = FighterComponent.getEnemy(Class.RANGER, 2);
                        enemy.portrait = "PortraitRanger";
                        PuzzleSystem puzzle = new PuzzleSystem(enemy, location);
                        puzzle.victoryDialogue = "ranger";
                        puzzle.defeatDialogue = "ranger";
                        puzzle.victoryEvent = puzzle.defeatEvent = new Runnable() {
                            @Override
                            public void run() {
                                Main.changeScreen(MapScreen.this);
                            }
                        };
                        Main.changeScreen(new Battle(puzzle));
                    }
                });
                dc.events.put("paladin", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = FighterComponent.getEnemy(Class.PALADIN, 3);
                        enemy.portrait = "PortraitPaladin";
                        PuzzleSystem puzzle = new PuzzleSystem(enemy, location);
                        puzzle.victoryDialogue = "paladin";
                        puzzle.defeatDialogue = "paladin";
                        puzzle.victoryEvent = puzzle.defeatEvent = new Runnable() {
                            @Override
                            public void run() {
                                Main.changeScreen(MapScreen.this);
                            }
                        };
                        Main.changeScreen(new Battle(puzzle));
                    }
                });
                dc.events.put("wizard", new Runnable() {
                    @Override
                    public void run() {
                        final FighterComponent enemy = FighterComponent.getEnemy(Class.WIZARD, 4);
                        enemy.portrait = "PortraitWizard";
                        PuzzleSystem puzzle = new PuzzleSystem(enemy, location);
                        puzzle.victoryDialogue = "wizard";
                        puzzle.defeatDialogue = "wizard";
                        puzzle.victoryEvent = puzzle.defeatEvent = new Runnable() {
                            @Override
                            public void run() {
                                Main.changeScreen(MapScreen.this);
                            }
                        };
                        Main.changeScreen(new Battle(puzzle));
                    }
                });
                Entity dialogue = new Entity();
                dialogue.add(dc);
                dialogue.add(new ActorComponent());
                location.engine.addEntity(dialogue);
                Main.changeScreen(location);
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
        stage.getRoot().getColor().a = 0;
        stage.addAction(Actions.fadeIn(Constants.TRANSITION_TIME));

        // is this the right way to do this?
        engine.getSystem(HUDSystem.class).update();
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
