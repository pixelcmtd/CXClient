package de.chrissx.mods.building;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;

public class Kaboom extends Mod {

	public Kaboom() {
		super("Kaboom");
	}

	@Override
	public void onTick()
	{
		EntityPlayerSP p = player();
		if (enabled && p.capabilities.isCreativeMode && p.onGround)
		{
			//does not create sound,
			//which seems to be related to mc.thePlayer not being a tnt entity
			new Explosion(mc.theWorld, p, p.posX, p.posY, p.posZ, 6, false, true).doExplosionB(true);
	        for (BlockPos bp : Util.getBlocksAround(p, 6, false))
	            for(int i = 0; i < 32; i++)
	            	Util.breakBlock(bp);
	        enabled = false;
		}
	}
}
