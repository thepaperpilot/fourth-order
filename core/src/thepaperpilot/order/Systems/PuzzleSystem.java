package thepaperpilot.order.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import thepaperpilot.order.Class;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Effects.DamageOverTimeComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Player;
import thepaperpilot.order.Rune;
import thepaperpilot.order.Screens.Location;
import thepaperpilot.order.Util.Constants;
import thepaperpilot.order.Util.Mappers;

public class PuzzleSystem extends EntitySystem {
    public static final FighterComponent NULL_FIGHTER = FighterComponent.getEnemy(Class.PALADIN, 0);
    public FighterComponent player;
    public FighterComponent enemy;
    public String victoryDialogue;
    public Runnable victoryEvent;
    public String defeatDialogue;
    public Runnable defeatEvent;
    public Location returnScreen;

    public final int size;
    public Entity[][] runes;
    public Entity selected;
    float[] cooldown;
    public FighterComponent collector = NULL_FIGHTER;
    public FighterComponent turn;
    public boolean stable;
    public float stableTimer;
    public Entity playerEntity;
    public Entity enemyEntity;

    public PuzzleSystem(FighterComponent fighter, Location returnScreen) {
        this(9, fighter, returnScreen);
    }

    public PuzzleSystem(int size, FighterComponent enemy, Location returnScreen) {
        super(5);
        this.size = size;
        this.returnScreen = returnScreen;
        runes = new Entity[size][size];
        cooldown = new float[size];
        this.enemy = enemy;
    }

    public void addedToEngine (Engine engine) {
        // Player Side
        playerEntity = new Entity();
        player = Player.getPlayer();
        player.reset();
        playerEntity.add(this.player);
        playerEntity.add(new ActorComponent());
        playerEntity.add(new PlayerControlledComponent());
        engine.addEntity(playerEntity);

        // Enemy Side
        enemyEntity = new Entity();
        enemy.reset();
        enemyEntity.add(enemy);
        enemyEntity.add(new ActorComponent());
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
        for (int i = size - 1; i >= 0; i--) {
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
                } else if (Mappers.actor.get(runes[i][j]).actor.hasActions()) {
                    stable = false;
                }
            }
        }
        if (stable) {
            stableTimer += deltaTime;
            if (stableTimer > Constants.STABLE_TIME) {
                if (checkForPossibleMoves()) {
                    //noinspection PointlessBooleanExpression
                    if (turn == enemy) {
                        makeMove(enemy);
                        return;
                    } else if (turn == player && Constants.PLAYERLESS) {
                        makeMove(player);
                        return;
                    }
                } else {
                    resetBoard();
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
                        if (runes[k][j] != null && rc.rune == Mappers.rune.get(runes[k][j]).rune && !Mappers.destroy.has(runes[k][j])) {
                            matched++;
                            runes[k][j].add(new DestroyComponent(collector));
                        } else break;
                    }
                    if (matched >= 4 && collector != NULL_FIGHTER) {
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
                            Main.playSound("match4.wav");
                        }
                        message.add(new ScreenShakeComponent(Constants.MATCH_4_RUMBLE));
                        getEngine().addEntity(message);
                    }
                    if (matched >= 5 && collector != NULL_FIGHTER) {
                        turn = collector;
                        Entity message = new Entity();
                        MessageComponent mc = new MessageComponent("Matched 5\nExtra Turn");
                        mc.x = Constants.UI_WIDTH + getRuneSize() * (i + matched) / 2;
                        mc.y = getRuneSize() * j;
                        message.add(mc);
                        message.add(new ScreenShakeComponent(Constants.MATCH_5_RUMBLE));
                        getEngine().addEntity(message);
                        Main.playSound("match5.wav");
                    }
                }
                if (checkForVerticalTriples(i, j, true)) {
                    runes[i][j].add(new DestroyComponent(collector));
                    runes[i][j + 1].add(new DestroyComponent(collector));
                    runes[i][j + 2].add(new DestroyComponent(collector));
                    RuneComponent rc = Mappers.rune.get(runes[i][j]);
                    int matched = 3;
                    for (int k = j + 3; k < size; k++) {
                        if (runes[i][k] != null && rc.rune == Mappers.rune.get(runes[i][k]).rune && !Mappers.destroy.has(runes[i][k])) {
                            matched++;
                            runes[i][k].add(new DestroyComponent(collector));

                        } else break;
                    }
                    if (matched >= 4 && collector != NULL_FIGHTER) {
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
                            Main.playSound("match4.wav");
                        }
                        message.add(new ScreenShakeComponent(Constants.MATCH_4_RUMBLE));
                        getEngine().addEntity(message);
                    }
                    if (matched >= 5 && collector != NULL_FIGHTER) {
                        turn = collector;
                        Entity message = new Entity();
                        MessageComponent mc = new MessageComponent("Matched 5\nExtra Turn");
                        mc.x = Constants.UI_WIDTH + getRuneSize() * (i + matched) / 2;
                        mc.y = getRuneSize() * j;
                        message.add(mc);
                        message.add(new ScreenShakeComponent(Constants.MATCH_5_RUMBLE));
                        getEngine().addEntity(message);
                        Main.playSound("match5.wav");
                    }
                }
            }
        }
    }

    private void resetBoard() {
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

    public void transition(final Screen screen) {
        turn = NULL_FIGHTER;
        getEngine().getSystem(RenderStageSystem.class).stage.addAction(Actions.sequence(Actions.delay(2), Actions.fadeOut(2, Interpolation.pow2), Actions.run(new Runnable() {
            @Override
            public void run() {
                Main.changeScreen(screen);
            }
        })));
    }

    public boolean isStable() {
        return stable && stableTimer > Constants.STABLE_TIME;
    }

    public int getRuneSize() {
        return (Constants.WORLD_WIDTH - 2 * Constants.UI_WIDTH) / size;
    }

    private void createRune(int column) {
        Entity rune = getRune(true);
        RuneComponent rc = Mappers.rune.get(rune);
        rc.x = column;
        rc.y = 0;
        getEngine().addEntity(rune);
    }

    public Entity getRune(boolean inclusive) {
        Entity rune = new Entity();
        RuneComponent rc = new RuneComponent();
        IdleAnimationComponent ic = new IdleAnimationComponent();
        rc.rune = Rune.randomRune(inclusive);
        ic.file = rc.rune.colorName + "Gem";
        if (rc.rune == Rune.EXP || rc.rune == Rune.DAMAGE) ic.chance = 1;
        rune.add(rc);
        rune.add(ic);
        rune.add(new ActorComponent());
        rune.add(new PuzzleComponent(this));
        return rune;
    }

    public void updateRune(Entity rune) {
        RuneComponent rc = Mappers.rune.get(rune);
        ActorComponent ac = Mappers.actor.get(rune);

        int targetY = rc.y;
        for (int i = rc.y + 1; i < size; i++) {
            if (runes[rc.x][i] == null) {
                targetY = i;
            } else break;
        }
        runes[rc.x][rc.y] = runes[rc.x][targetY];
        runes[rc.x][targetY] = rune;
        rc.y = targetY;
        ac.actor.addAction(moveRuneAction(rune));
    }

    private Action moveRuneAction(Entity entity) {
        RuneComponent rc = Mappers.rune.get(entity);
        ActorComponent ac = Mappers.actor.get(entity);

        Vector2 dest = new Vector2(Constants.UI_WIDTH + (rc.x + .375f) * getRuneSize(), Constants.WORLD_HEIGHT - (rc.y + .625f) * getRuneSize());
        float dist = dest.dst(ac.actor.getX(), ac.actor.getY());
        return Actions.sequence(Actions.moveTo(dest.x, dest.y, dist / Constants.TILE_SPEED, Interpolation.pow2In), Actions.delay(Constants.RUNE_DELAY));
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
                    final ActorComponent ac1 = Mappers.actor.get(entity);
                    final ActorComponent ac2 = Mappers.actor.get(selected);
                    ac1.actor.addAction(Actions.sequence(moveRuneAction(entity), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            // play error sound
                            ac1.actor.addAction(moveRuneAction(entity));
                            ac2.actor.addAction(moveRuneAction(selected));
                        }
                    })));
                    ac2.actor.addAction(moveRuneAction(selected));
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
        ActorComponent ac = Mappers.actor.get(runes[x][y]);
        ActorComponent ac1 = Mappers.actor.get(runes[x + 1][y]);
        ActorComponent ac2 = Mappers.actor.get(runes[x + 2][y]);

        return rc != null && rc1 != null && rc2 != null && !(visual && (ac.actor.hasActions() || ac1.actor.hasActions() || ac2.actor.hasActions())) && rc.rune == rc1.rune && rc.rune == rc2.rune;

    }

    private boolean checkForVerticalTriples(int x, int y, boolean visual) {
        if (y >= size - 2) return false;
        if (runes[x][y] == null || runes[x][y + 1] == null || runes[x][y + 2] == null) return false;

        RuneComponent rc = Mappers.rune.get(runes[x][y]);
        RuneComponent rc1 = Mappers.rune.get(runes[x][y + 1]);
        RuneComponent rc2 = Mappers.rune.get(runes[x][y + 2]);
        ActorComponent ac = Mappers.actor.get(runes[x][y]);
        ActorComponent ac1 = Mappers.actor.get(runes[x][y + 1]);
        ActorComponent ac2 = Mappers.actor.get(runes[x][y + 2]);

        return rc != null && rc1 != null && rc2 != null && !(visual && (ac.actor.hasActions() || ac1.actor.hasActions() || ac2.actor.hasActions())) && rc.rune == rc1.rune && rc.rune == rc2.rune;

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

    private void makeMove(FighterComponent fc) {
        boolean cast = false;
        for (Entity entity : fc.spells) {
            if (fc.canCast(entity, this) && MathUtils.randomBoolean(.3f)) {
                fc.cast(entity, this);
                cast = true;
            }
        }
        if (!cast) makeRandomSwitch();
        takeTurn(fc);
    }

    private void makeRandomSwitch() {
        int startX = MathUtils.random(size);
        int startY = MathUtils.random(size);
        for (int i = startX; i < size; i++) {
            for (int j = startY; j < size; j++) {
                if (i < size - 1  && checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (j < size - 1 && checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
            for (int j = 0; j < startY; j++) {
                if (i < size - 1 && checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (j < size - 1 && checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
        }
        for (int i = 0; i < startX; i++) {
            for (int j = startY; j < size - 1; j++) {
                if (i < size - 1 && checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (j < size - 1 && checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
            for (int j = 0; j < startY; j++) {
                if (i < size - 1 && checkSwitch(i, j, i + 1, j)) makeSwitch(i, j, i + 1, j);
                else if (j < size - 1 && checkSwitch(i, j, i, j + 1)) makeSwitch(i, j, i, j + 1);
                else continue;
                return;
            }
        }
        resetBoard();
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

        for (Entity entity : getEngine().getEntitiesFor(Family.all(StatusEffectComponent.class, DamageOverTimeComponent.class).get())) {
            StatusEffectComponent sc = Mappers.statusEffect.get(entity);
            DamageOverTimeComponent hc = Mappers.damageOverTime.get(entity);

            if (sc.target == turn) {
                turn.hit(hc.amount, this);
            }
        }
    }

    public void end(FighterComponent fighter) {
        Entity message = new Entity();
        if (fighter == enemy) {
            Main.playSound("victory.wav");
            message.add(new MessageComponent("[GOLD]You Are Victorious"));
            if (victoryDialogue != null) {
                Entity entity = new Entity();
                DialogueComponent dc = DialogueComponent.read("dialogue/victory.json");
                dc.start = victoryDialogue;
                dc.events.put("end", victoryEvent);
                entity.add(dc);
                entity.add(new ActorComponent());
                returnScreen.engine.addEntity(entity);
            }
        } else if (fighter == player) {
            Main.playSound("lose.wav");
            message.add(new MessageComponent("[FIREBRICK]You Have Been Defeated"));
            if (defeatDialogue != null) {
                Entity entity = new Entity();
                DialogueComponent dc = DialogueComponent.read("dialogue/defeat.json");
                dc.start = defeatDialogue;
                dc.events.put("end", defeatEvent);
                entity.add(dc);
                entity.add(new ActorComponent());
                returnScreen.engine.addEntity(entity);
            }
        }
        getEngine().addEntity(message);
        transition(returnScreen);
        Player.save();
    }
}
