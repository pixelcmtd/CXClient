package de.chrissx.mods.fun;

import de.chrissx.mods.Mod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class RollHead extends Mod {

	public RollHead() {
		super("RollHead");
	}

	@Override
	public void onTick()
	{
		if(enabled)
		{
			EntityPlayerSP p = mc.thePlayer;
			float f = (float) (p.ticksExisted % 20 / 10F * Math.PI);
		    p.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(p.rotationYaw, MathHelper.sin(f) * 90, p.onGround));
		}
	}
}
