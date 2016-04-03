package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;

public class MessageComponent implements Component {
    public String message = "";
    public int x = 500;
    public int y = 300;

    public MessageComponent(String message) {
        this.message = message;
    }
}
