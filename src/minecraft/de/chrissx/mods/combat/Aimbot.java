package de.chrissx.mods.combat;

import java.io.File;

import de.chrissx.Util;
import de.chrissx.mods.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Aimbot extends Mod {

	AimbotMode mode = AimbotMode.GUN;
	File mf;
	
	public Aimbot() {
		super("AimBot");
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if(enabled)
			if(mode == AimbotMode.GUN)
				for(Entity e : mc.theWorld.loadedEntityList)
					if(!(e instanceof EntityLivingBase) || e == mc.thePlayer || e.isInvisible() || e.isDead || mc.thePlayer.getDistanceToEntity(e) > 30)
						continue;
					else {
						Util.faceEntity(e);
						return;
					}
			else
				Util.sendMessage("§4Currently bow mode isn't supported.");
	}

	@Override
	public void onStop() {}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 3 && args[1].equalsIgnoreCase("mode"))
			try {
				mode = AimbotMode.valueOf(args[2]);
			} catch (Exception e) {
				Util.sendMessage("Error valueOf-ing AimbotMode.");
			}
		else
			Util.sendMessage("#aimbot to toggle, #aimbot mode <AimbotMode> to set gun or bow mode.");
	}

	@Override
	public void apiUpdate() {
		write(mf, mode.b);
	}
}
