package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Systems.PuzzleSystem;

public class GlyphComponent implements Component {
    public String glyph = "Strike";
    public FighterComponent caster = PuzzleSystem.NULL_FIGHTER;
}
