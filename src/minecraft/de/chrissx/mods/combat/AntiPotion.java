package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class AntiPotion extends Mod {

	final Potion[] blockedEffects = {
			Potion.hunger, Potion.moveSlowdown, Potion.digSlowdown, Potion.harm,
			Potion.confusion, Potion.blindness, Potion.weakness, Potion.wither, Potion.poison
	};

	public AntiPotion() {
		super("AntiPotion", "antipotion");
	}

	@Override
	public void onTick()
	{
		if(enabled && !player().capabilities.isCreativeMode && player().onGround && hasBadEffect())
			for (int i = 0; i < 1000; i++)
				sendPacket(new C03PacketPlayer());
	}

	boolean hasBadEffect()
	{
		if (player().getActivePotionEffects().isEmpty())
		  return false;
		for(Potion e : blockedEffects)
			if (player().isPotionActive(e))
				return true;
		return false;
	}
}
