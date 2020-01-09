package net.urod.util;

import net.minecraft.util.math.BlockPos;

public class BlockPosWithDistFromMiner {
    private static final int SIZE_BITS_X = 25;
    private static final int SIZE_BITS_D = 6;
    private static final int SIZE_BITS_Z;
    private static final int SIZE_BITS_Y;

    private static final long BITS_X;
    private static final long BITS_Y;
    private static final long BITS_Z;
    private static final long BITS_D;

    private static final int BIT_SHIFT_Z;
    private static final int BIT_SHIFT_X;
    private static final int BIT_SHIFT_Y;

    static {
        SIZE_BITS_Z = SIZE_BITS_X;
        SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z - SIZE_BITS_D;
        BITS_D = (1L << SIZE_BITS_D) - 1L;
        BITS_X = (1L << SIZE_BITS_X) - 1L;
        BITS_Y = (1L << SIZE_BITS_Y) - 1L;
        BITS_Z = (1L << SIZE_BITS_Z) - 1L;
        BIT_SHIFT_Y = SIZE_BITS_D;
        BIT_SHIFT_Z = SIZE_BITS_Y + SIZE_BITS_D;
        BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z + SIZE_BITS_D;
    }

    int distance;
    int x;
    int y;
    int z;


    public BlockPosWithDistFromMiner(BlockPos blockPos, int distance) {
        x = blockPos.getX();
        y = blockPos.getY();
        z = blockPos.getZ();
        this.distance = distance;
    }

    BlockPosWithDistFromMiner(int x, int y, int z, int distance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.distance = distance;
    }

    public static BlockPosWithDistFromMiner fromLong(long value) {
        return new BlockPosWithDistFromMiner(unpackLongX(value), unpackLongY(value), unpackLongZ(value), unpackLongD(value));
    }

    public static int unpackLongX(long x) {
        return (int) (x << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
    }

    public static int unpackLongY(long y) {
        return (int) (y << 64 - BIT_SHIFT_Y - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
    }

    public static int unpackLongZ(long z) {
        return (int) (z << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
    }

    public static int unpackLongD(long d) {
        return (int) (d << 64 - SIZE_BITS_D >> 64 - SIZE_BITS_Z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }

    @Override
    public int hashCode() {
        return (int) asLong();
    }

    public long asLong() {
        return asLong(getX(), getY(), getZ(), getD());
    }

    public static long asLong(int x, int y, int z, int d) {
        long l = 0L;
        l |= ((long) x & BITS_X) << BIT_SHIFT_X;
        l |= ((long) y & BITS_Y) << BIT_SHIFT_Y;
        l |= ((long) z & BITS_Z) << BIT_SHIFT_Z;
        l |= ((long) d & BITS_D) << 0;
        return l;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getD() {
        return distance;
    }
}