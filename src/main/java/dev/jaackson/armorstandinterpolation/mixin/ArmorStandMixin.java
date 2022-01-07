package dev.jaackson.armorstandinterpolation.mixin;

import dev.jaackson.armorstandinterpolation.ext.ArmorStandExt;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends LivingEntity implements ArmorStandExt {

    private Rotations prevHeadPose = DEFAULT_HEAD_POSE;
    private Rotations prevBodyPose = DEFAULT_BODY_POSE;
    private Rotations prevLeftArmPose = DEFAULT_LEFT_ARM_POSE;
    private Rotations prevRightArmPose  = DEFAULT_RIGHT_ARM_POSE;
    private Rotations prevLeftLegPose = DEFAULT_LEFT_LEG_POSE;
    private Rotations prevRightLegPose = DEFAULT_RIGHT_LEG_POSE;

    public ArmorStandMixin(EntityType<? extends ArmorStand> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract Rotations getHeadPose();

    @Shadow
    public abstract Rotations getBodyPose();

    @Shadow
    public abstract Rotations getLeftArmPose();

    @Shadow
    public abstract Rotations getRightArmPose();

    @Shadow
    public abstract Rotations getLeftLegPose();

    @Shadow
    public abstract Rotations getRightLegPose();

    @Shadow @Final private static Rotations DEFAULT_HEAD_POSE;

    @Shadow @Final private static Rotations DEFAULT_BODY_POSE;

    @Shadow @Final private static Rotations DEFAULT_LEFT_ARM_POSE;

    @Shadow @Final private static Rotations DEFAULT_RIGHT_ARM_POSE;

    @Shadow @Final private static Rotations DEFAULT_LEFT_LEG_POSE;

    @Shadow @Final private static Rotations DEFAULT_RIGHT_LEG_POSE;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!this.prevHeadPose.equals(this.getHeadPose())) {
            this.prevHeadPose = new Rotations(this.getHeadPose().getX(), this.getHeadPose().getY(), this.getHeadPose().getZ());
        }

        if (!this.prevBodyPose.equals(this.getBodyPose())) {
            this.prevBodyPose = new Rotations(this.getBodyPose().getX(), this.getBodyPose().getY(), this.getBodyPose().getZ());
        }

        if (!this.prevLeftArmPose.equals(this.getLeftArmPose())) {
            this.prevLeftArmPose = new Rotations(this.getLeftArmPose().getX(), this.getLeftArmPose().getY(), this.getLeftArmPose().getZ());
        }

        if (!this.prevRightArmPose.equals(this.getRightArmPose())) {
            this.prevRightArmPose = new Rotations(this.getRightArmPose().getX(), this.getRightArmPose().getY(), this.getRightArmPose().getZ());
        }

        if (!this.prevLeftLegPose.equals(this.getLeftLegPose())) {
            this.prevLeftLegPose = new Rotations(this.getLeftLegPose().getX(), this.getLeftLegPose().getY(), this.getLeftLegPose().getZ());
        }

        if (!this.prevRightLegPose.equals(this.getRightLegPose())) {
            this.prevRightLegPose = new Rotations(this.getRightLegPose().getX(), this.getRightLegPose().getY(), this.getRightLegPose().getZ());
        }
    }

    @Override
    public Rotations getPrevHeadPose() {
        return prevHeadPose;
    }

    @Override
    public Rotations getPrevBodyPose() {
        return prevBodyPose;
    }

    @Override
    public Rotations getPrevLeftArmPose() {
        return prevLeftArmPose;
    }

    @Override
    public Rotations getPrevRightArmPose() {
        return prevRightArmPose;
    }

    @Override
    public Rotations getPrevLeftLegPose() {
        return prevLeftLegPose;
    }

    @Override
    public Rotations getPrevRightLegPose() {
        return prevRightLegPose;
    }
}
