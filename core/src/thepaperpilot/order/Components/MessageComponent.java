package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class MessageComponent implements Component {
    public String message = "";
    public float x = 500;
    public float y = 300;
    public Color color = Color.WHITE;
    public boolean large = true;

    public MessageComponent(String message) {
        this.message = message;
    }
}
