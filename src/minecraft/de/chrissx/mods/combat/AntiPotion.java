package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AntiPotion extends Mod {

	final Potion[] blockedEffects = {
			Potion.hunger, Potion.moveSlowdown, Potion.digSlowdown, Potion.harm,
			Potion.confusion, Potion.blindness, Potion.weakness, Potion.wither, Potion.poison
			};
	
	public AntiPotion() {
		super("AntiPotion");
	}

	@Override
	public void onTick()
	{
		if(enabled && !mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.onGround && hasBadEffect())
			for (int i = 0; i < 1000; i++)
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	}
	
	boolean hasBadEffect()
	{
		if (mc.thePlayer.getActivePotionEffects().isEmpty())
		  return false;
		for(Potion e : blockedEffects)
			if (mc.thePlayer.isPotionActive(e))
				return true;
		return false;
	}
}
