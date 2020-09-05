package de.chrissx.mods;

import de.chrissx.HackedClient;
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
	protected final String[] argv0;

	protected Semimod(String name, String... argv0) {
		super(name);
		this.argv0 = argv0;
	}

	public String[] getArgv0() {
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
	 * @param button true if you want a left click, false if you want a right click
	 */
	protected void click(boolean button) {
		if(button) mc.clickMouse();
		else mc.rightClickMouse();
	}

	@SuppressWarnings("rawtypes")
	protected void sendPacket(Packet p) {
		player().sendQueue.addToSendQueue(p);
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
	public void apiUpdate() {}
}
