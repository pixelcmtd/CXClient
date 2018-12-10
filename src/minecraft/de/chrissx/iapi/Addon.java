package de.chrissx.iapi;

import de.chrissx.HackedClient;
import de.chrissx.mods.StopListener;
import net.minecraft.client.Minecraft;

public abstract class Addon {

	/**
	 * Gets the AddonManager
	 * @return The AddonManager
	 */
	public AddonManager getManager() {
		return HackedClient.getClient().getAddonManager();
	}

	/**
	 * Gets the HackedClient-instance
	 * @return The HackedClient's instance
	 */
	public HackedClient getClient() {
		return HackedClient.getClient();
	}

	/**
	 * Gets the Minecraft-class's instance
	 * @return The Minecraft-instance
	 */
	public Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	/**
	 * Gets the name of this Addon
	 * @return The Addon's name
	 */
	public String getName() {
		return HackedClient.getClient().getAddonManager().getName(this);
	}
}
