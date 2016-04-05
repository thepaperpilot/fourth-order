package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Systems.PuzzleSystem;

public class TotemComponent implements Component {
    public String color = "Skull";
    public boolean damaged = false;
    public FighterComponent caster = PuzzleSystem.NULL_FIGHTER;
}
