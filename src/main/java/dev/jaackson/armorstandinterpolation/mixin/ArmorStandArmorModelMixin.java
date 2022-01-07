package dev.jaackson.armorstandinterpolation.mixin;

import dev.jaackson.armorstandinterpolation.ext.ArmorStandExt;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandArmorModel.class)
public abstract class ArmorStandArmorModelMixin extends HumanoidModel<ArmorStand> {

    public ArmorStandArmorModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/decoration/ArmorStand;FFFFF)V", at = @At("HEAD"), cancellable = true)
    public void setupAnim(ArmorStand armorStand, float f, float g, float h, float i, float j, CallbackInfo ci) {
        ArmorStandExt extensions = (ArmorStandExt) armorStand;
        ArmorStandExt.lerp(this.head, extensions.getPrevHeadPose(), armorStand.getHeadPose());
        ArmorStandExt.lerp(this.body, extensions.getPrevBodyPose(), armorStand.getBodyPose());
        ArmorStandExt.lerp(this.leftArm, extensions.getPrevLeftArmPose(), armorStand.getLeftArmPose());
        ArmorStandExt.lerp(this.rightArm, extensions.getPrevRightArmPose(), armorStand.getRightArmPose());
        ArmorStandExt.lerp(this.leftLeg, extensions.getPrevLeftLegPose(), armorStand.getLeftLegPose());
        ArmorStandExt.lerp(this.rightLeg, extensions.getPrevRightLegPose(), armorStand.getRightLegPose());
        this.hat.copyFrom(this.head);
        ci.cancel();
    }
}
