package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;

public class DestroyComponent implements Component {

    public Target target = Target.NULL;

    public DestroyComponent(Target target) {
        this.target = target;
    }

    public enum Target {
        NULL,
        PLAYER,
        ENEMY;
    }
}
