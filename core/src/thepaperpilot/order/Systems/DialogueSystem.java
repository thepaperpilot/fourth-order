package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import thepaperpilot.order.Components.DialogueComponent;
import thepaperpilot.order.Dialogue;
import thepaperpilot.order.Util.Mappers;

public class DialogueSystem extends IteratingSystem {

    private Stage stage;

    public DialogueSystem(Stage stage) {
        super(Family.all(DialogueComponent.class).get(), 5);
        this.stage = stage;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DialogueComponent dc = Mappers.dialogue.get(entity);

        new Dialogue(dc.lines, dc.player, dc.enemies).open(stage);

        entity.remove(DialogueComponent.class);
    }
}
