package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Systems.PuzzleSystem;

public class TotemComponent implements Component {
    public Rune rune = Rune.DAMAGE;
    public boolean damaged = false;
    public FighterComponent caster = null;
}
