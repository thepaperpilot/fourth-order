package thepaperpilot.order.Components.Spells;

import com.badlogic.ashley.core.Component;

public class DestroyColorComponent implements Component {
    public int color;
    //  0 - poison
    //  1 - surprise
    //  2 - mortal
    //  3 - steam
    //  4 - mason

    public DestroyColorComponent(int color) {
        this.color = color;
    }
}
