package thepaperpilot.order;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import thepaperpilot.order.Util.Constants;

public class Main extends Game implements Screen {
    public static final AssetManager manager = new AssetManager();

    public static Skin skin;
    public static Main instance;
    private static Sound bgm;
    private static long bgmId;
    private static Sound newBGM;
    private static long newId;
    private static float transition = 1;
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
        // TODO make a giant texture of all the textures with an atlas file and an tsx file
        // and make it have 2 pixels between the tiles so there won't be black lines
        // and ideally have it filled with original art, not Kenney's (but his is cc0, so we can use it for now/ever. no rush)
        manager.load("skin.json", Skin.class);

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
                skin.getFont("large").getData().setScale(.5f);
                skin.getFont("large").getData().markupEnabled = true;
                skin.getFont("font").getData().setScale(.25f);
                skin.getFont("font").getData().markupEnabled = true;

                // temp battle
                changeScreen(new Battle());
            }
        }
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

        // Transition bgms
        if (transition != 1) {
            if (transition > 1 || bgm == null) {
                transition = 1;
                if (bgm != null) bgm.stop(bgmId);
                bgm = newBGM;
                bgmId = newId;
                bgm.setVolume(bgmId, .5f);
                newBGM = null;
            } else {
                transition += Gdx.graphics.getDeltaTime();
                bgm.setVolume(bgmId, (1 - transition) / 2);
                newBGM.setVolume(newId, transition / 2);
            }
        }

        getScreen().render(Gdx.graphics.getDeltaTime());

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

    public static void changeBGM(String bgm) {
        bgm = "BGM/" + bgm;
        manager.load(bgm, Sound.class);
        manager.finishLoadingAsset(bgm);
        if (newBGM != null && newBGM == manager.get(bgm, Sound.class)) return;
        newBGM = manager.get(bgm, Sound.class);
        if (Main.bgm != newBGM) {
            transition = 0;
            newId = newBGM.loop(.5f);
        }
    }

    public static void click() {
        Main.manager.get("SFX/click1.ogg", Sound.class).play(1, MathUtils.random(.5f, 2), 0);
    }
}
