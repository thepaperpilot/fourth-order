package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class IdleAnimationComponent implements Component {
    public String file;
    public Animation animation;
    public boolean animating;
    public float time;
}
