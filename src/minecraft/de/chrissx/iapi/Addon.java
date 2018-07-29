package de.chrissx.iapi;

import de.chrissx.HackedClient;
import de.chrissx.mods.StopListener;
import net.minecraft.client.Minecraft;

public abstract class Addon implements StopListener {

	/**
	 * Gets the AddonManager
	 * @return The AddonManager
	 */
	public AddonManager getManager() {
		return getClient().getAddonManager();
	}
	
	/**
	 * Gets the HackedClient-instance
	 * @return The HackedClient's instance
	 */
	public HackedClient getClient() {
		return HackedClient.getClient();
	}
	
	final String name;
	
	/**
	 * Gets the Minecraft-class's instance
	 * @return The Minecraft-instance
	 */
	public Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}
	
	/**
	 * 
	 * @param name The name of the addon
	 */
	public Addon(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void onStop() {}
}
