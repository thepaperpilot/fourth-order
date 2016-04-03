package thepaperpilot.order.Components.Spells;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import thepaperpilot.order.Util.Constants;

public class RefreshComponent implements Component {
    public int number = MathUtils.random(Constants.DESTROY_LOW, Constants.DESTROY_HIGH);
}
