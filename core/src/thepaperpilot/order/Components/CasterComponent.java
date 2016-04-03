package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;

public class CasterComponent implements Component {
    public DestroyComponent.Target caster = DestroyComponent.Target.NULL;

    public CasterComponent(DestroyComponent.Target caster) {
        this.caster = caster;
    }
}
