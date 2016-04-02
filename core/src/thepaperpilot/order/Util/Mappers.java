package thepaperpilot.order.Util;

import com.badlogic.ashley.core.ComponentMapper;
import thepaperpilot.order.Components.*;

public class Mappers {
    public static final ComponentMapper<FighterComponent> fighter = ComponentMapper.getFor(FighterComponent.class);
    public static final ComponentMapper<IdleAnimationComponent> idleAnimation = ComponentMapper.getFor(IdleAnimationComponent.class);
    public static final ComponentMapper<PlayerControlledComponent> playerControlled = ComponentMapper.getFor(PlayerControlledComponent.class);
    public static final ComponentMapper<PuzzleComponent> puzzle = ComponentMapper.getFor(PuzzleComponent.class);
    public static final ComponentMapper<UIComponent> ui = ComponentMapper.getFor(UIComponent.class);
}
