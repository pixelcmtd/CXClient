package de.chrissx.locations;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class LocFloat64 {
	
	public double x, y, z;
	
	public LocFloat64() {
		x = y = z = 0;
	}
	
	public LocFloat64(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public LocFloat64(LocFloat32 vector) {
        this(vector.x, vector.y, vector.z);
    }
	
	public LocFloat64(LocInt64 vector) {
        this(vector.x, vector.y, vector.z);
    }
	
	public LocFloat64(LocInt32 vector) {
        this(vector.x, vector.y, vector.z);
    }
	
	public LocFloat64(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }
	
	public LocFloat64(Vec3i vec)
	{
		this(vec.getX(), vec.getY(), vec.getZ());
	}

    public LocFloat64 scale(double factor) {
        return new LocFloat64(x * factor, y * factor, z * factor);
    }

    public LocFloat64 subtractReverse(LocFloat64 vec) {
        return new LocFloat64(vec.x - x, vec.y - y, vec.z - z);
    }

    public LocFloat64 normalize() {
        double d = MathHelper.sqrt(x * x + y * y + z * z);
        return d < 1.0E-4 ? new LocFloat64(0.0, 0.0, 0.0) : new LocFloat64(x / d, y / d, z / d);
    }

    public double dotProduct(LocFloat64 vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public LocFloat64 crossProduct(LocFloat64 vec) {
        return new LocFloat64(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    public LocFloat64 subtract(LocFloat64 vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public LocFloat64 subtract(double x, double y, double z) {
        return addVector(-x, -y, -z);
    }

    public LocFloat64 add(LocFloat64 p_178787_1_) {
        return addVector(p_178787_1_.x, p_178787_1_.y, p_178787_1_.z);
    }

    public LocFloat64 addVector(double x, double y, double z) {
        return new LocFloat64(x + x, y + y, z + z);
    }

    public double distanceTo(LocFloat64 vec) {
        double d = vec.x - x;
        double e = vec.y - y;
        double f = vec.z - z;
        return MathHelper.sqrt(d * d + e * e + f * f);
    }

    public double squareDistanceTo(LocFloat64 vec) {
        double var2 = vec.x - x;
        double var4 = vec.y - y;
        double var6 = vec.z - z;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public double lengthVector() {
        return MathHelper.sqrt(x * x + y * y + z * z);
    }

    public LocFloat64 getIntermediateWithXValue(LocFloat64 vec, double x) {
        double var4 = vec.x - x;
        double var6 = vec.y - y;
        double var8 = vec.z - z;
        if (var4 * var4 < 1.0000000116860974E-7) {
            return null;
        }
        double var10 = (x - x) / var4;
        return var10 >= 0.0 && var10 <= 1.0 ? new LocFloat64(x + var4 * var10, y + var6 * var10, z + var8 * var10) : null;
    }

    public LocFloat64 getIntermediateWithYValue(LocFloat64 vec, double y) {
        double var4 = vec.x - x;
        double var6 = vec.y - y;
        double var8 = vec.z - z;
        if (var6 * var6 < 1.0000000116860974E-7)
            return null;
        double var10 = (y - y) / var6;
        return var10 >= 0.0 && var10 <= 1.0 ? new LocFloat64(x + var4 * var10, y + var6 * var10, z + var8 * var10) : null;
    }

    public LocFloat64 getIntermediateWithZValue(LocFloat64 vec, double z) {
        double var4 = vec.x - x;
        double var6 = vec.y - y;
        double var8 = vec.z - z;
        if (var8 * var8 < 1.0000000116860974E-7)
            return null;
        double var10 = (z - z) / var8;
        return var10 >= 0.0 && var10 <= 1.0 ? new LocFloat64(x + var4 * var10, y + var6 * var10, z + var8 * var10) : null;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public LocFloat64 rotatePitch(float pitch) {
        float f = MathHelper.cos(pitch);
        float g = MathHelper.sin(pitch);
        double d = x;
        double e = y * (double)f + z * (double)g;
        double h = z * (double)f - y * (double)g;
        return new LocFloat64(d, e, h);
    }

    public LocFloat64 rotateYaw(float yaw) {
        float f = MathHelper.cos(yaw);
        float g = MathHelper.sin(yaw);
        double d = x * (double)f + z * (double)g;
        double e = y;
        double h = z * (double)f - x * (double)g;
        return new LocFloat64(d, e, h);
    }
    
    public Vec3 toVec3()
    {
    	return new Vec3(x, y, z);
    }
}
