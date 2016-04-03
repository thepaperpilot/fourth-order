package thepaperpilot.order;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.SpellComponent;
import thepaperpilot.order.Systems.DialogueSystem;
import thepaperpilot.order.Systems.RenderStageSystem;
import thepaperpilot.order.Util.Constants;

public class DialogueScreen implements Screen { //possible for things other than dialogues
    public final Stage ui;
    public final Engine engine;

    public DialogueScreen() {
        /* Create Stuff */
        ui = new Stage(new StretchViewport(Constants.WORLD_WIDTH * 2, Constants.WORLD_HEIGHT * 2)); // rebel
        engine = new Engine();

        /* Add Listeners to Engine */

        /* Add Systems to Engine */
        engine.addSystem(new DialogueSystem(ui)); //priority 15
        engine.addSystem(new RenderStageSystem(ui)); //priority 10

        // testing
        DialogueComponent dc = new DialogueComponent();
        dc.lines = new Dialogue.Line[1];
        dc.lines[0] = new Dialogue.Line("who should I beat up?", "Player", 0);
        dc.lines[0].options = new Dialogue.Option[3];
        dc.lines[0].options[0] = new Dialogue.Option("The Alchemist");
        dc.lines[0].options[0].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitAlchemist"};
                dc.lines = new Dialogue.Line[3];
                dc.lines[0] = new Dialogue.Line("ra ra ra, I'm gonna beat you up", "Alchemist", 1);
                dc.lines[1] = new Dialogue.Line("pls no", "Player", 0);
                dc.lines[2] = new Dialogue.Line("haha like I give a... (NSFW)", "Alchemist", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitAlchemist";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getTruthSpell());
                dc.lines[2].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                engine.addEntity(dialogue);
            }
        };
        dc.lines[0].options[1] = new Dialogue.Option("The Rogue");
        dc.lines[0].options[1].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitRogue"};
                dc.lines = new Dialogue.Line[3];
                dc.lines[0] = new Dialogue.Line("zip zip, I'm coming for you", "Rogue", 1);
                dc.lines[1] = new Dialogue.Line("???\n\nwha...?", "Player", 0);
                dc.lines[2] = new Dialogue.Line("ur goin down m8", "Rogue", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitRogue";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getCondenseSpell());
                dc.lines[2].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                engine.addEntity(dialogue);
            }
        };
        dc.lines[0].options[2] = new Dialogue.Option("The Wizard");
        dc.lines[0].options[2].events = new Runnable() {
            @Override
            public void run() {
                DialogueComponent dc = new DialogueComponent();
                dc.enemies = new String[]{"PortraitWizard"};
                dc.lines = new Dialogue.Line[1];
                dc.lines[0] = new Dialogue.Line("...", "Wizard", 1);
                final FighterComponent enemy = new FighterComponent();
                enemy.portrait = "PortraitWizard";
                enemy.add(SpellComponent.getStrikeSpell());
                enemy.add(SpellComponent.getPremonitionSpell());
                dc.lines[0].events = new Runnable() {
                    @Override
                    public void run() {
                        Main.changeScreen(new Battle(enemy));
                    }
                };
                Entity dialogue = new Entity();
                dialogue.add(dc);
                engine.addEntity(dialogue);
            }
        };
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
