package de.chrissx.mods.building;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;

public class Kaboom extends Mod {

	public Kaboom() {
		super("Kaboom", "Makes you explode, once you are in creative mode");
	}

	@Override
	public void onTick() {
		EntityPlayerSP p = player();
		if (p.capabilities.isCreativeMode && p.onGround) {
			// does not create sound,
			// which seems to be related to thePlayer not being a tnt entity
			// TODO: somehow playing a sound would be nice
			new Explosion(world(), p, p.posX, p.posY, p.posZ, 6, false, true).doExplosionB(true);
			for (BlockPos bp : Util.getBlocksAround(p, 6, false))
				for (int i = 0; i < 32; i++)
					Util.breakBlock(bp);
			enabled = false;
		}
	}
}
