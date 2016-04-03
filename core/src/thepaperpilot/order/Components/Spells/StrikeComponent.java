package thepaperpilot.order.Components.Spells;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Components.FighterComponent;

public class StrikeComponent implements Component {
    public int damage = 3;
    public FighterComponent caster;
}
