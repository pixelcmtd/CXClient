package de.chrissx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import de.chrissx.alts.Alt;
import de.chrissx.alts.AltManager;
import de.chrissx.alts.mcleaks.McLeaksApi;
import de.chrissx.alts.mcleaks.McLeaksSession;
import de.chrissx.hotkeys.Hotkey;
import de.chrissx.hotkeys.HotkeySaving;
import de.chrissx.iapi.AddonManager;
import de.chrissx.mods.ChatBot;
import de.chrissx.mods.Mod;
import de.chrissx.mods.ModList;
import de.chrissx.mods.RenderedObject;
import de.chrissx.mods.StopListener;
import de.chrissx.mods.TickListener;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRenameWorld;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.util.IChatComponent;

public class HackedClient {
	static HackedClient instance = null;
	List<Hotkey> hotkeys = new ArrayList<Hotkey>();
	final ModList mods;
	final AltManager altManager;
	List<Integer> lastPressed = new ArrayList<Integer>();
	final Minecraft mc = Minecraft.getMinecraft();
	public McLeaksSession mcLeaksSession = null;
	public boolean disableHotkeys = true;
	final AddonManager addonManager;

	public void onDraw(FontRenderer r) {
		if(!mc.gameSettings.showDebugInfo && mods.iaui.isEnabled()) {
			int i = 0;
			for(RenderedObject o : mods.renderedObjects)
				if(o.isEnabled())
					o.onRender(r, 4, i++ * 8 + 4);
		}
	}

	public void onDisable() {
		for(StopListener l : mods.stopListeners)
			l.onStop();
		// TODO: don't do this
		for(Mod m : mods.mods)
			if(m.isEnabled())
				m.toggle();
	}

	public void onDisconnectedOrLeft() {
		disableHotkeys = true;
		onDisable();
	}

	public void onJoined() {
		disableHotkeys = false;
	}

	public void onShutdown() {
		onDisable();

		try {
			HotkeySaving.saveHotkeys(new File(Consts.hotkeyFile), hotkeys);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HackedClient() throws IOException {
		instance = this;

		Util.init();

		HotkeySaving.init(this);

		Util.checkIfExistsAndMake(Consts.cxclientPath, "cxclientPath");
		Util.checkIfExistsAndMake(Consts.addonPath, "addonPath");

		File f = new File(Consts.hotkeyFile);

		if(f.exists())
			try {
				hotkeys = HotkeySaving.loadHotkeys(f.toPath());
			} catch (Exception e) {
				e.printStackTrace();
			}

		altManager = new AltManager();
		mods = new ModList();
		addonManager = new AddonManager(Consts.addonPath);

		// TODO: RCON server (maybe first enabled by a command too)
	}

	// TODO: merge all this stuff into the normal command system and make the alt manager something like an exec `alt $cmd`
	/**
	 * This func is called whenever "GuiRenameWorld" aka the Alt-Manager
	 * wants to exec a command.
	 * @param input
	 * @param gui
	 */
	public void guiRenameWorld(String input, GuiRenameWorld gui) {
		try {
			String[] args = input.split(" ");
			String cmd = args[0];
			if(cmd.charAt(0) == '#')
				cmd = cmd.substring(1);
			if(cmd.equalsIgnoreCase("login")) {
				if(args.length == 2) {
					altManager.login(args[1], "");
					gui.setText("Logged into cracked account.");
				} else if(args.length == 1)
					gui.setText("login <email> [password] - don't use password if account is cracked.");
				else {
					String pass = args[2];
					for(int i = 3; i < args.length; i++)
						pass += " " + args[i];
					altManager.login(args[1], pass);
					gui.setText("Logged into premium account.");
				}
			} else if(cmd.equalsIgnoreCase("help"))
				gui.setText("Alt-commands: login, help, load, mcleaks, alts, cxcsv, vault");
			else if(cmd.equalsIgnoreCase("load")) {
				altManager.loadAlt(args[1]);
				gui.setText("Logged into " + (altManager.currentAlt.isCracked() ? "cracked" : "premium") + " account.");
			} else if(cmd.equalsIgnoreCase("mcleaks")) {
				if(args.length < 2)
					throw new Exception("mcleaks [token]");
				String token = args[1];
				for(int i = 2; i < args.length; i++)
					token += " " + args[i];
				mcLeaksSession = McLeaksApi.redeemMcleaksToken(token);
				altManager.login(mcLeaksSession.getMcname(), "");
				gui.setText("Success.");
			} else if(cmd.equalsIgnoreCase("alts"))
				for(Alt a : altManager.getAlts())
					gui.setText((gui.getText() == input ? "" : gui.getText() + ", ") + altManager.getName(a));
			else if(cmd.equalsIgnoreCase("cxcsv")) {
				String s = args[2];
				for(int i = 3; i < args.length; i++)
					s += " " + args[i];
				if(args[1].equalsIgnoreCase("load"))
					altManager.loadCxcsv(Paths.get(s));
				else if(args[1].equalsIgnoreCase("save"))
					altManager.saveCxcsv(s);
				else
					gui.setText("cxcsv load/save [file]");
			} else if(cmd.equalsIgnoreCase("clear"))
				altManager.clear();
			else if(cmd.equalsIgnoreCase("vault")) {
				if(args.length < 4)
					throw new Exception("Not enough arguments.");
				String s = args[3];
				for(int i = 4; i < args.length; i++)
					s += " " + args[i];
				if(args[1].charAt(0) == 'l') {
					altManager.loadVault(s, args[2]);
					gui.setText("Load successful.");
				} else if(args[1].charAt(0) == 's') {
					altManager.storeVault(s, args[2]);
					gui.setText("Store successful.");
				} else
					gui.setText("vault l/s [password (no spaces!)] [file]");
			} else
				guiRenameWorld("help", gui);
		} catch (Exception e) {
			e.printStackTrace();
			gui.setText(e.getMessage());
		}
	}

	public void onTick() {
		for(TickListener l : mods.tickListeners)
			try {
				if(l.isEnabled()) l.onTick();
			} catch (Exception e) {
				e.printStackTrace();
			}

		if(!disableHotkeys)
			updateKeyboard();
	}

	public void onChatMessage(IChatComponent component) {
		for(ChatBot b : mods.chatBots)
			try {
				if(b.isEnabled()) b.onChatMessage(component);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	void updateKeyboard() {
		for(Hotkey hk : hotkeys)
			if(Keyboard.isKeyDown(hk.key) && !lastPressed.contains(hk.key) &&
			        !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiRepair))
				hk.handler.onHotkey();

		lastPressed = new ArrayList<Integer>();
		for(Hotkey hk : hotkeys)
			if(Keyboard.isKeyDown(hk.key))
				lastPressed.add(hk.key);
	}

	public AddonManager getAddonManager() {
		return addonManager;
	}

	public void onCommand(final String[] args) {
		addonManager.execCmd(args);
	}

	public static HackedClient getClient() {
		return instance;
	}

	public List<Hotkey> getHotkeys() {
		return hotkeys;
	}

	public ModList getMods() {
		return mods;
	}

	public AltManager getAltManager() {
		return altManager;
	}
}
