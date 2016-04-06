package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Systems.DialogueSystem;
import thepaperpilot.order.UI.ScrollText;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class DialogueListener implements EntityListener {

    private Stage stage;
    private Engine engine;

    public DialogueListener(Stage stage, Engine engine) {
        this.stage = stage;
        this.engine = engine;
    }

    @Override
    public void entityAdded(final Entity entity) {
        final DialogueComponent dc = Mappers.dialogue.get(entity);
        ActorComponent ac = Mappers.actor.get(entity);

        dc.playerFace = new Image(Main.getTexture(dc.player));
        dc.actorFace = new Image();

        dc.messageLabel = new ScrollText();
        dc.messageLabel.setAlignment(Align.topLeft);
        dc.messageLabel.setWrap(true);
        dc.message = new Table(Main.skin);
        dc.message.top().left();
        dc.message.setBackground(Main.skin.getDrawable("default-round"));
        dc.message.pad(20);

        Table faces = new Table(Main.skin);
        faces.bottom().left().add(dc.playerFace).width(3 * Constants.FACE_SIZE).height(4 * Constants.FACE_SIZE).expand().bottom().left();
        faces.add(dc.actorFace).width(3 * Constants.FACE_SIZE).height(4 * Constants.FACE_SIZE).bottom().right();

        Table dialogue = new Table(Main.skin);
        dialogue.pad(8).setFillParent(true);
        dialogue.bottom().left().add(faces).expand().fill().padBottom(4).row();
        dialogue.add(dc.message).colspan(2).expandX().fillX().height(Constants.DIALOGUE_SIZE);
        ac.actor = dialogue;
        ac.front = true;

        ac.actor.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                engine.getSystem(DialogueSystem.class).advance(entity, false);
                return false;
            }

            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E:
                    case Input.Keys.ENTER:
                    case Input.Keys.SPACE:
                        engine.getSystem(DialogueSystem.class).advance(entity, true);
                        break;
                    case Input.Keys.UP:
                    case Input.Keys.W:
                    case Input.Keys.A:
                        DialogueSystem.moveSelection(entity, -1);
                        break;
                    case Input.Keys.DOWN:
                    case Input.Keys.S:
                    case Input.Keys.D:
                        DialogueSystem.moveSelection(entity, 1);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        stage.setKeyboardFocus(ac.actor);
        engine.getSystem(DialogueSystem.class).next(entity, dc.start);
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
