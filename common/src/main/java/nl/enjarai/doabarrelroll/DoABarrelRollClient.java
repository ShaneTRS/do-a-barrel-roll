package nl.enjarai.doabarrelroll;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SmoothUtil;
import nl.enjarai.doabarrelroll.api.event.ClientEvents;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.api.event.RollGroup;
import nl.enjarai.doabarrelroll.config.ActivationBehaviour;
import nl.enjarai.doabarrelroll.config.LimitedModConfigServer;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.flight.RotationModifiers;
import nl.enjarai.doabarrelroll.platform.Services;
import nl.enjarai.doabarrelroll.util.MixinHooks;
import nl.enjarai.doabarrelroll.util.StarFoxUtil;

public class DoABarrelRollClient {
    public static final SmoothUtil PITCH_SMOOTHER = new SmoothUtil();
    public static final SmoothUtil YAW_SMOOTHER = new SmoothUtil();
    public static final SmoothUtil ROLL_SMOOTHER = new SmoothUtil();
    public static final RollGroup FALL_FLYING_GROUP = RollGroup.of(DoABarrelRoll.id("fall_flying"));
    public static double throttle = 0;

    public static void init() {
        FALL_FLYING_GROUP.trueIf(DoABarrelRollClient::isFallFlying);

        // Keyboard modifiers
        RollEvents.EARLY_CAMERA_MODIFIERS.register(context -> context
                .useModifier(RotationModifiers::manageThrottle, ModConfig.INSTANCE::getEnableThrust)
                .useModifier(RotationModifiers.buttonControls(1800)),
                2000, FALL_FLYING_GROUP);

        // Mouse modifiers, including swapping axes
        RollEvents.EARLY_CAMERA_MODIFIERS.register(context -> context
                .useModifier(ModConfig.INSTANCE::configureRotation),
                1000, FALL_FLYING_GROUP);

        // Generic movement modifiers, banking and such
        RollEvents.LATE_CAMERA_MODIFIERS.register(context -> context
                .useModifier(RotationModifiers::applyControlSurfaceEfficacy, ModConfig.INSTANCE::getSimulateControlSurfaceEfficacy)
                .useModifier(RotationModifiers.smoothing(
                        PITCH_SMOOTHER, YAW_SMOOTHER, ROLL_SMOOTHER,
                        ModConfig.INSTANCE.getSmoothing()
                ))
                .useModifier(RotationModifiers::banking, ModConfig.INSTANCE::getEnableBanking),
                1000, FALL_FLYING_GROUP);

        ClientEvents.SERVER_CONFIG_UPDATE.register(ModConfig.INSTANCE::notifyPlayerOfServerConfig);

        ModConfig.touch();

        // Init barrel rollery.
        StarFoxUtil.register();
    }

    public static void clearValues() {
        PITCH_SMOOTHER.clear();
        YAW_SMOOTHER.clear();
        ROLL_SMOOTHER.clear();
        throttle = 0;
    }

    public static boolean isFallFlying() {
        var hybrid = ModConfig.INSTANCE.getActivationBehaviour() == ActivationBehaviour.HYBRID ||
                    ModConfig.INSTANCE.getActivationBehaviour() == ActivationBehaviour.HYBRID_TOGGLE;
        if (hybrid && !MixinHooks.thirdJump) {
            return false;
        }
        if (!ModConfig.INSTANCE.getModEnabled()) {
            return false;
        }

        var player = MinecraftClient.getInstance().player;
        if (player == null) {
            return false;
        }
        if (ModConfig.INSTANCE.getDisableWhenSubmerged() && player.isSubmergedInWater()) {
            return false;
        }
        return player.isFallFlying() && switch (ModConfig.INSTANCE.getActivationBehaviour()) {
            case VANILLA -> ModKeybindings.TOGGLE_ENABLED.isPressed();
            case INVERTED -> !ModKeybindings.TOGGLE_ENABLED.isPressed();
            default -> true;
        };
    }

    public static boolean isConnectedToRealms() {
        return false; // We are not connected to realms.
    }
}
