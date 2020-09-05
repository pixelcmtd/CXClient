package de.chrissx.mods;

import java.io.File;

import de.chrissx.util.Random;
import de.chrissx.util.Util;
import net.minecraft.client.gui.inventory.GuiChest;

public class Autosteal extends Mod {

	boolean bypass = false;
	byte timer = 0;
	File bpf, tf;

	public Autosteal() {
		super("AutoSteal", "autosteal");
		bpf = getApiFile("bypass");
		tf = getApiFile("timer");
	}

	@Override
	public void onTick() {
		if(enabled && currentScreen() instanceof GuiChest)
		{
			if(bypass)
			{
				if(timer < Random.rand(6))
					timer++;
				else {
					timer = 0;
					GuiChest gui = (GuiChest)currentScreen();
					for(int i = 0; i < gui.getUpper().getSizeInventory(); i++)
						if(gui.inventorySlots.getSlot(i).getHasStack()) {
							playerController().windowClick(gui.inventorySlots.windowId, i, 0, 1, player());
							return;
						}
				}
			}
			else
			{
				GuiChest gui = (GuiChest)currentScreen();
				for(int i = 0; i < gui.getUpper().getSizeInventory(); i++)
					if(gui.inventorySlots.getSlot(i).getHasStack())
						playerController().windowClick(gui.inventorySlots.windowId, i, 0, 1, player());
			}
		}
	}

	@Override
	public String getRenderstring() {
		return name + (bypass ? "(BYPASS)" : "");
	}

	@Override
	public void processCommand(String[] args) {
		if(args.length == 1)
			toggle();
		else if(args.length == 2 && args[1].equalsIgnoreCase("bypass"))
			bypass = !bypass;
		else
			Util.sendMessage("#autosteal to toggle, #autosteal bypass to toggle the bypass.");
	}

	@Override
	public void apiUpdate() {
		write(bpf, bypass);
		write(tf, timer);
	}
}