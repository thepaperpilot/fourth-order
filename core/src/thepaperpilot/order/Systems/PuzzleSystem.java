package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class PuzzleSystem extends EntitySystem {
    public final int size;
    public Entity[][] runes;
    public Entity selected;
    float[] cooldown;
    public DestroyComponent.Target turn = DestroyComponent.Target.NULL;

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
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkForHorizontalMatches(i, j, true)) {
                    runes[i][j].add(new DestroyComponent(turn));
                    runes[i + 1][j].add(new DestroyComponent(turn));
                    runes[i + 2][j].add(new DestroyComponent(turn));
                    RuneComponent rc = Mappers.rune.get(runes[i][j]);
                    int matched = 3;
                    for (int k = i + 3; k < size; k++) {
                        if (runes[k][j] != null && rc.matches(Mappers.rune.get(runes[k][j]))) {
                            matched++;
                            runes[k][j].add(new DestroyComponent(turn));
                            if (matched == 4) {
                                // match 4
                            }
                            if (matched == 5) {
                                // match 5
                            }
                        } else break;
                    }
                }
                if (checkForVerticalMatches(i, j, true)) {
                    runes[i][j].add(new DestroyComponent(turn));
                    runes[i][j + 1].add(new DestroyComponent(turn));
                    runes[i][j + 2].add(new DestroyComponent(turn));
                    RuneComponent rc = Mappers.rune.get(runes[i][j]);
                    int matched = 3;
                    for (int k = j + 3; k < size; k++) {
                        if (runes[k][j] != null && rc.matches(Mappers.rune.get(runes[i][k]))) {
                            matched++;
                            runes[i][k].add(new DestroyComponent(turn));
                            if (matched == 4) {
                                // match 4
                            }
                            if (matched == 5) {
                                // match 5
                            }
                        } else break;
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
        RuneComponent rc = new RuneComponent();
        IdleAnimationComponent ic = new IdleAnimationComponent();
        UIComponent uc = new UIComponent();
        pc.x = column;
        pc.y = 0;
        switch (MathUtils.random(6)) {
            case 0:
                rc.poison = 1;
                ic.file = "PurpleGem";
                break;
            case 1:
                rc.surprise = 1;
                ic.file = "YellowGem";
                break;
            case 2:
                rc.mortal = 1;
                ic.file = "RedGem";
                break;
            case 3:
                rc.steam = 1;
                ic.file = "BlueGem";
                break;
            case 4:
                rc.mason = 1;
                ic.file = "GreenGem";
                break;
            case 5:
                rc.exp = 1;
                ic.file = "XPGem";
                ic.chance = 1;
                break;
            case 6:
                rc.damage = 1;
                ic.file = "DamageRune";
                ic.chance = 1;
                break;
        }
        rune.add(pc);
        rune.add(rc);
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
        runes[pc.x][pc.y] = runes[pc.x][targetY];
        runes[pc.x][targetY] = rune;
        pc.y = targetY;
        uc.actor.addAction(moveRuneAction(rune));
    }

    private MoveToAction moveRuneAction(Entity entity) {
        PuzzleComponent pc = Mappers.puzzle.get(entity);
        UIComponent uc = Mappers.ui.get(entity);

        Vector2 dest = new Vector2(Constants.UI_WIDTH + (pc.x + .125f) * getRuneSize(), Constants.WORLD_HEIGHT - (pc.y + 1) * getRuneSize());
        float dist = dest.dst(uc.actor.getX(), uc.actor.getY());
        return Actions.moveTo(dest.x, dest.y, dist / Constants.TILE_SPEED, Interpolation.pow2In);
    }

    public void select(final Entity entity) {
        if (selected == null) {
            selected = entity;
            entity.add(new SelectedComponent());
        } else if (selected == entity) {
            selected = null;
            entity.remove(SelectedComponent.class);
        } else {
            final PuzzleComponent pc1 = Mappers.puzzle.get(entity);
            final PuzzleComponent pc2 = Mappers.puzzle.get(selected);
            if (Math.abs(pc1.x - pc2.x) == 1 && Math.abs(pc1.y - pc2.y) == 0 || Math.abs(pc1.x - pc2.x) == 0 && Math.abs(pc1.y - pc2.y) == 1) {
                int tempX = pc1.x;
                int tempY = pc1.y;
                pc1.x = pc2.x;
                pc1.y = pc2.y;
                pc2.x = tempX;
                pc2.y = tempY;
                runes[pc1.x][pc1.y] = entity;
                runes[pc2.x][pc2.y] = selected;
                if (checkForMatches()) {
                    updateRune(entity);
                    updateRune(selected);
                } else {
                    final Entity selected = this.selected;
                    final UIComponent uc1 = Mappers.ui.get(entity);
                    final UIComponent uc2 = Mappers.ui.get(selected);
                    uc1.actor.addAction(Actions.sequence(moveRuneAction(entity), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            // play error sound
                            uc1.actor.addAction(moveRuneAction(entity));
                            uc2.actor.addAction(moveRuneAction(selected));
                        }
                    })));
                    uc2.actor.addAction(moveRuneAction(selected));
                    tempX = pc1.x;
                    tempY = pc1.y;
                    pc1.x = pc2.x;
                    pc1.y = pc2.y;
                    pc2.x = tempX;
                    pc2.y = tempY;
                    runes[pc1.x][pc1.x] = entity;
                    runes[pc2.x][pc2.y] = selected;
                }
                selected.remove(SelectedComponent.class);
                selected = null;
            } else {
                selected.remove(SelectedComponent.class);
                selected = entity;
                selected.add(new SelectedComponent());
            }
        }
    }

    public boolean checkForMatches() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkForHorizontalMatches(i, j, false) || checkForVerticalMatches(i, j, false)) return true;
            }
        }
        return false;
    }

    private boolean checkForHorizontalMatches(int x, int y, boolean visual) {
        if (x >= size - 2) return false;
        if (runes[x][y] == null || runes[x + 1][y] == null || runes[x + 2][y] == null) return false;

        RuneComponent rc = Mappers.rune.get(runes[x][y]);

        return !(visual && (Mappers.ui.get(runes[x][y]).actor.hasActions() || Mappers.ui.get(runes[x + 1][y]).actor.hasActions() || Mappers.ui.get(runes[x + 2][y]).actor.hasActions())) && rc.matches(Mappers.rune.get(runes[x + 1][y])) && rc.matches(Mappers.rune.get(runes[x + 2][y]));

    }

    private boolean checkForVerticalMatches(int x, int y, boolean visual) {
        if (y >= size - 2) return false;
        if (runes[x][y] == null || runes[x][y + 1] == null || runes[x][y + 2] == null) return false;

        RuneComponent rc = Mappers.rune.get(runes[x][y]);

        return !(visual && (Mappers.ui.get(runes[x][y]).actor.hasActions() || Mappers.ui.get(runes[x][y + 2]).actor.hasActions() || Mappers.ui.get(runes[x][y + 2]).actor.hasActions())) && rc.matches(Mappers.rune.get(runes[x][y + 1])) && rc.matches(Mappers.rune.get(runes[x][y + 2]));

    }
}