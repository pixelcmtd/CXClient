package de.chrissx.mods.combat;

import java.util.HashMap;
import java.util.Map;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.BooleanOption;
import de.chrissx.util.Random;
import net.minecraft.client.gui.inventory.GuiChest;

public class Autosteal extends Mod {

	BooleanOption bypass = new BooleanOption("bypass", "Slows down to prevent being detected", false);
	byte timer = 0;

	public Autosteal() {
		super("AutoSteal", "autosteal", "Automatically empties any chest you open into your inventory");
		addOption(bypass);
	}

	@Override
	public void onTick() {
		if (currentScreen() instanceof GuiChest) {
			if (bypass.value) {
				if (timer < Random.rand(6))
					timer++;
				else {
					timer = 0;
					GuiChest gui = (GuiChest) currentScreen();
					for (int i = 0; i < gui.getUpper().getSizeInventory(); i++)
						if (gui.inventorySlots.getSlot(i).getHasStack()) {
							playerController().windowClick(gui.inventorySlots.windowId, i, 0, 1, player());
							return;
						}
				}
			} else {
				GuiChest gui = (GuiChest) currentScreen();
				for (int i = 0; i < gui.getUpper().getSizeInventory(); i++)
					if (gui.inventorySlots.getSlot(i).getHasStack())
						playerController().windowClick(gui.inventorySlots.windowId, i, 0, 1, player());
			}
		}
	}

	@Override
	public String getRenderstring() {
		return name + (bypass.value ? "(BYPASS)" : "");
	}

	@Override
	public Map<String, Object> apiValues() {
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put(bypass.name, bypass.value);
		vals.put("timer", timer);
		return vals;
	}
}