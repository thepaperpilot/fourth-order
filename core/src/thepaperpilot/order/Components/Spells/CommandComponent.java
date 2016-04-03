package thepaperpilot.order.Components.Spells;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Components.FighterComponent;

public class CommandComponent implements Component {
    public float mulDamage = 2;
    public FighterComponent caster;
    public boolean damaged = false;
}
