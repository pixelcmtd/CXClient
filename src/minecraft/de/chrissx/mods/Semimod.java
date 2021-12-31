package de.chrissx.mods;

import java.util.ArrayList;

import de.chrissx.HackedClient;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;

public abstract class Semimod extends EapiModule implements Bindable, CommandExecutor, Toggleable {

	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final HackedClient hc = HackedClient.getClient();
	protected final String argv0, description;
	protected final ArrayList<Option> options = new ArrayList<Option>();

	protected Semimod(String name, String argv0, String description) {
		super(name);
		this.argv0 = argv0;
		this.description = description;
	}

	protected void addOption(Option o) {
		options.add(o);
	}

	public String getArgv0() {
		return argv0;
	}

	/***
	 * mc.thePlayer
	 */
	protected EntityPlayerSP player() {
		return mc.thePlayer;
	}

	/***
	 * mc.playerController
	 */
	protected PlayerControllerMP playerController() {
		return mc.playerController;
	}

	/***
	 * mc.thePlayer.inventory
	 */
	protected InventoryPlayer inventory() {
		return player().inventory;
	}

	/***
	 * mc.theWorld
	 */
	protected WorldClient world() {
		return mc.theWorld;
	}

	/***
	 * mc.currentScreen
	 */
	protected GuiScreen currentScreen() {
		return mc.currentScreen;
	}

	/**
	 * mc.gameSettings
	 */
	protected GameSettings settings() {
		return mc.gameSettings;
	}

	/***
	 * Simulates a mouse click.
	 * 
	 * @param button true if you want a left click, false if you want a right click
	 */
	protected void click(boolean button) {
		if (button)
			mc.clickMouse();
		else
			mc.rightClickMouse();
	}

	@SuppressWarnings("rawtypes")
	protected void sendPacket(Packet p) {
		player().sendQueue.addToSendQueue(p);
	}

	// TODO: pretty colors
	@Override
	public void processCommand(String[] args) {
		if (args.length == 1) {
			toggle();
			return;
		}

		String value = args.length > 2 ? args[2] : "";
		for (int i = 1; i < args.length; i++)
			value += " " + args[i];

		for (Option o : options) {
			if (args[1].equalsIgnoreCase(o.name)) {
				try {
					o.set(value);
				} catch (Exception e) {
					Util.sendMessage("Cannot parse " + o.name + " value: " + e.getMessage());
				}
				return;
			}
		}

		Util.sendMessage(name + " (" + argv0 + "): " + description);
		if (options.size() > 0) {
			Util.sendMessage("");
			Util.sendMessage("Options:");
		}
		for (Option o : options)
			Util.sendMessage(o.name + ": " + o.description + "(default: " + o.defaultValue + ")");
	}

	@Override
	public void onHotkey() {
		toggle();
	}

	@Override
	public void apiUpdate() {
	}
}
