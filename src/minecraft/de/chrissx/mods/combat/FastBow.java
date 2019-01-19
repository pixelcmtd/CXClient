package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastBow extends Mod {

	public FastBow() {
		super("FastBow");
	}

	@Override
	public void onTick()
	{
		EntityPlayerSP p = mc.thePlayer;
		ItemStack is;
		if(enabled && mc.gameSettings.keyBindUseItem.isKeyDown() && p.onGround && p.getHealth() > 0 &&
				(is = p.inventory.getCurrentItem()) != null && is.stackSize > 0 && is.getItem() instanceof ItemBow)
		{
			mc.rightClickMouse();
			for (int i = 0; i < 20; i++)
			  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			mc.playerController.onStoppedUsingItem(p);
		}
	}
}
