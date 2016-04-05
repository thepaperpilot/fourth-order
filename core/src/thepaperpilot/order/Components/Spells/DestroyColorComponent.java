package thepaperpilot.order.Components.Spells;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Rune;

public class DestroyColorComponent implements Component {
    public Rune rune;

    public DestroyColorComponent(Rune rune) {
        this.rune = rune;
    }
}
