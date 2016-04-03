package thepaperpilot.order.Util;

import com.badlogic.ashley.core.ComponentMapper;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Spells.DestroyColorComponent;
import thepaperpilot.order.Components.Spells.RefreshComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;

public class Mappers {
    public static final ComponentMapper<DestroyComponent> destroy = ComponentMapper.getFor(DestroyComponent.class);
    public static final ComponentMapper<ElectrifiedComponent> electrified = ComponentMapper.getFor(ElectrifiedComponent.class);
    public static final ComponentMapper<FighterComponent> fighter = ComponentMapper.getFor(FighterComponent.class);
    public static final ComponentMapper<IdleAnimationComponent> idleAnimation = ComponentMapper.getFor(IdleAnimationComponent.class);
    public static final ComponentMapper<RuneComponent> rune = ComponentMapper.getFor(RuneComponent.class);
    public static final ComponentMapper<PlayerControlledComponent> playerControlled = ComponentMapper.getFor(PlayerControlledComponent.class);
    public static final ComponentMapper<PuzzleComponent> puzzle = ComponentMapper.getFor(PuzzleComponent.class);
    public static final ComponentMapper<SpellComponent> spell = ComponentMapper.getFor(SpellComponent.class);
    public static final ComponentMapper<UIComponent> ui = ComponentMapper.getFor(UIComponent.class);

    /* Spells */
    public static final ComponentMapper<DestroyColorComponent> destroyColor = ComponentMapper.getFor(DestroyColorComponent.class);
    public static final ComponentMapper<RefreshComponent> refresh = ComponentMapper.getFor(RefreshComponent.class);
    public static final ComponentMapper<StrikeComponent> strike = ComponentMapper.getFor(StrikeComponent.class);
}
