package thepaperpilot.order.Util;

import com.badlogic.ashley.core.ComponentMapper;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Components.Spells.CommandComponent;
import thepaperpilot.order.Components.Spells.DestroyColorComponent;
import thepaperpilot.order.Components.Spells.RefreshComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;

public class Mappers {
    public static final ComponentMapper<DestroyComponent> destroy = ComponentMapper.getFor(DestroyComponent.class);
    public static final ComponentMapper<DialogueComponent> dialogue = ComponentMapper.getFor(DialogueComponent.class);
    public static final ComponentMapper<ElectrifiedComponent> electrified = ComponentMapper.getFor(ElectrifiedComponent.class);
    public static final ComponentMapper<FighterComponent> fighter = ComponentMapper.getFor(FighterComponent.class);
    public static final ComponentMapper<IdleAnimationComponent> idleAnimation = ComponentMapper.getFor(IdleAnimationComponent.class);
    public static final ComponentMapper<MessageComponent> message = ComponentMapper.getFor(MessageComponent.class);
    public static final ComponentMapper<PlayerControlledComponent> playerControlled = ComponentMapper.getFor(PlayerControlledComponent.class);
    public static final ComponentMapper<PuzzleComponent> puzzle = ComponentMapper.getFor(PuzzleComponent.class);
    public static final ComponentMapper<RuneComponent> rune = ComponentMapper.getFor(RuneComponent.class);
    public static final ComponentMapper<ScreenShakeComponent> screenShake = ComponentMapper.getFor(ScreenShakeComponent.class);
    public static final ComponentMapper<SpellComponent> spell = ComponentMapper.getFor(SpellComponent.class);
    public static final ComponentMapper<StatusEffectComponent> statusEffect = ComponentMapper.getFor(StatusEffectComponent.class);
    public static ComponentMapper<TotemComponent> totem = ComponentMapper.getFor(TotemComponent.class);
    public static final ComponentMapper<UIComponent> ui = ComponentMapper.getFor(UIComponent.class);

    /* Effects */
    public static final ComponentMapper<DamageMultiplierComponent> damageMultiplier = ComponentMapper.getFor(DamageMultiplierComponent.class);

    /* Spells */
    public static final ComponentMapper<CommandComponent> command = ComponentMapper.getFor(CommandComponent.class);
    public static final ComponentMapper<DestroyColorComponent> destroyColor = ComponentMapper.getFor(DestroyColorComponent.class);
    public static final ComponentMapper<RefreshComponent> refresh = ComponentMapper.getFor(RefreshComponent.class);
    public static final ComponentMapper<StrikeComponent> strike = ComponentMapper.getFor(StrikeComponent.class);
}
