package de.chrissx.mods.movement;

import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Nofall extends Mod {

	public Nofall() {
		super("NoFall");
	}

	@Override
	public void onTick() {
		if(enabled) {
			if(mc.thePlayer.fallDistance > 2)
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		}
	}
}