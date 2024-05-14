package dev.angel.impl.module.combat.aimassist;

import dev.angel.api.module.Category;
import dev.angel.api.module.Module;
import dev.angel.api.value.EnumValue;
import dev.angel.api.value.NumberValue;
import dev.angel.api.value.Value;
import dev.angel.impl.module.combat.aimassist.mode.BodyPartMode;
import dev.angel.impl.module.combat.aimassist.mode.MouseButton;
import net.minecraft.entity.Entity;

// TODO: more smooth
public class AimAssist extends Module {

    protected final Value<Boolean> onlyWeapon = new Value<>(
            new String[]{"OnlyWeapon", "Weapon", "Sword"},
            "Only activates if we are holding a weapon",
            true
    );

    protected final Value<Boolean> crosshair = new Value<>(
            new String[]{"Crosshair", "Looking"},
            "Keeps modifying aim even if we are looking at the target",
            false
    );

    protected final EnumValue<MouseButton> button = new EnumValue<>(
            new String[]{"OnlyClick", "Click", "OnlyLeftClick", "OnlyRightClick", "Button"},
            "Only modifies aim if one of these buttons is pressed",
            MouseButton.LEFT
    );

    protected final NumberValue<Integer> targetRange = new NumberValue<>(
            new String[]{"TargetRange", "Range", "TargetDistance", "Distance", "Dist"},
            "How close an entity has to be to be targeted",
            5, 1, 6
    );

    protected final NumberValue<Integer> fov = new NumberValue<>(
            new String[]{"FOV", "Field"},
            "Only aims at entities within this fov",
            180, 0, 180
    );

    protected final EnumValue<BodyPartMode> bodyPart = new EnumValue<>(
            new String[]{"BodyPart", "Target", "Part"},
            "Which part of the body to target",
            BodyPartMode.HEAD
    );

    protected final NumberValue<Double> speed = new NumberValue<>(
            new String[]{"Speed", "sped"},
            "fast.",
            1D, 0.1D, 5D, 0.1D
    );

    protected final Value<Boolean> vertical = new Value<>(
            new String[]{"Vertical", "Vert", "LilUziVert"},
            "Aims vertically",
            false
    );

    protected final NumberValue<Float> verticalSpeed = new NumberValue<>(
            new String[]{"VerticalSpeed", "VerticalSped"},
            "How fast to aim vertically",
            0.5f, 0.1f, 10.0f, 0.1f
    );

    protected final Value<Boolean> horizontal = new Value<>(
            new String[]{"Horizontal", "Horiz"},
            "Aims horizontally",
            true
    );

    protected final NumberValue<Float> horizontalSpeed = new NumberValue<>(
            new String[]{"HorizontalSpeed", "Horizontal"},
            "How fast to aim horizontally",
            1.0f, 0.1f, 10.0f, 0.1f
    );

    protected Entity target;

    public AimAssist() {
        super("AimAssist", new String[]{"AimAssist", "AA", "Aimbot"}, "Automatically aims at targets", Category.COMBAT);
        this.offerValues(onlyWeapon, crosshair, button, targetRange, fov, bodyPart, speed, vertical, verticalSpeed, horizontal, horizontalSpeed);
        this.offerListeners(new ListenerMotion(this));
    }
}
