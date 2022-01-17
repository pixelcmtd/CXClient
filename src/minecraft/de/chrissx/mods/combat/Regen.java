package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.IntOption;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Mod {

	IntOption speed = new IntOption("speed", "Packets per tick", 250);

	public Regen() {
		super("Regen", "regen", "Makes you regenerate faster");
		addOption(speed);
	}

	@Override
	public void onTick() {
		EntityPlayerSP p = player();
		if (!playerController().isInCreativeMode() && p.getFoodStats().getFoodLevel() > 17 && p.getHealth() < 20
		        && p.getHealth() != 0 && p.onGround)
			for (short i = 0; i < speed.value; i++)
				sendPacket(new C03PacketPlayer());
	}
}
