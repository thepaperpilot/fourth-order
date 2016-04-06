package thepaperpilot.order.Util;

import com.badlogic.ashley.core.ComponentMapper;
import thepaperpilot.order.Components.*;
import thepaperpilot.order.Components.Effects.DamageMultiplierComponent;
import thepaperpilot.order.Components.Effects.DamageOverTimeComponent;
import thepaperpilot.order.Components.Spells.DamageComponent;
import thepaperpilot.order.Components.Spells.DestroyColorComponent;
import thepaperpilot.order.Components.Spells.RefreshComponent;
import thepaperpilot.order.Components.Spells.StrikeComponent;

public class Mappers {
    public static final ComponentMapper<DestroyComponent> destroy = ComponentMapper.getFor(DestroyComponent.class);
    public static final ComponentMapper<DialogueComponent> dialogue = ComponentMapper.getFor(DialogueComponent.class);
    public static final ComponentMapper<ElectrifiedComponent> electrified = ComponentMapper.getFor(ElectrifiedComponent.class);
    public static final ComponentMapper<FighterComponent> fighter = ComponentMapper.getFor(FighterComponent.class);
    public static final ComponentMapper<GlyphComponent> glyph = ComponentMapper.getFor(GlyphComponent.class);
    public static final ComponentMapper<IdleAnimationComponent> idleAnimation = ComponentMapper.getFor(IdleAnimationComponent.class);
    public static final ComponentMapper<MessageComponent> message = ComponentMapper.getFor(MessageComponent.class);
    public static final ComponentMapper<PlayerControlledComponent> playerControlled = ComponentMapper.getFor(PlayerControlledComponent.class);
    public static final ComponentMapper<PuzzleComponent> puzzle = ComponentMapper.getFor(PuzzleComponent.class);
    public static final ComponentMapper<RuneComponent> rune = ComponentMapper.getFor(RuneComponent.class);
    public static final ComponentMapper<ScreenShakeComponent> screenShake = ComponentMapper.getFor(ScreenShakeComponent.class);
    public static final ComponentMapper<SpellComponent> spell = ComponentMapper.getFor(SpellComponent.class);
    public static final ComponentMapper<StatusEffectComponent> statusEffect = ComponentMapper.getFor(StatusEffectComponent.class);
    public static ComponentMapper<TotemComponent> totem = ComponentMapper.getFor(TotemComponent.class);
    public static final ComponentMapper<ActorComponent> actor = ComponentMapper.getFor(ActorComponent.class);

    /* Spells + Effects */
    public static final ComponentMapper<DamageMultiplierComponent> damageMultiplier = ComponentMapper.getFor(DamageMultiplierComponent.class);
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<DamageOverTimeComponent> damageOverTime = ComponentMapper.getFor(DamageOverTimeComponent.class);
    public static final ComponentMapper<DestroyColorComponent> destroyColor = ComponentMapper.getFor(DestroyColorComponent.class);
    public static final ComponentMapper<RefreshComponent> refresh = ComponentMapper.getFor(RefreshComponent.class);
    public static final ComponentMapper<StrikeComponent> strike = ComponentMapper.getFor(StrikeComponent.class);
}
