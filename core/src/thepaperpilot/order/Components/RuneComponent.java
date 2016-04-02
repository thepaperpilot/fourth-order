package thepaperpilot.order.Components;

import com.badlogic.ashley.core.Component;

public class RuneComponent implements Component {
    public float poison = 0;
    public float surprise = 0;
    public float mortal = 0;
    public float steam = 0;
    public float mason = 0;

    public float exp = 0;
    public float damage = 0;

    public boolean matches(RuneComponent mc) {
        return poison == mc.poison &&
                surprise == mc.surprise &&
                mortal == mc.mortal &&
                steam == mc.steam &&
                exp == mc.exp &&
                damage == mc.damage;
    }

    public String toString() {
        return poison + "," + surprise + "," + mortal + "," + steam + "," + mason;
    }
}
