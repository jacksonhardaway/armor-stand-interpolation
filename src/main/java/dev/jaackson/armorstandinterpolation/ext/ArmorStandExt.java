package dev.jaackson.armorstandinterpolation.ext;

import com.mojang.math.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Rotations;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ArmorStandExt {
    /**
     * Interpolates a {@link ModelPart} between a start and end euler.
     * <p>This method works by converting the eulers into a {@link Quaterniond} then using a slerp function to
     * interpolate between the two eulers.
     * <p>
     * The rotation is then converted back to an euler and applied to the {@link ModelPart}.
     *
     * @param part  The part to interpolate.
     * @param start The starting rotation.
     * @param end   The ending rotation.
     */
    static void lerp(ModelPart part, Rotations start, Rotations end) {
        if (start.getX() == end.getX() && start.getY() == end.getY() && start.getZ() == end.getZ()) {
            part.setRotation(
                    Constants.DEG_TO_RAD * end.getX(),
                    Constants.DEG_TO_RAD * end.getY(),
                    Constants.DEG_TO_RAD * end.getZ()
            );
            return;
        }

        Quaternionf from = new Quaternionf().rotateXYZ(
                Constants.DEG_TO_RAD * start.getX(),
                Constants.DEG_TO_RAD * start.getY(),
                Constants.DEG_TO_RAD * start.getZ()
        );
        Quaternionf to = new Quaternionf().rotateXYZ(
                Constants.DEG_TO_RAD * end.getX(),
                Constants.DEG_TO_RAD * end.getY(),
                Constants.DEG_TO_RAD * end.getZ()
        );

        Vector3f result = from.slerp(to, Minecraft.getInstance().getFrameTime(), new Quaternionf())
                .getEulerAnglesXYZ(new Vector3f());

        part.setRotation(result.x(), result.y(), result.z());
    }

    Rotations getPrevHeadPose();

    Rotations getPrevBodyPose();

    Rotations getPrevLeftArmPose();

    Rotations getPrevRightArmPose();

    Rotations getPrevLeftLegPose();

    Rotations getPrevRightLegPose();

}
