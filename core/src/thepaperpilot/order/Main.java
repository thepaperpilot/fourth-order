package thepaperpilot.order;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Util.Constants;

public class Main extends Game implements Screen {
    public static final AssetManager manager = new AssetManager();

    public static Skin skin;
    public static Main instance;
    private static Stage loadingStage;

    public static void changeScreen(Screen screen) {
        if (screen == null)
            return;
        instance.setScreen(screen);
    }

    @Override
    public void create() {
        // use this so I can make a static changeScreen function
        // it basically makes Main a singleton
        instance = this;
        Player.setPreferences(Gdx.app.getPreferences("thepaperpilot.order.save"));

        if (Constants.PROFILING) GLProfiler.enable();

        // start loading all our assets
        manager.load("skin.json", Skin.class);
        manager.load("audio/bgm.wav", Music.class);

        changeScreen(this);
    }

    @Override
    public void show() {
        // show a basic loading screen
        loadingStage = new Stage(new ExtendViewport(200, 200));

        Label loadingLabel = new Label("Loading...", new Skin(Gdx.files.internal("skin.json")));
        loadingLabel.setFillParent(true);
        loadingLabel.setAlignment(Align.center);
        loadingStage.addActor(loadingLabel);
        loadingStage.addAction(Actions.sequence(Actions.alpha(0), Actions.forever(Actions.sequence(Actions.fadeIn(1), Actions.fadeOut(1)))));

        // basically a sanity check? loadingStage shouldn't have any input listeners
        // but I guess this'll help if the inputprocessor gets set to something it shouldn't
        Gdx.input.setInputProcessor(loadingStage);
    }

    @Override
    public void render(float delta) {
        // render the loading screen
        // act shouldn't do anything, but putting it here is good practice, I guess?
        loadingStage.act();
        loadingStage.draw();

        // continue loading. If complete, do shit
        if (manager.update()) {
            if (skin == null) {
                skin = manager.get("skin.json", Skin.class);
                //skin.getFont("large").getData().setScale(.5f);
                skin.getFont("large").getData().markupEnabled = true;
                skin.getFont("font").getData().setScale(.5f);
                skin.getFont("font").getData().markupEnabled = true;

                Music bgm = manager.get("audio/bgm.wav", Music.class);
                bgm.setLooping(true);
                bgm.play();
            }

            DialogueComponent dc = new DialogueComponent();
            DialogueScreen ds = new DialogueScreen(dc);
            if (Player.hasPlayer()) {
                dc.lines = new Dialogue.Line[]{getOpponentSelector(ds)};
            } else {
                dc.lines = new Dialogue.Line[]{getClassSelector(), getOpponentSelector(ds)};
            }
            changeScreen(ds);
        }
    }

    private Dialogue.Line getOpponentSelector(final DialogueScreen ds) {
        final Runnable reset = new Runnable() {
            @Override
            public void run() {
                Main.changeScreen(Main.instance);
            }
        };
        Dialogue.Line line = new Dialogue.Line("There are 5 guardians in the Fourth Order, one for each element. Who should I go beat up?", "Player", 0);
        line.options = new Dialogue.Option[6];
        line.options[0] = new Dialogue.Option("Poison");
        line.options[0].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitAlchemist"};
                dc.lines = new Dialogue.Line[3];
                dc.lines[0] = new Dialogue.Line("ra ra ra, I'm gonna beat you up", "Guardian of Poison", 1);
                dc.lines[1] = new Dialogue.Line("pls no", "Player", 0);
                dc.lines[2] = new Dialogue.Line("haha like I give a... (NSFW)", "Guardian of Poison", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitAlchemist";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getTruthSpell());
                enemy.add(SpellComponent.getWitherSpell());
                DialogueComponent victory = new DialogueComponent();
                victory.enemies = new String[]{"PortraitAlchemist"};
                victory.lines = new Dialogue.Line[1];
                victory.lines[0] = new Dialogue.Line("Maybe...\nI was the one beat up...", "Guardian of Poison", 1);
                victory.lines[0].events = reset;
                DialogueComponent loss = new DialogueComponent();
                loss.enemies = new String[]{"PortraitAlchemist"};
                loss.lines = new Dialogue.Line[1];
                loss.lines[0] = new Dialogue.Line("Ha... And the strong continue to belittle the weak. Come back when you're older, nerd", "Alchemist", 1);
                loss.lines[0].events = reset;
                enemy.victory = victory;
                enemy.loss = loss;
                dc.lines[2].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                ds.engine.addEntity(dialogue);
            }
        };
        line.options[1] = new Dialogue.Option("Surprise");
        line.options[1].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitRogue"};
                dc.lines = new Dialogue.Line[3];
                dc.lines[0] = new Dialogue.Line("zip zip, I'm coming for you", "Guardian of Surprise", 1);
                dc.lines[1] = new Dialogue.Line("???\n\nwha...?", "Player", 0);
                dc.lines[2] = new Dialogue.Line("ur goin down m8", "Rogue", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitRogue";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getCondenseSpell());
                enemy.add(SpellComponent.getCollectSpell());
                DialogueComponent victory = new DialogueComponent();
                victory.enemies = new String[]{"PortraitRogue"};
                victory.lines = new Dialogue.Line[1];
                victory.lines[0] = new Dialogue.Line("I'll seek vengeance!", "Guardian of Surprise", 1);
                victory.lines[0].events = reset;
                DialogueComponent loss = new DialogueComponent();
                loss.enemies = new String[]{"PortraitRogue"};
                loss.lines = new Dialogue.Line[1];
                loss.lines[0] = new Dialogue.Line("And may your pockets be lighter.", "Guardian of Surprise", 1);
                loss.lines[0].events = reset;
                enemy.victory = victory;
                enemy.loss = loss;
                dc.lines[2].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                ds.engine.addEntity(dialogue);
            }
        };
        line.options[2] = new Dialogue.Option("Mason");
        line.options[2].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitWizard"};
                dc.lines = new Dialogue.Line[1];
                dc.lines[0] = new Dialogue.Line("...", "Guardian of Mason", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitWizard";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getPremonitionSpell());
                DialogueComponent victory = new DialogueComponent();
                victory.enemies = new String[]{"PortraitWizard"};
                victory.lines = new Dialogue.Line[1];
                victory.lines[0] = new Dialogue.Line("...", "Guardian of Mason", 1);
                victory.lines[0].events = reset;
                DialogueComponent loss = new DialogueComponent();
                loss.enemies = new String[]{"PortraitWizard"};
                loss.lines = new Dialogue.Line[1];
                loss.lines[0] = new Dialogue.Line("...", "Guardian of Mason", 1);
                loss.lines[0].events = reset;
                enemy.victory = victory;
                enemy.loss = loss;
                dc.lines[0].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                ds.engine.addEntity(dialogue);
            }
        };
        line.options[3] = new Dialogue.Option("Steam");
        line.options[3].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitPaladin"};
                dc.lines = new Dialogue.Line[1];
                dc.lines[0] = new Dialogue.Line("I will stop you, in the name of [YELLOW]The Light![]", "Guardian of Steam", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitPaladin";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getImmortalitySpell());
                enemy.add(SpellComponent.getSustainSpell());
                DialogueComponent victory = new DialogueComponent();
                victory.enemies = new String[]{"PortraitPaladin"};
                victory.lines = new Dialogue.Line[1];
                victory.lines[0] = new Dialogue.Line("The Light...\nIt has forsaken me...", "Guardian of Steam", 1);
                victory.lines[0].events = reset;
                DialogueComponent loss = new DialogueComponent();
                loss.enemies = new String[]{"PortraitPaladin"};
                loss.lines = new Dialogue.Line[1];
                loss.lines[0] = new Dialogue.Line("Another evil vanquished", "Guardian of Steam", 1);
                loss.lines[0].events = reset;
                enemy.victory = victory;
                enemy.loss = loss;
                dc.lines[0].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                ds.engine.addEntity(dialogue);
            }
        };
        line.options[4] = new Dialogue.Option("Mortal");
        line.options[4].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitRanger"};
                dc.lines = new Dialogue.Line[1];
                dc.lines[0] = new Dialogue.Line("Beware of my traps!", "Guardian of Mortal", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitRanger";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getAntidoteSpell());
                enemy.add(SpellComponent.getCommandSpell());
                DialogueComponent victory = new DialogueComponent();
                victory.enemies = new String[]{"PortraitRanger"};
                victory.lines = new Dialogue.Line[1];
                victory.lines[0] = new Dialogue.Line("I got caught in one of your traps instead :(", "Guardian of Mortal", 1);
                victory.lines[0].events = reset;
                DialogueComponent loss = new DialogueComponent();
                loss.enemies = new String[]{"PortraitRanger"};
                loss.lines = new Dialogue.Line[1];
                loss.lines[0] = new Dialogue.Line("Another successful hunt!", "Guardian of Mortal", 1);
                loss.lines[0].events = reset;
                enemy.victory = victory;
                enemy.loss = loss;
                dc.lines[0].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                ds.engine.addEntity(dialogue);
            }
        };
        line.options[5] = new Dialogue.Option("YOURSELF (bonus level)");
        line.options[5].events = new Runnable() {
            @Override
            public void run() {
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitPlayer";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getRefreshSpell());
                Main.changeScreen(new Battle(17, enemy));
            }
        };
        return line;
    }

    private Dialogue.Line getClassSelector() {
        Dialogue.Line line = new Dialogue.Line("What is my class?", "Player", 0);
        line.options = new Dialogue.Option[5];
        line.options[0] = new Dialogue.Option("Alchemist");
        line.options[0].events = new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getTruthSpell());
            }
        };
        line.options[1] = new Dialogue.Option("Rogue");
        line.options[1].events = new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getCondenseSpell());
            }
        };
        line.options[2] = new Dialogue.Option("Ranger");
        line.options[2].events = new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getAntidoteSpell());
            }
        };
        line.options[3] = new Dialogue.Option("Paladin");
        line.options[3].events = new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getImmortalitySpell());
            }
        };
        line.options[4] = new Dialogue.Option("Wizard");
        line.options[4].events = new Runnable() {
            @Override
            public void run() {
                Player.getPlayer().add(SpellComponent.getPremonitionSpell());
            }
        };
        return line;
    }

    @Override
    public void hide() {
        /// we're a good garbage collector
        loadingStage.dispose();
    }

    @Override
    public void pause() {
        // we're a passthrough!
        if (getScreen() == this) return;
        super.pause();
    }

    @Override
    public void resume() {
        // we're a passthrough!
        if (getScreen() == this) return;
        super.pause();
    }

    @Override
    public void resize(int width, int height) {
        // we're a passthrough!
        if (getScreen() == this) return;
        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }

    @Override
    public void dispose() {
        // we're a passthrough!
        if (getScreen() == this) return;
        if (getScreen() != null) {
            getScreen().dispose();
        }
        // also clean up our shit
        manager.dispose();
        skin.dispose();
    }

    @Override
    public void render() {
        // we're a passthrough!
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getScreen().render(Gdx.graphics.getDeltaTime() * Constants.DELTA_MOD);

        if (Constants.PROFILING) {
            System.out.println("calls: " + GLProfiler.calls);
            System.out.println("drawCalls: " + GLProfiler.drawCalls);
            System.out.println("shaderSwitches: " + GLProfiler.shaderSwitches);
            System.out.println("textureBindings: " + GLProfiler.textureBindings);
            System.out.println("vertexCount: " + GLProfiler.vertexCount.total);
            System.out.println();
            GLProfiler.reset();
        }
    }

    public static Texture getTexture(String name) {
        name += ".png";
        manager.load(name, Texture.class);
        manager.finishLoadingAsset(name);
        return Main.manager.get(name, Texture.class);
    }

    public static void playSound(String sound) {
        sound = "audio/" + sound;
        manager.load(sound, Sound.class);
        manager.finishLoading();
        Main.manager.get(sound, Sound.class).play(Constants.MASTER_VOLUME);
    }
}
