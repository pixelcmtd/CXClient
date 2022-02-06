package de.chrissx.mods.combat;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.FloatOption;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;

public class AutoLeave extends Mod {

	FloatOption min = new FloatOption("min", "The number of HP at which to leave", 2);

	public AutoLeave() {
		super("AutoLeave", "Leaves when your health does below a certain threshold");
		addOption(min);
	}

	@Override
	public void onTick() {
		if (player().getHealth() < min.value) {
			hc.onDisconnectedOrLeft();
			world().sendQuittingDisconnectingPacket();
			mc.loadWorld((WorldClient) null);
			if (mc.isIntegratedServerRunning())
				mc.displayGuiScreen(new GuiMainMenu());
			else
				mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
		}
	}
}
