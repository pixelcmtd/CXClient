package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import de.chrissx.util.Random;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Derp extends Mod {

	public Derp() {
		super("Derp");
	}

	@Override
	public void onTick()
	{
		if(enabled)
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw
					+ Random.randFloat() * 360 - 180, Random.randFloat() * 180 - 90, mc.thePlayer.onGround));
	}
}
