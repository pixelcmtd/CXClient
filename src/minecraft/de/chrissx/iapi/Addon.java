package de.chrissx.iapi;

import de.chrissx.HackedClient;
import net.minecraft.client.Minecraft;

public abstract class Addon {

	public final String name, author, version, description;

	protected Addon(String name, String author, String version, String description) {
		this.name = name;
		this.author = author;
		this.version = version;
		this.description = description;
	}

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

	/**
	 * Gets the Minecraft-class's instance
	 * @return The Minecraft-instance
	 */
	public Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}
}
