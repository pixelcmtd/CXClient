package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Tired extends Mod {

	public Tired() {
		super("Tired");
	}

	@Override
	public void onTick()
	{
		if(enabled)
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.ticksExisted % 100, mc.thePlayer.onGround));
	}
}
