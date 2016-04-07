package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.order.Components.ActorComponent;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.UI.Line;
import thepaperpilot.order.UI.Option;
import thepaperpilot.order.Util.Mappers;

public class DialogueSystem extends IteratingSystem {

    public DialogueSystem() {
        super(Family.all(DialogueComponent.class).get(), 5);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DialogueComponent dc = Mappers.dialogue.get(entity);

        if (dc.timer != 0) {
            dc.timer -= deltaTime;
            if (dc.timer <= 0) {
                next(entity, dc.lines.get(dc.line).next);
            }
        }
    }

    public void advance(Entity entity, boolean override) {
        DialogueComponent dc = Mappers.dialogue.get(entity);

        if (override || dc.lines.get(dc.line).options.length == 0) {
            if (dc.messageLabel.isFinished()) {
                if (dc.lines.get(dc.line).options.length == 0) next(entity, dc.lines.get(dc.line).next);
                else if (dc.selected != null) dc.selected.select(entity, this);
            } else {
                dc.messageLabel.finish();
            }
        }
    }

    public void next(Entity entity, String start) {
        DialogueComponent dc = Mappers.dialogue.get(entity);

        if (dc.line == null)
            dc.line = start;
        else if (dc.events.get(dc.lines.get(dc.line).event) != null)
            dc.events.get(dc.lines.get(dc.line).event).run();

        if (!dc.lines.containsKey(start)) {
            getEngine().removeEntity(entity);
            return;
        }

        // update the dialogue stage for the next part of the dialogue
        Line line = dc.lines.get(dc.line = start);
        if (line.face == 0) {
            dc.playerFace.setColor(1, 1, 1, 1);
            dc.actorFace.setColor(.5f, .5f, .5f, 1);
        } else {
            dc.playerFace.setColor(.5f, .5f, .5f, 1);
            dc.actorFace.setDrawable(new TextureRegionDrawable(new TextureRegion(Main.getTexture(dc.actors[line.face - 1]))));
            dc.actorFace.setColor(1, 1, 1, 1);
        }
        dc.messageLabel.setMessage((line.name != null ? "[GOLD]" + line.name + "[]\n" : "") + line.message);
        dc.message.clearChildren();
        dc.message.add(dc.messageLabel).expandX().fillX().left().padBottom(20).row();
        dc.timer = line.timer;
        if (line.options.length == 0) {
            if (line.timer == 0)
                dc.message.add(new Label("Click to continue...", Main.skin)).expand().center().bottom();
        } else {
            for (int i = 0; i < line.options.length; i++) {
                line.options[i].reset(entity, this);
                dc.message.add(line.options[i].label).left().padLeft(10).row();
            }
            dc.selected = line.options[0];
            updateSelected(entity);
        }
        if (line.timer == 0) {
            ActorComponent ac = Mappers.actor.get(entity);
            ac.actor.setTouchable(line.options.length == 0 ? Touchable.enabled : Touchable.childrenOnly);
        }
    }

    public static void moveSelection(Entity entity, int amount) {
        DialogueComponent dc = Mappers.dialogue.get(entity);

        if (dc.selected == null) return;
        Line currLine = dc.lines.get(dc.line);
        for (int i = 0; i < currLine.options.length; i++) {
            if (dc.selected == currLine.options[i]) {
                if (i + amount < 0)
                    dc.selected = currLine.options[currLine.options.length - 1];
                else if (i + amount >= currLine.options.length)
                    dc.selected = currLine.options[0];
                else dc.selected = currLine.options[i + amount];
                break;
            }
        }
        updateSelected(entity);
    }

    public static void updateSelected(Entity entity) {
        DialogueComponent dc = Mappers.dialogue.get(entity);

        for (int i = 0; i < dc.lines.get(dc.line).options.length; i++) {
            Option option = dc.lines.get(dc.line).options[i];
            if (dc.selected == option) {
                option.label.setText(" > " + option.message);
                option.label.setColor(Color.ORANGE);
            } else {
                option.label.setText("> " + option.message);
                option.label.setColor(Color.WHITE);
            }
        }
    }
}
