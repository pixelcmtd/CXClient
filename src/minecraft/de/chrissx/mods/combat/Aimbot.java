package de.chrissx.mods.combat;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class Aimbot extends Mod {

	Option<AimbotMode> mode = new Option<AimbotMode>("mode", "Gun or bow mode, the style of aiming", AimbotMode.GUN) {
		@Override
		public void set(String value) {
			this.value = value == "" ? (this.value == AimbotMode.BOW ? AimbotMode.GUN : AimbotMode.BOW)
			             : AimbotMode.valueOf(value);
		}
	};
	File mf;

	public Aimbot() {
		super("AimBot", "aimbot", "Faces the next enemy for shooting with bows, guns, ...");
		addOption(mode);
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if (mode.value == AimbotMode.GUN)
			for (Entity e : world().loadedEntityList)
				if (e instanceof EntityLivingBase && e != player() && !e.isInvisible() && !e.isDead
				        && player().getDistanceToEntity(e) <= 30) {
					Util.faceBounds(e.boundingBox);
					return;
				} else
					Util.sendError("Currently bow mode isn't supported."); // TODO: implement
	}

	@Override
	public void apiUpdate() {
		write(mf, mode.value.b);
	}
}
