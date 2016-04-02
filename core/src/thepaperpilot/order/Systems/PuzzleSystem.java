package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Components.IdleAnimationComponent;
import thepaperpilot.order.Components.ManaComponent;
import thepaperpilot.order.Components.PuzzleComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class PuzzleSystem extends EntitySystem {
    public final int size;
    public Entity[][] runes;
    float[] cooldown;

    public PuzzleSystem(int size) {
        this.size = size;
        runes = new Entity[size][size];
        cooldown = new float[size];
    }

    public void update (float deltaTime) {
        for (int i = 0; i < size; i++) {
            cooldown[i] += deltaTime;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (runes[i][j] == null) {
                    if (j == 0) {
                        if (cooldown[i] > 2 * getRuneSize() / Constants.TILE_SPEED) {
                            createRune(i);
                            cooldown[i] = 0;
                        }
                    } else if (runes[i][j - 1] != null){
                        lowerRune(runes[i][j - 1]);
                    }
                }
            }
        }
    }

    public int getRuneSize() {
        return (Constants.WORLD_WIDTH - 2 * Constants.UI_WIDTH) / size;
    }

    private void createRune(int column) {
        Entity rune = new Entity();
        PuzzleComponent pc = new PuzzleComponent(this);
        ManaComponent mc = new ManaComponent();
        IdleAnimationComponent ic = new IdleAnimationComponent();
        UIComponent uc = new UIComponent();
        pc.x = column;
        pc.y = 0;
        switch (MathUtils.random(4)) {
            case 0:
                mc.poison = 1;
                ic.file = "PurpleGem";
                break;
            case 1:
                mc.surprise = 1;
                ic.file = "YellowGem";
                break;
            case 2:
                mc.mortal = 1;
                ic.file = "RedGem";
                break;
            case 3:
                mc.steam = 1;
                ic.file = "BlueGem";
                break;
            case 4:
                mc.mason = 1;
                ic.file = "GreenGem";
                break;
        }
        rune.add(pc);
        rune.add(mc);
        rune.add(ic);
        rune.add(uc);
        getEngine().addEntity(rune);
    }

    public void lowerRune(Entity rune) {
        PuzzleComponent pc = Mappers.puzzle.get(rune);
        UIComponent uc = Mappers.ui.get(rune);

        int targetY = pc.y;
        for (int i = pc.y + 1; i < size; i++) {
            if (runes[pc.x][i] == null) {
                targetY = i;
            } else break;
        }
        runes[pc.x][pc.y] = null;
        runes[pc.x][targetY] = rune;
        pc.y = targetY;
        Vector2 dest = new Vector2(Constants.UI_WIDTH + (pc.x + .125f) * getRuneSize(), Constants.WORLD_HEIGHT - (pc.y + 1) * getRuneSize());
        float dist = dest.dst(uc.actor.getX(), uc.actor.getY());
        uc.actor.addAction(Actions.moveTo(dest.x, dest.y, dist / Constants.TILE_SPEED, Interpolation.pow2In));
    }
}
