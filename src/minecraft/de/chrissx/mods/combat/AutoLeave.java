package de.chrissx.mods.combat;

import java.io.File;

import de.chrissx.HackedClient;
import de.chrissx.mods.Mod;
import de.chrissx.util.Util;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.realms.RealmsBridge;

public class AutoLeave extends Mod {

	float min = 2;
	File mf;

	public AutoLeave() {
		super("AutoLeave");
		mf = getApiFile("min");
	}

	@Override
	public void onTick()
	{
		if(mc.thePlayer.getHealth() < min)
		{
        	HackedClient.getClient().onDisconnectedOrLeft();
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld((WorldClient)null);
            if (mc.isIntegratedServerRunning())
                this.mc.displayGuiScreen(new GuiMainMenu());
            else if (mc.func_181540_al())
                new RealmsBridge().switchToRealms(new GuiMainMenu());
            else
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
		}
	}

	@Override
	public void processCommand(String[] args)
	{
		if(args.length == 1)
			toggle();
		else if(args[1].equalsIgnoreCase("min"))
			try
			{
				min = Float.parseFloat(args[2]);
			}
			catch(Exception e)
			{
				Util.sendMessage("\u00a74Error parsing float.");
			}
		else
			Util.sendMessage("#autoleave to toggle, #autoleave min <float> to set minimum health before leave.");
	}
	
	@Override
	public void apiUpdate()
	{
		write(mf, min);
	}
}
