package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Systems.PuzzleSystem;

public class StatusEffectComponent implements Component {
    public int turns = 0;
    public FighterComponent target = PuzzleSystem.NULL_FIGHTER;
}
