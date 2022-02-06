package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.EnumOption;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Aimbot extends Mod {

	Option<AimbotMode> mode = new EnumOption<AimbotMode>(AimbotMode.class, "mode", "Gun or bow mode, the style of aiming", new AimbotMode[] {AimbotMode.GUN, AimbotMode.BOW});

	public Aimbot() {
		super("AimBot", "Faces the next enemy for shooting with bows, guns, ...");
		addOption(mode);
	}

	@Override
	public void onTick() {
		if (mode.value == AimbotMode.GUN) {
			for (Entity e : world().loadedEntityList)
				if (e instanceof EntityLivingBase && e != player() && !e.isInvisible() && !e.isDead
				        && player().getDistanceToEntity(e) <= 30) {
					Util.faceBounds(e.boundingBox);
					return;
				}
		} else
			Util.sendError("Currently bow mode isn't supported."); // TODO: implement
	}
}
