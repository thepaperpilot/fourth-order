package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

import java.util.Collections;

public class PuzzleSystem extends EntitySystem {
    public static final FighterComponent NULL_FIGHTER = new FighterComponent();
    public FighterComponent player;
    public FighterComponent enemy;

    public final int size;
    public Entity[][] runes;
    public Entity selected;
    float[] cooldown;
    public FighterComponent collector = NULL_FIGHTER;
    public FighterComponent turn;
    public boolean stable;
    public float stableTimer;

    // TODO make this class cleaner/smaller
    // too much hard coding of the 5 types
    public PuzzleSystem(int size, FighterComponent enemy) {
        super(5);
        this.size = size;
        runes = new Entity[size][size];
        cooldown = new float[size];
        this.enemy = enemy;
    }

    public void addedToEngine (Engine engine) {
        // Player Side
        Entity playerEntity = new Entity();
        player = new FighterComponent();
        playerEntity.add(this.player);
        playerEntity.add(new UIComponent());
        playerEntity.add(new PlayerControlledComponent());
        // TODO class selection system
        player.add(SpellComponent.getStrikeSpell());
        player.add(SpellComponent.getRefreshSpell());
        engine.addEntity(playerEntity);

        // Enemy Side
        Entity enemyEntity = new Entity();
        enemyEntity.add(enemy);
        enemyEntity.add(new UIComponent());
        engine.addEntity(enemyEntity);

        Entity message = new Entity();
        if (MathUtils.randomBoolean()) {
            turn = player;
            message.add(new MessageComponent("Player Goes First\nUse it Wisely"));
        } else {
            turn = enemy;
            message.add(new MessageComponent("Enemy Goes First\nGood Luck..."));
        }
        engine.addEntity(message);
    }

    public void update (float deltaTime) {
        for (int i = 0; i < size; i++) {
            cooldown[i] += deltaTime;
        }
        stable = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (runes[i][j] == null) {
                    stable = false;
                    if (j == 0) {
                        if (cooldown[i] > 2 * getRuneSize() / Constants.TILE_SPEED) {
                            createRune(i);
                            cooldown[i] = 0;
                        }
                    } else if (runes[i][j - 1] != null){
                        updateRune(runes[i][j - 1]);
                    }
                } else if (Mappers.ui.get(runes[i][j]).actor.hasActions()) {
                    stable = false;
                }
            }
        }
        if (stable) {
            stableTimer += deltaTime;
            if (stableTimer > Constants.STABLE_TIME) {
                if (checkForPossibleMoves()) {
                    //noinspection PointlessBooleanExpression
                    if (turn == enemy || Constants.PLAYERLESS) {
                        boolean cast = false;
                        Collections.reverse(enemy.spells);
                        for (Entity entity : enemy.spells) {
                            if (enemy.canCast(entity, this)) {
                                enemy.cast(entity, this);
                                cast = true;
                            }
                        }
                        Collections.reverse(enemy.spells);
                        if (!cast) makeRandomMove();
                        takeTurn(enemy);
                        return;
                    }
                } else {
                    Entity message = new Entity();
                    message.add(new MessageComponent("No More Moves\nBoard Reset"));
                    getEngine().addEntity(message);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (runes[i][j] != null) {
                                runes[i][j].add(new DestroyComponent(NULL_FIGHTER));
                            }
                        }
                    }
                }
            }
        } else stableTimer = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkForHorizontalTriples(i, j, true)) {
                    runes[i][j].add(new DestroyComponent(collector));
                    runes[i + 1][j].add(new DestroyComponent(collector));
                    runes[i + 2][j].add(new DestroyComponent(collector));
                    RuneComponent rc = Mappers.rune.get(runes[i][j]);
                    int matched = 3;
                    for (int k = i + 3; k < size; k++) {
                        if (runes[k][j] != null && rc.matches(Mappers.rune.get(runes[k][j])) && !Mappers.destroy.has(runes[k][j])) {
                            matched++;
                            runes[k][j].add(new DestroyComponent(collector));
                        } else break;
                    }
                    if (matched >= 4) {
                        /// destroy row
                        for (int k = 0; k < i; k++) {
                            if (runes[k][j] != null) runes[k][j].add(new DestroyComponent(NULL_FIGHTER));
                        }
                        for (int k = i + matched; k < size; k++) {
                            if (runes[k][j] != null) runes[k][j].add(new DestroyComponent(NULL_FIGHTER));
                        }
                        Entity message = new Entity();
                        if (matched == 4) {
                            MessageComponent mc = new MessageComponent("Matched 4\nRow Destroyed");
                            mc.x = Constants.UI_WIDTH + getRuneSize() * i;
                            mc.y = getRuneSize() * (j + matched) / 2;
                            message.add(mc);
                        }
                        message.add(new ScreenShakeComponent(Constants.MATCH_4_RUMBLE));
                        getEngine().addEntity(message);
                    }
                    if (matched >= 5) {
                        turn = collector;
                        Entity message = new Entity();
                        MessageComponent mc = new MessageComponent("Matched 5\nExtra Turn");
                        mc.x = Constants.UI_WIDTH + getRuneSize() * (i + matched) / 2;
                        mc.y = getRuneSize() * j;
                        message.add(mc);
                        message.add(new ScreenShakeComponent(Constants.MATCH_5_RUMBLE));
                        getEngine().addEntity(message);
                    }
                }
                if (checkForVerticalTriples(i, j, true)) {
                    runes[i][j].add(new DestroyComponent(collector));
                    runes[i][j + 1].add(new DestroyComponent(collector));
                    runes[i][j + 2].add(new DestroyComponent(collector));
                    RuneComponent rc = Mappers.rune.get(runes[i][j]);
                    int matched = 3;
                    for (int k = j + 3; k < size; k++) {
                        if (runes[i][k] != null && rc.matches(Mappers.rune.get(runes[i][k])) && !Mappers.destroy.has(runes[i][k])) {
                            matched++;
                            runes[i][k].add(new DestroyComponent(collector));

                        } else break;
                    }
                    if (matched >= 4) {
                        // destroy column
                        for (int k = 0; k < j; k++) {
                            if (runes[i][k] != null) runes[i][k].add(new DestroyComponent(NULL_FIGHTER));
                        }
                        for (int k = j + matched; k < size; k++) {
                            if (runes[i][k] != null) runes[i][k].add(new DestroyComponent(NULL_FIGHTER));
                        }
                        Entity message = new Entity();
                        if (matched == 4) {
                            MessageComponent mc = new MessageComponent("Matched 4\nColumn Destroyed");
                            mc.x = Constants.UI_WIDTH + getRuneSize() * (i + matched) / 2;
                            mc.y = getRuneSize() * j;
                            message.add(mc);
                        }
                        message.add(new ScreenShakeComponent(Constants.MATCH_4_RUMBLE));
                        getEngine().addEntity(message);
                    }
                    if (matched >= 5) {
                        turn = collector;
                        Entity message = new Entity();
                        MessageComponent mc = new MessageComponent("Matched 5\nExtra Turn");
                        mc.x = Constants.UI_WIDTH + getRuneSize() * (i + matched) / 2;
                        mc.y = getRuneSize() * j;
                        message.add(mc);
                        message.add(new ScreenShakeComponent(Constants.MATCH_5_RUMBLE));
                        getEngine().addEntity(message);
                    }
                }
            }
        }
    }

    public boolean isStable() {
        return stable && stableTimer > Constants.STABLE_TIME;
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
        rc.x = column;
        rc.y = 0;
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
        RuneComponent rc = Mappers.rune.get(rune);
        UIComponent uc = Mappers.ui.get(rune);

        int targetY = rc.y;
        for (int i = rc.y + 1; i < size; i++) {
            if (runes[rc.x][i] == null) {
                targetY = i;
            } else break;
        }
        runes[rc.x][rc.y] = runes[rc.x][targetY];
        runes[rc.x][targetY] = rune;
        rc.y = targetY;
        uc.actor.addAction(moveRuneAction(rune));
    }

    private MoveToAction moveRuneAction(Entity entity) {
        RuneComponent rc = Mappers.rune.get(entity);
        UIComponent uc = Mappers.ui.get(entity);

        Vector2 dest = new Vector2(Constants.UI_WIDTH + (rc.x + .375f) * getRuneSize(), Constants.WORLD_HEIGHT - (rc.y + .625f) * getRuneSize());
        float dist = dest.dst(uc.actor.getX(), uc.actor.getY());
        return Actions.moveTo(dest.x, dest.y, dist / Constants.TILE_SPEED, Interpolation.pow2In);
    }

    public void select(final Entity entity) {
        if (!isStable() || turn != player) return;
        if (selected == null) {
            selected = entity;
            entity.add(new SelectedComponent());
        } else if (selected == entity) {
            selected = null;
            entity.remove(SelectedComponent.class);
        } else {
            final RuneComponent rc1 = Mappers.rune.get(entity);
            final RuneComponent rc2 = Mappers.rune.get(selected);
            if (Math.abs(rc1.x - rc2.x) == 1 && Math.abs(rc1.y - rc2.y) == 0 || Math.abs(rc1.x - rc2.x) == 0 && Math.abs(rc1.y - rc2.y) == 1) {
                int tempX = rc1.x;
                int tempY = rc1.y;
                rc1.x = rc2.x;
                rc1.y = rc2.y;
                rc2.x = tempX;
                rc2.y = tempY;
                runes[rc1.x][rc1.y] = entity;
                runes[rc2.x][rc2.y] = selected;
                if (checkForTriples()) {
                    takeTurn(player);
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
                    tempX = rc1.x;
                    tempY = rc1.y;
                    rc1.x = rc2.x;
                    rc1.y = rc2.y;
                    rc2.x = tempX;
                    rc2.y = tempY;
                    runes[rc1.x][rc1.x] = entity;
                    runes[rc2.x][rc2.y] = selected;
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

    public boolean checkForTriples() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkForHorizontalTriples(i, j, false) || checkForVerticalTriples(i, j, false)) return true;
            }
        }
        return false;
    }

    private boolean checkForHorizontalTriples(int x, int y, boolean visual) {
        if (x >= size - 2) return false;
        if (runes[x][y] == null || runes[x + 1][y] == null || runes[x + 2][y] == null) return false;

        RuneComponent rc = Mappers.rune.get(runes[x][y]);
        RuneComponent rc1 = Mappers.rune.get(runes[x + 1][y]);
        RuneComponent rc2 = Mappers.rune.get(runes[x + 2][y]);
        UIComponent ui = Mappers.ui.get(runes[x][y]);
        UIComponent ui1 = Mappers.ui.get(runes[x + 1][y]);
        UIComponent ui2 = Mappers.ui.get(runes[x + 2][y]);

        if (Mappers.destroy.has(runes[x][y])) return false;
        if (Mappers.destroy.has(runes[x + 1][y])) return false;
        if (Mappers.destroy.has(runes[x + 2][y])) return false;

        return rc != null && rc1 != null && rc2 != null && !(visual && (ui.actor.hasActions() || ui1.actor.hasActions() || ui2.actor.hasActions())) && rc.matches(rc1) && rc.matches(rc2);

    }

    private boolean checkForVerticalTriples(int x, int y, boolean visual) {
        if (y >= size - 2) return false;
        if (runes[x][y] == null || runes[x][y + 1] == null || runes[x][y + 2] == null) return false;

        RuneComponent rc = Mappers.rune.get(runes[x][y]);
        RuneComponent rc1 = Mappers.rune.get(runes[x][y + 1]);
        RuneComponent rc2 = Mappers.rune.get(runes[x][y + 2]);
        UIComponent ui = Mappers.ui.get(runes[x][y]);
        UIComponent ui1 = Mappers.ui.get(runes[x][y + 1]);
        UIComponent ui2 = Mappers.ui.get(runes[x][y + 2]);

        if (Mappers.destroy.has(runes[x][y])) return false;
        if (Mappers.destroy.has(runes[x][y + 1])) return false;
        if (Mappers.destroy.has(runes[x][y + 2])) return false;

        return rc != null && rc1 != null && rc2 != null && !(visual && (ui.actor.hasActions() || ui1.actor.hasActions() || ui2.actor.hasActions())) && rc.matches(rc1) && rc.matches(rc2);

    }

    // TODO optimize the shit out of this
    private boolean checkForPossibleMoves() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != size - 1) {
                    if (checkSwitch(i, j, i + 1, j)) return true;
                }
                if (j != size - 1) {
                    if (checkSwitch(i, j, i, j + 1)) return true;
                }
            }
        }
        return false;
    }

    // check out this hard core AI. It's 100% guaranteed to be smarter than the player
    private void makeRandomMove() {
        int startX = MathUtils.random(size - 1);
        int startY = MathUtils.random(size - 1);
        for (int i = startX; i < size - 1; i++) {
            for (int j = startY; j < size - 1; j++) {
                if (checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
            for (int j = 0; j < startY; j++) {
                if (checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
        }
        for (int i = 0; i < startX; i++) {
            for (int j = startY; j < size - 1; j++) {
                if (checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
            for (int j = 0; j < startY; j++) {
                if (checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
        }
    }

    private void makeSwitch(int x1, int y1, int x2, int y2) {
        RuneComponent rc1 = Mappers.rune.get(runes[x1][y1]);
        RuneComponent rc2 = Mappers.rune.get(runes[x2][y2]);

        Entity temp = runes[x2][y2];
        runes[x2][y2] = runes[x1][y1];
        runes[x1][y1] = temp;
        rc1.x = x2;
        rc1.y = y2;
        rc2.x = x1;
        rc2.y = y1;
        updateRune(runes[x1][y1]);
        updateRune(runes[x2][y2]);
    }

    private boolean checkSwitch(int x1, int y1, int x2, int y2) {
        boolean check = false;
        Entity temp = runes[x2][y2];
        runes[x2][y2] = runes[x1][y1];
        runes[x1][y1] = temp;
        if (checkForTriples()) check = true;
        temp = runes[x2][y2];
        runes[x2][y2] = runes[x1][y1];
        runes[x1][y1] = temp;
        return check;
    }

    public Entity randomRune() {
        return runes[MathUtils.random(size - 1)][MathUtils.random(size - 1)];
    }

    public void takeTurn(FighterComponent fighterComponent) {
        collector = fighterComponent;
        stableTimer = 0;
        if (fighterComponent == NULL_FIGHTER || fighterComponent == player) {
            turn = enemy;
        } else turn = player;
    }
}
