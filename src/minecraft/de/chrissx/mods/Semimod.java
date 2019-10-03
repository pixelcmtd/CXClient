package de.chrissx.mods;

import de.chrissx.HackedClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.InventoryPlayer;

public abstract class Semimod implements Bindable, Commandable, Toggleable {

	protected final String name;
	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final HackedClient hc = HackedClient.getClient();

	protected Semimod(String name)
	{
		this.name = name;
	}

	protected EntityPlayerSP player()
	{
		return mc.thePlayer;
	}

	protected InventoryPlayer inventory() {
		return player().inventory;
	}
	
	protected WorldClient world() {
		return mc.theWorld;
	}

	@Override
	public void processCommand(String[] args) {
		toggle();
	}

	@Override
	public void onHotkey() {
		toggle();
	}
	
	@Override
	public String getName() {
		return name;
	}
}
