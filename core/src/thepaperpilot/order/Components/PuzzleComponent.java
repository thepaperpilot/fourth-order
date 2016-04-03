package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Systems.PuzzleSystem;

public class PuzzleComponent implements Component {
    public PuzzleSystem puzzle;

    public PuzzleComponent(PuzzleSystem puzzle) {
        this.puzzle = puzzle;
    }
}
