package net.minecraft.util;

import java.util.Random;
import java.util.UUID;

public class MathHelper
{
    public static final float SQRT_2 = sqrt(2.0F);

    /**
     * A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536.
     */
    private static final float[] SIN_TABLE = new float[65536];

    /**
     * Though it looks like an array, this is really more like a mapping.  Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.  Value
     * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
     * integer that can cause the upper 5 bits to get that value.  Used for highly optimized "find the log-base-2 of
     * this number" calculations.
     */
    private static final int[] multiplyDeBruijnBitPosition;
    private static final double field_181163_d;
    private static final double[] field_181164_e;
    private static final double[] field_181165_f;

    /**
     * sin looked up in a table
     */
    public static float sin(float f)
    {
        return SIN_TABLE[(int)(f * 10430.378F) & 65535];
    }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static float cos(float value)
    {
        return SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }

    public static float sqrt(float value)
    {
        return (float)Math.sqrt((double)value);
    }

    public static float sqrt(double value)
    {
        return (float)Math.sqrt(value);
    }

    /**
     * Returns the greatest integer less than or equal to the float argument
     */
    public static int floor(float value)
    {
        int i = (int)value;
        return value < (float)i ? i - 1 : i;
    }

    /**
     * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    public static int truncateDoubleToInt(double value)
    {
        return (int)(value) - 1024;
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    public static int floor(double value)
    {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }

    /**
     * Long version of floor_double
     */
    public static long floor_long(double value)
    {
        long i = (long)value;
        return value < (double)i ? i - 1L : i;
    }

    public static int func_154353_e(double value)
    {
        return (int)(value >= 0 ? value : -value + 1);
    }

    public static float abs(float value)
    {
        return value >= 0.0F ? value : -value;
    }

    /**
     * Returns the unsigned value of an int.
     */
    public static int abs_int(int value)
    {
        return value >= 0 ? value : -value;
    }

    public static int ceiling_float_int(float value)
    {
        int i = (int)value;
        return value > (float)i ? i + 1 : i;
    }

    public static int ceiling_double_int(double d)
    {
        int i = (int)d;
        return d > (double)i ? i + 1 : i;
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters.
     */
    public static int clamp(int i, int min, int max)
    {
        return i < min ? min : (i > max ? max : i);
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters
     */
    public static float clamp(float f, float min, float max)
    {
        return f < min ? min : (f > max ? max : f);
    }

    public static double clamp(double d, double min, double max)
    {
        return d < min ? min : (d > max ? max : d);
    }

    public static double denormalizeClamp(double d, double e, double f)
    {
        return f < 0.0D ? d : (f > 1.0D ? e : d + (e - d) * f);
    }

    /**
     * Maximum of the absolute value of two numbers.
     */
    public static double abs_max(double d, double e)
    {
        d = d < 0 ? -d : d;
        
        e = e < 0 ? -e : e;

        return d > e ? d : e;
    }

    /**
     * Buckets an integer with specifed bucket sizes.
     */
    public static int bucketInt(int i, int j)
    {
        return i < 0 ? -((-i - 1) / j) - 1 : i / j;
    }

    public static int getRandomIntegerInRange(Random r, int i, int j)
    {
        return i >= j ? i : r.nextInt(j - i + 1) + i;
    }

    public static float randomFloatClamp(Random r, float f, float g)
    {
        return f >= g ? f : r.nextFloat() * (g - f) + f;
    }

    public static double getRandomDoubleInRange(Random r, double d, double e)
    {
        return d >= e ? d : r.nextDouble() * (e - d) + d;
    }

    public static double average(long[] values)
    {
        long i = 0;

        for (long j : values)
        {
            i += j;
        }

        return (double)i / (double)values.length;
    }

    public static boolean epsilonEquals(float first, float second)
    {
        return abs(second - first) < 1.0E-5F;
    }

    public static int normalizeAngle(int i, int j)
    {
        return (i % j + j) % j;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static float wrapAngleTo180_float(float angle)
    {
        angle = angle % 360;

        return angle >= 180 ? angle - 360 : angle < -180 ? angle + 360 : angle;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static double wrapAngleTo180_double(double angle)
    {
    	angle = angle % 360;
    	
        return angle >= 180 ? angle - 360 : angle < -180 ? angle + 360 : angle;
    }

    /**
     * parses the string as integer or returns the second parameter if it fails
     */
    public static int parseIntWithDefault(String stri, int def_val)
    {
        try
        {
            return Integer.parseInt(stri);
        }
        catch (Throwable t)
        {
            return def_val;
        }
    }

    /**
     * parses the string as integer or returns the second parameter if it fails. this value is capped to par2
     */
    public static int parseIntWithDefaultAndMax(String stringInt, int defaultValue, int maxComparer)
    {
        return Math.max(maxComparer, parseIntWithDefault(stringInt, defaultValue));
    }

    /**
     * parses the string as double or returns the second parameter if it fails.
     */
    public static double parseDoubleWithDefault(String stringInt, double defaultValue)
    {
        try
        {
            return Double.parseDouble(stringInt);
        }
        catch (Throwable var4)
        {
            return defaultValue;
        }
    }

    public static double parseDoubleWithDefaultAndMax(String stringInt, double defaultValue, double p_82713_3_)
    {
        return Math.max(p_82713_3_, parseDoubleWithDefault(stringInt, defaultValue));
    }

    /**
     * Returns the input value rounded up to the next highest power of two.
     */
    public static int roundUpToPowerOfTwo(int value)
    {
        int i = value - 1;
        i = i | i >> 1;
        i = i | i >> 2;
        i = i | i >> 4;
        i = i | i >> 8;
        i = i | i >> 16;
        return i + 1;
    }

    /**
     * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
     */
    private static boolean isPowerOfTwo(int value)
    {
        return value != 0 && (value & value - 1) == 0;
    }

    /**
     * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
     * value.  Optimized for cases where the input value is a power-of-two.  If the input value is not a power-of-two,
     * then subtract 1 from the return value.
     */
    private static int calculateLogBaseTwoDeBruijn(int value)
    {
        value = isPowerOfTwo(value) ? value : roundUpToPowerOfTwo(value);
        return multiplyDeBruijnBitPosition[(int)((long)value * 125613361 >> 27) & 31];
    }

    /**
     * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the
     * highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
     */
    public static int calculateLogBaseTwo(int value)
    {
        return calculateLogBaseTwoDeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static int func_154354_b(int i, int j)
    {
        if (j == 0)
            return 0;
        else if (i == 0)
            return j;
        else
        {
            if (i < 0)
                j *= -1;

            int k = i % j;
            return k == 0 ? i : i + j - k;
        }
    }

    public static int func_180183_b(float f, float g, float h)
    {
        return func_180181_b(floor(f * 255.0F), floor(g * 255.0F), floor(h * 255.0F));
    }

    public static int func_180181_b(int i, int j, int k)
    {
        return (((i << 8) + j) << 8) + k;
    }

    public static int some_bit_shifting_with_ints(int i, int j)
    {
        return (i) & -16777216 | ((int)((float)((i & 16711680) >> 16) * (float)((j & 16711680) >> 16) / 255.0F)) << 16 | ((int)((float)((i & 65280) >> 8) * (float)((j & 65280) >> 8) / 255.0F)) << 8 | ((int)((float)((i & 255) >> 0) * (float)((j & 255) >> 0) / 255.0F));
    }

    public static double remove_pre_dot(double d)
    {
        return d - Math.floor(d);
    }

    public static long getPositionRandom(Vec3i pos)
    {
        return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
    }

    public static long getCoordinateRandom(int x, int y, int z)
    {
        long i = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
        return i * i * 42317861L + i * 11L;
    }

    public static UUID getRandomUuid(Random rand)
    {
        long i = rand.nextLong() & -61441L | 16384L;
        long j = rand.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
        return new UUID(i, j);
    }

    public static double func_181160_c(double d, double e, double f)
    {
        return (d - e) / (f - e);
    }

    public static double func_181159_b(double d, double e)
    {
        double f = e * e + d * d;

        if (Double.isNaN(f))
        {
            return Double.NaN;
        }
        else
        {

        	boolean f1 = d < 0, f2 = e < 0;
        	
            if (f1)
            {
                d = -d;
            }

            if (f2)
            {
                e = -e;
            }

            boolean f3 = d > e;

            if (f3)
            {
                double d1 = e;
                e = d;
                d = d1;
            }

            double g = func_181161_i(f);
            e = e * g;
            d = d * g;
            double h = field_181163_d + d;
            int i = (int)Double.doubleToRawLongBits(h);
            double j = field_181164_e[i];
            double k = field_181165_f[i];
            double l = h - field_181163_d;
            double m = d * k - e * l;
            double n = (6.0D + m * m) * m * 0.16666666666666666D;
            double o = j + n;

            if (f1)
            {
                return -o;
            }
            else if (f2)
            {
                return Math.PI - o;
            }
            else if (f3)
            {
                return (Math.PI / 2) - o;
            }
            else
            {
            	return o;
            }
        }
    }

    public static double func_181161_i(double d)
    {
        double e = 0.5D * d;
        long i = Double.doubleToRawLongBits(d);
        i = 6910469410427058090L - (i >> 1);
        d = Double.longBitsToDouble(i);
        d = d * (1.5D - e * d * d);
        return d;
    }

    public static int hsv_to_rgb(float h, float s, float v)
    {
        int i = (int)(h * 6.0F) % 6;
        float f = h * 6.0F - (float)i;
        float f1 = v * (1.0F - s);
        float f2 = v * (1.0F - f * s);
        float f3 = v * (1.0F - (1.0F - f) * s);
        float f4;
        float f5;
        float f6;

        switch (i)
        {
            case 0:
                f4 = v;
                f5 = f3;
                f6 = f1;
                break;

            case 1:
                f4 = f2;
                f5 = v;
                f6 = f1;
                break;

            case 2:
                f4 = f1;
                f5 = v;
                f6 = f3;
                break;

            case 3:
                f4 = f1;
                f5 = f2;
                f6 = v;
                break;

            case 4:
                f4 = f3;
                f5 = f1;
                f6 = v;
                break;

            case 5:
                f4 = v;
                f5 = f1;
                f6 = f2;
                break;

            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + h + ", " + s + ", " + v);
        }

        return clamp((int)(f4 * 255.0F), 0, 255) << 16 | clamp((int)(f5 * 255.0F), 0, 255) << 8 | clamp((int)(f6 * 255.0F), 0, 255);
    }

    static
    {
    	double pi_multiply_divide = Math.PI * 2 / 65536;
    	
        for (int i = 0; i < 65536; ++i)
        {
            SIN_TABLE[i] = (float)Math.sin((double)i * pi_multiply_divide);
        }

        multiplyDeBruijnBitPosition = new int[] {0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
        field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        field_181164_e = new double[257];
        field_181165_f = new double[257];

        for (int j = 0; j < 257; ++j)
        {
            double d0 = (double)j / 256.0D;
            double d1 = Math.asin(d0);
            field_181165_f[j] = Math.cos(d1);
            field_181164_e[j] = d1;
        }
    }
}
