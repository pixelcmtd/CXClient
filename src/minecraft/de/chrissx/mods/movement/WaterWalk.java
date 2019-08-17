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
		if(enabled && !mc.gameSettings.keyBindSneak.pressed)
		{
			EntityPlayerSP p = mc.thePlayer;
			if(p.isInWater() || Util.isWater(mc.theWorld.getBlock(new BlockPos(p.posX, p.posY, p.posZ))))
			{
				if(p.motionY < 0) p.motionY = 0;
				if(mc.gameSettings.keyBindJump.pressed) p.jump();
				else p.isAirBorne = false;
			}
		}
	}
}
