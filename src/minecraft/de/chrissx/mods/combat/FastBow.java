package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastBow extends Mod {

	public FastBow() {
		super("FastBow", "fastbow");
	}

	@Override
	public void onTick()
	{
		EntityPlayerSP p = player();
		ItemStack is;
		if(enabled && settings().keyBindUseItem.isKeyDown() && p.onGround && p.getHealth() > 0 &&
				(is = inventory().getCurrentItem()) != null && is.stackSize > 0 && is.getItem() instanceof ItemBow)
		{
			click(false);
			for (int i = 0; i < 20; i++)
			  sendPacket(new C03PacketPlayer(false));
			playerController().onStoppedUsingItem(p);
		}
	}
}
