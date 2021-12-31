package de.chrissx.mods.combat;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Aimbot extends Mod {

	AimbotMode mode = AimbotMode.GUN;
	File mf;
	
	public Aimbot() {
		super("AimBot", "aimbot", "Faces the next enemy for shooting with bows, guns, ...");
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if(mode == AimbotMode.GUN)
			for(Entity e : world().loadedEntityList)
				if(e instanceof EntityLivingBase && e != player() && !e.isInvisible() && !e.isDead && player().getDistanceToEntity(e) <= 30)
				{
					Util.faceBounds(e.boundingBox);
					return;
				}
		else Util.sendMessage("\u00a74Currently bow mode isn't supported."); // TODO: implement
	}

	@Override
	public void onStop() {}

	// TODO: replace
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
