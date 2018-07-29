package de.chrissx.mods.combat;

import java.util.HashMap;
import java.util.Map;

import de.chrissx.Util;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Mod {

	public AutoArmor() {
		super("AutoArmor");
	}

	@Override
	public void onTick() {
		if(enabled && mc.currentScreen instanceof GuiInventory) {
			int[] bestArmorSlots = new int[4];
			int[] bestArmorValues = new int[4];
				
			for(int armorType = 0; armorType < 4; armorType++)
			{
				ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(armorType);
				if(oldArmor != null && oldArmor.getItem() instanceof ItemArmor)
				bestArmorValues[armorType] = ((ItemArmor)oldArmor.getItem()).damageReduceAmount;
					
				bestArmorSlots[armorType] = -1;
			}
				
			for(int slot = 0; slot < 36; slot++)
			{
				ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
				if(stack == null || !(stack.getItem() instanceof ItemArmor))
					continue;
					
					ItemArmor armor = (ItemArmor)stack.getItem();
					int armorType = armor.armorType;
					int armorValue = armor.damageReduceAmount;
					
					if(armorValue > bestArmorValues[armorType])
					{
						bestArmorSlots[armorType] = slot;
						bestArmorValues[armorType] = armorValue;
					}
				}
				
				for(int armorType = 0; armorType < 4; armorType++)
				{
					int slot = bestArmorSlots[armorType];
					if(slot == -1)
						continue;
						
					ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(armorType);
					if(oldArmor == null || mc.thePlayer.inventory.getFirstEmptyStack() != -1)
					{
						if(slot < 9)
							slot += 36;
						
						Util.swapSlots(slot, 8 - armorType, ((GuiInventory)mc.currentScreen).inventorySlots.windowId);
						
						break;
					}
				}
		}
	}
}