package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import thepaperpilot.order.Dialogue;

public class DialogueComponent implements Component {
    public Dialogue.Line[] lines = new Dialogue.Line[]{};

    public String player = "PortraitPlayer";
    public String[] enemies = new String[]{};
}
