package de.chrissx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;

import de.chrissx.alts.AltManager;
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
import net.minecraft.util.ChatComponentText;
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
	// TODO: put a lock on it and support running commands while in main menu that way
	public Consumer<String> sendMessage = (msg) -> {
		mc.thePlayer.addChatMessage(new ChatComponentText(Consts.prefix + msg));
	};

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
	 * This func is called whenever "GuiRenameWorld" aka the Alt-Manager wants to exec a command.
	 * @param input
	 * @param gui
	 */
	public void guiRenameWorld(String input, GuiRenameWorld gui) {
		sendMessage = (a) -> gui.setText(a);
		String[] split = input.split(" ");
		String[] cmd = new String[split.length + 1];
		cmd[0] = "alt";
		System.arraycopy(split, 0, cmd, 1, split.length);
		addonManager.execCmd(cmd);
		sendMessage = (msg) -> mc.thePlayer.addChatMessage(new ChatComponentText(Consts.prefix + msg));
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
