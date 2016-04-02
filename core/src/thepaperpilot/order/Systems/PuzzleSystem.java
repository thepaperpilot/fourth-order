package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class PuzzleSystem extends EntitySystem {
    public final int size;
    public Entity[][] runes;
    public Entity selected;
    float[] cooldown;

    public PuzzleSystem(int size) {
        super(5);
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
                        updateRune(runes[i][j - 1]);
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

    public void updateRune(Entity rune) {
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

    public void select(Entity entity) {
        if (selected == null) {
            selected = entity;
            entity.add(new SelectedComponent());
        } else if (selected == entity) {
            selected = null;
            entity.remove(SelectedComponent.class);
        } else {
            PuzzleComponent pc1 = Mappers.puzzle.get(entity);
            PuzzleComponent pc2 = Mappers.puzzle.get(selected);
            if (Math.abs(pc1.x - pc2.x) == 1 && Math.abs(pc1.y - pc2.y) == 0 || Math.abs(pc1.x - pc2.x) == 0 && Math.abs(pc1.y - pc2.y) == 1) {
                // TODO check if move makes a match, reverse move if not
                int tempX = pc1.x;
                int tempY = pc1.y;
                pc1.x = pc2.x;
                pc1.y = pc2.y;
                pc2.x = tempX;
                pc2.y = tempY;
                updateRune(entity);
                updateRune(selected);
                selected.remove(SelectedComponent.class);
                selected = null;
            } else {
                selected.remove(SelectedComponent.class);
                selected = entity;
                selected.add(new SelectedComponent());
            }
        }
    }
}
