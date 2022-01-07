package dev.jaackson.armorstandinterpolation.mixin;

import dev.jaackson.armorstandinterpolation.ext.ArmorStandExt;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandModel.class)
public abstract class ArmorStandModelMixin extends ArmorStandArmorModel {

    @Shadow
    @Final
    private ModelPart shoulderStick;

    @Shadow
    @Final
    private ModelPart leftBodyStick;

    @Shadow
    @Final
    private ModelPart rightBodyStick;

    public ArmorStandModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/decoration/ArmorStand;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ArmorStand;isNoBasePlate()Z", shift = At.Shift.AFTER), cancellable = true)
    public void setupAnim(ArmorStand armorStand, float f, float g, float h, float i, float j, CallbackInfo ci) {
        ArmorStandExt extensions = (ArmorStandExt) armorStand;
        ArmorStandExt.lerp(this.rightBodyStick, extensions.getPrevBodyPose(), armorStand.getBodyPose());
        ArmorStandExt.lerp(this.leftBodyStick, extensions.getPrevBodyPose(), armorStand.getBodyPose());
        ArmorStandExt.lerp(this.shoulderStick, extensions.getPrevBodyPose(), armorStand.getBodyPose());
        ci.cancel();
    }
}
