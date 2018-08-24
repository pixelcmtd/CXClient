package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Explosion
{
    /** whether or not the explosion sets fire to blocks around it */
    final boolean isFlaming;

    /** whether or not this explosion spawns smoke particles */
    final boolean isSmoking;
    /** the RNG used to calculate what blocks to set on fire */
    final Random explosionRNG;
    final World worldObj;
    final double explosionX;
    final double explosionY;
    final double explosionZ;
    final Entity exploder;
    final float explosionSize;
    final List<BlockPos> affectedBlockPositions;
    final Map<EntityPlayer, Vec3> playerKnockbackMap;

    public Explosion(World world, Entity exploder, double x, double y, double z, float size, List<BlockPos> blocks)
    {
        this(world, exploder, x, y, z, size, false, true, blocks);
    }

    public Explosion(World world, Entity exploder, double x, double y, double z, float size, boolean flames, boolean smoke, List<BlockPos> affectedBlocks)
    {
        this(world, exploder, x, y, z, size, flames, smoke);
        this.affectedBlockPositions.addAll(affectedBlocks);
    }

    public Explosion(World world, Entity exploder, double x, double y, double z, float size, boolean flaming, boolean smoke)
    {
        this.explosionRNG = new Random();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayList();
        this.playerKnockbackMap = Maps.<EntityPlayer, Vec3>newHashMap();
        this.worldObj = world;
        this.exploder = exploder;
        this.explosionSize = size;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
        this.isFlaming = flaming;
        this.isSmoking = smoke;
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA()
    {
        Set<BlockPos> set = Sets.<BlockPos>newHashSet();
        //int i = 16;

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                for (int l = 0; l < 16; ++l)
                {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
                    {
                        double d0 = (double)((float)j / 15 * 2 - 1);
                        double d1 = (double)((float)k / 15 * 2 - 1);
                        double d2 = (double)((float)l / 15 * 2 - 1);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        double d4 = this.explosionX;
                        double d6 = this.explosionY;
                        double d8 = this.explosionZ;

                        for (float f1 = 0.3F; f > 0; f -= 0.22500001)
                        {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

                            if (iblockstate.getBlock().getMaterial() != Material.air)
                            {
                                float f2 = exploder != null ? exploder.getExplosionResistance(this, worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(null);
                                f -= (f2 + 0.3) * 0.3;
                            }

                            if (f > 0 && (exploder == null || exploder.verifyExplosion(this, worldObj, blockpos, iblockstate, f)))
                                set.add(blockpos);

                            d4 += d0 * 0.30000001192092896;
                            d6 += d1 * 0.30000001192092896;
                            d8 += d2 * 0.30000001192092896;
                        }
                    }
                }
            }
        }

        affectedBlockPositions.addAll(set);
        float f3 = this.explosionSize * 2.0F;
        int k1 = MathHelper.floor(this.explosionX - f3 - 1);
        int l1 = MathHelper.floor(this.explosionX + f3 + 1);
        int i2 = MathHelper.floor(this.explosionY - f3 - 1);
        int i1 = MathHelper.floor(this.explosionY + f3 + 1);
        int j2 = MathHelper.floor(this.explosionZ - f3 - 1);
        int j1 = MathHelper.floor(this.explosionZ + f3 + 1);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = (Entity)list.get(k2);

            if (!entity.isImmuneToExplosions())
            {
                double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)f3;

                if (d12 <= 1)
                {
                    double d5 = entity.posX - this.explosionX;
                    double d7 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
                    double d9 = entity.posZ - this.explosionZ;
                    double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0)
                    {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double)this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float)((int)((d10 * d10 + d10) / 2 * 8 * f3 + 1)));
                        double d11 = EnchantmentProtection.func_92092_a(entity, d10);
                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.disableDamage)
                            this.playerKnockbackMap.put((EntityPlayer)entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean spawnParticles)
    {
        worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4F, (1 + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (explosionSize >= 2 && isSmoking)
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, explosionX, explosionY, explosionZ, 1, 0, 0);
        else
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, explosionX, explosionY, explosionZ, 1, 0, 0);

        if (this.isSmoking)
        {
            for (BlockPos blockpos : affectedBlockPositions)
            {
                Block block = worldObj.getBlockState(blockpos).getBlock();

                if (spawnParticles)
                {
                    double d0 = (double)((float)blockpos.getX() + worldObj.rand.nextFloat());
                    double d1 = (double)((float)blockpos.getY() + worldObj.rand.nextFloat());
                    double d2 = (double)((float)blockpos.getZ() + worldObj.rand.nextFloat());
                    double d3 = d0 - explosionX;
                    double d4 = d1 - explosionY;
                    double d5 = d2 - explosionZ;
                    double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / (double)explosionSize + 0.1D);
                    d7 = d7 * (double)(worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + explosionX * 1.0D) / 2.0D, (d1 + explosionY * 1.0D) / 2.0D, (d2 + explosionZ * 1.0D) / 2.0D, d3, d4, d5);
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
                }

                if (block.getMaterial() != Material.air)
                {
                    if (block.canDropFromExplosion(this))
                    {
                        block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0F / this.explosionSize, 0);
                    }

                    this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState(), 3);
                    block.onBlockDestroyedByExplosion(this.worldObj, blockpos, this);
                }
            }
        }

        if (isFlaming)
        {
            for (BlockPos blockpos1 : affectedBlockPositions)
            {
                if (worldObj.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && worldObj.getBlockState(blockpos1.down()).getBlock().isFullBlock() && explosionRNG.nextInt(3) == 0)
                {
                    worldObj.setBlockState(blockpos1, Blocks.fire.getDefaultState());
                }
            }
        }
    }

    public Map<EntityPlayer, Vec3> getPlayerKnockbackMap()
    {
        return playerKnockbackMap;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    public EntityLivingBase getExplosivePlacedBy()
    {
        return exploder == null ? null : (exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)exploder).getTntPlacedBy() : (exploder instanceof EntityLivingBase ? (EntityLivingBase)exploder : null));
    }

    public void func_180342_d()
    {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions()
    {
        return this.affectedBlockPositions;
    }
}
