package net.minecraft.client.audio;

import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;

public class GuardianSound extends MovingSound
{
    final EntityGuardian guardian;

    public GuardianSound(EntityGuardian guardian)
    {
        super(new ResourceLocation("minecraft:mob.guardian.attack"));
        this.guardian = guardian;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (!guardian.isDead && this.guardian.hasTargetedEntity())
        {
            xPosF = (float)guardian.posX;
            yPosF = (float)guardian.posY;
            zPosF = (float)guardian.posZ;
            float f = this.guardian.func_175477_p(0);
            this.volume = 0.0F + 1.0F * f * f;
            this.pitch = 0.7F + 0.5F * f;
        }
        else
        {
            this.donePlaying = true;
        }
    }
}
