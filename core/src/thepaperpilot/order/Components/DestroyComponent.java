package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;

public class DestroyComponent implements Component {

    public final FighterComponent collector;

    public DestroyComponent(FighterComponent collector) {
        this.collector = collector;
    }

    public enum Target {
        NULL,
        PLAYER,
        ENEMY
    }
}
