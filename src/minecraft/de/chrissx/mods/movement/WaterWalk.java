package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;

public class WaterWalk extends Mod {

	int timer = 2;

	public WaterWalk() {
		super("WaterWalk");
	}

	@Override
	public void onTick()
	{
		if(enabled && !settings().keyBindSneak.pressed)
		{
			EntityPlayerSP p = player();
			if(p.isInWater() || Util.isWater(world().getBlock(new BlockPos(p.posX, p.posY, p.posZ))))
			{
				if(p.motionY < 0) p.motionY = 0;
				if(settings().keyBindJump.pressed) p.jump();
				else p.isAirBorne = false;
			}
		}
	}
}
