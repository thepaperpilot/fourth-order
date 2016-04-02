package thepaperpilot.order.Listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import thepaperpilot.order.Battle;
import thepaperpilot.order.Components.FighterComponent;
import thepaperpilot.order.Components.UIComponent;
import thepaperpilot.order.Main;
import thepaperpilot.order.Util.Mappers;
import thepaperpilot.order.Util.ProgressBar.ProgressBarStyle;
import thepaperpilot.order.Util.TextProgressBar;

public class FighterListener implements EntityListener {
    private Battle battle;

    public FighterListener(Battle battle) {
        this.battle = battle;
    }

    @Override
    public void entityAdded(Entity entity) {
        FighterComponent fc = Mappers.fighter.get(entity);
        UIComponent uc = Mappers.ui.get(entity);

        Table ui = new Table(Main.skin);
        ui.setFillParent(true);
        if (Mappers.playerControlled.has(entity)) ui.left().top();
        else ui.right().bottom();

        Table table = new Table(Main.skin);
        table.top().pad(20);
        Table left = new Table(Main.skin);
        Table portrait = new Table(Main.skin); // temp placeholder
        portrait.setBackground(Main.skin.getDrawable("default-round"));
        left.add(portrait).size(90, 120).padBottom(4).row();
        fc.experience = new TextProgressBar("Exp", 0, fc.maxMason, 1, false, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.experience.setColor(Color.GREEN);
        fc.experience.setAnimateDuration(.1f);
        left.add(fc.experience).minWidth(1).height(40).expandX().fill();
        Table right = new Table(Main.skin);
        fc.healthBar = new TextProgressBar("hp", 0, fc.maxHealth, 1, false, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.healthBar.setValue(fc.health);
        fc.healthBar.setAnimateDuration(.1f);
        fc.poisonBar = new TextProgressBar("poison", 0, fc.maxPoision, 1, true, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.poisonBar.setColor(Color.PURPLE);
        fc.poisonBar.setAnimateDuration(.1f);
        fc.surpriseBar = new TextProgressBar("surprise", 0, fc.maxSurprise, 1, true, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.surpriseBar.setColor(Color.YELLOW);
        fc.surpriseBar.setAnimateDuration(.1f);
        fc.mortalBar = new TextProgressBar("mortal", 0, fc.maxMortal, 1, true, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.mortalBar.setColor(Color.RED);
        fc.mortalBar.setAnimateDuration(.1f);
        fc.steamBar = new TextProgressBar("steam", 0, fc.maxSteam, 1, true, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.steamBar.setColor(Color.TEAL);
        fc.steamBar.setAnimateDuration(.1f);
        fc.masonBar = new TextProgressBar("mason", 0, fc.maxMason, 1, true, Main.skin.get(ProgressBarStyle.class), Main.skin.get(LabelStyle.class));
        fc.masonBar.setColor(Color.GREEN);
        fc.masonBar.setAnimateDuration(.1f);
        right.add(fc.healthBar).minWidth(1).height(40).colspan(5).row();
        right.add(fc.poisonBar).minSize(1).pad(2);
        right.add(fc.surpriseBar).minSize(1).pad(2);
        right.add(fc.mortalBar).minSize(1).pad(2);
        right.add(fc.steamBar).minSize(1).pad(2);
        right.add(fc.masonBar).minSize(1).pad(2);
        table.add(left).padRight(4);
        table.add(right).expandX().fill().padBottom(2).row();
        for (Entity entity1 : fc.spells) {
            Table button = new Table(Main.skin);
            button.setBackground(Main.skin.getDrawable("default-round"));
            table.add(button).expandX().fill().height(40).colspan(2).pad(2).row();
        }
        ui.add(table).expandY().fill().width(280);
        uc.actor = ui;
        battle.ui.addActor(ui);
    }

    @Override
    public void entityRemoved(Entity entity) {
        UIComponent uc = Mappers.ui.get(entity);

        uc.actor.remove();
    }
}
