package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent implements Component {
    public Actor actor = new Actor();
    public boolean front = false;
}
