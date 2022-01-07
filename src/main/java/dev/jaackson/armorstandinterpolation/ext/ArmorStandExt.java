package dev.jaackson.armorstandinterpolation.ext;

import com.mojang.math.Constants;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Rotations;

public interface ArmorStandExt {
    Rotations getPrevHeadPose();

    Rotations getPrevBodyPose();

    Rotations getPrevLeftArmPose();

    Rotations getPrevRightArmPose();

    Rotations getPrevLeftLegPose();

    Rotations getPrevRightLegPose();

    /**
     * Interpolates a {@link ModelPart} between a start and end euler.
     * <p>This method works by converting the eulers into a {@link Quaternion} then using a slerp function to
     * interpolate between the two eulers.
     *
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
        Quaternion result = slerp(
                ArmorStandExt.fromEuler(start.getX(), start.getY(), start.getZ()),
                ArmorStandExt.fromEuler(end.getX(), end.getY(), end.getZ()),
                Minecraft.getInstance().getFrameTime()
        );

        Vector3d vec = ArmorStandExt.toEuler(result);
        part.setRotation(
                Constants.DEG_TO_RAD * (float) vec.x,
                Constants.DEG_TO_RAD * (float) vec.y,
                Constants.DEG_TO_RAD * (float) vec.z
        );
    }

    private static Quaternion slerp(Quaternion from, Quaternion to, double delta) {
        if (from.equals(to))
            return from;

        double dot = from.i() * to.i() + from.j() * to.j() + from.k() * to.k() + from.r() * to.r();
        double s1;
        if (dot < 0.0) {
            s1 = -1.0;
            dot = -dot;
        } else {
            s1 = 1.0;
        }
        double s0;
        if (dot > 1.0 - Constants.EPSILON) {
            s0 = 1.0 - delta;
            s1 = Math.copySign(delta, s1);
        } else {
            double theta = Math.acos(dot);
            double invSinTheta = Math.sin(theta);
            s0 = Math.sin((1.0 - delta) * theta) / invSinTheta;
            s1 = Math.copySign(Math.sin(delta * theta) / invSinTheta, s1);
        }
        return new Quaternion(
                (float) (s0 * from.i() + s1 * to.i()),
                (float) (s0 * from.j() + s1 * to.j()),
                (float) (s0 * from.k() + s1 * to.k()),
                (float) (s0 * from.r() + s1 * to.r())
        );
    }

    private static Quaternion fromEuler(double pitch, double yaw, double roll) {
        double x = (Constants.DEG_TO_RAD * pitch) / 2.0;
        double y = (Constants.DEG_TO_RAD * yaw) / 2.0;
        double z = (Constants.DEG_TO_RAD * roll) / 2.0;
        double c1 = Math.cos(x);
        double c2 = Math.cos(y);
        double c3 = Math.cos(z);
        double s1 = Math.sin(x);
        double s2 = Math.sin(y);
        double s3 = Math.sin(z);
        return new Quaternion(
                (float) fuzzyFix(s1 * c2 * c3 - c1 * s2 * s3),
                (float) fuzzyFix(c1 * s2 * c3 + s1 * c2 * s3),
                (float) fuzzyFix(c1 * c2 * s3 - s1 * s2 * c3),
                (float) fuzzyFix(c1 * c2 * c3 + s1 * s2 * s3));
    }

    private static double fuzzyFix(double value) {
        if (Math.abs(value) < Constants.EPSILON) return 0.0D;
        if (Math.abs(1.0D - value) < Constants.EPSILON) return 1.0D;
        if (Math.abs(-1.0D - value) < Constants.EPSILON) return -1.0D;
        return value;
    }

    private static Vector3d toEuler(Quaternion quat) {
        double x = quat.i();
        double y = quat.j();
        double z = quat.k();
        double w = quat.r();
        double x2 = x + x, y2 = y + y, z2 = z + z;
        double xx = x * x2, xy = x * y2, xz = x * z2;
        double yy = y * y2, yz = y * z2, zz = z * z2;
        double wx = w * x2, wy = w * y2, wz = w * z2;
        double m11 = fuzzyFix(1.0 - (yy + zz));
        double m21 = fuzzyFix(xy + wz);
        double m31 = fuzzyFix(xz - wy);
        double m12 = fuzzyFix(xy - wz);
        double m22 = fuzzyFix(1.0 - (xx + zz));
        double m32 = fuzzyFix(yz + wx);
        double m33 = fuzzyFix(1.0 - (xx + yy));
        double eulerX;
        double eulerY = Constants.RAD_TO_DEG * Math.asin(-Math.min(Math.max(m31, -1.0), 1.0));
        double eulerZ;
        if (Math.abs(m31) < 1.0 - Constants.EPSILON) {
            eulerX = Constants.RAD_TO_DEG * Math.atan2(m32, m33);
            eulerZ = Constants.RAD_TO_DEG * Math.atan2(m21, m11);
        } else {
            eulerX = 0.0;
            eulerZ = Constants.RAD_TO_DEG * Math.atan2(-m12, m22);
        }
        if (Math.abs(eulerX) > 90.0) {
            eulerX -= Math.copySign(180.0, eulerX);
            eulerZ -= Math.copySign(180.0, eulerZ);
            eulerY = -eulerY;
            eulerY += Math.copySign(180.0, eulerY);
        }
        if (Math.abs(eulerY) > 180.0) {
            eulerY -= Math.copySign(360.0, eulerY);
        }

        return new Vector3d(eulerX, eulerY, eulerZ);
    }

}
