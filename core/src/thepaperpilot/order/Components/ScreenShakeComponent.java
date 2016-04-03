package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;

public class ScreenShakeComponent implements Component {
    public int shake = 0;

    public ScreenShakeComponent(int shake) {
        this.shake = shake;
    }
}
