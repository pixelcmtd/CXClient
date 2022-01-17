package de.chrissx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import de.chrissx.alts.Alt;
import de.chrissx.alts.AltManager;
import de.chrissx.alts.mcleaks.McLeaksApi;
import de.chrissx.alts.mcleaks.McLeaksSession;
import de.chrissx.hotkeys.Hotkey;
import de.chrissx.hotkeys.HotkeySaving;
import de.chrissx.iapi.Addon;
import de.chrissx.iapi.AddonManager;
import de.chrissx.mods.Bindable;
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
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

public class HackedClient {
	static HackedClient instance = null;
	List<Hotkey> hotkeys = new ArrayList<Hotkey>();
	final ModList mods;
	final AltManager altManager;
	List<Integer> lastPressed = new ArrayList<Integer>();
	final Minecraft mc = Minecraft.getMinecraft();
	McLeaksSession mcLeaksSession = null;
	boolean disableHotkeys = true;
	final AddonManager addonManager;

	public void onDraw(FontRenderer r) {
		if(mods.iaui.isEnabled()) {
			//can't use the paragraph char because git/github (don't know where the problem is coming from yet)
			int i = 0;
			for(RenderedObject o : mods.renderedObjects)
				if(o.isEnabled())
					o.onRender(r, 4, i++ * 8 + 4);
		}
	}

	public void onDisable() {
		for(StopListener l : mods.stopListeners)
			l.onStop();
		for(Mod m : mods)
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

		Util.checkIfExistsAndMake(Consts.configPath, "configPath");
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

	/**
	 * This func is called whenever "GuiRenameWorld" aka the Alt-Manager
	 * wants to exec a command.
	 * @param input
	 * @param gui
	 */
	public void guiRenameWorld(String input, IGuiRenameWorld gui) {
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

	// TODO: something like a #enabled command
	// TODO: commands to get `Consts.mcVersion`, `mc.getVersion()`, `Consts.version` and `Consts.APIVER`
	// TODO: also a way to get the values of the mods
	// TODO: make all (ALL) commands # agnostic
	public void onCommand(final String[] args) {
		final String cmd = args[0];

		if(cmd.equalsIgnoreCase("#cmdblock")) {
			if(args.length < 2) {
				Util.sendMessage("Please enter a command!");
				return;
			}
			String Cmd = args[1];
			for(int i = 2; i < args.length; i++)
				Cmd += " " + args[i];
			Util.cheatCmdBlock(Cmd);
		} else if(cmd.equalsIgnoreCase("#print-players")) {
			for(EntityPlayer p : mc.theWorld.playerEntities) {
				Util.sendMessage(p.getName() + " (X: " + (long)p.posX + ", Y: " + (long)p.posY + ", Z: " + (long)p.posZ + ")");
			}
		} else if(cmd.equalsIgnoreCase("#bind")) {
			if(args.length != 3) {
				Util.sendMessage("#bind <key> <mod-name>");
				return;
			}
			int keyId = Util.getKeyId(args[1]);
			Bindable bindable = mods.getBindable(args[2].toLowerCase());
			if(keyId == Keyboard.KEY_NONE)
				Util.sendError("LWJGL can't find that key.");
			else if(Hotkey.containsKey(hotkeys, keyId))
				Util.sendError("Key already registered.");
			else if(bindable == null)
				Util.sendError("That Bindable does not exist.");
			else
				hotkeys.add(new Hotkey(keyId, bindable));
		} else if(cmd.equalsIgnoreCase("#mods")) {
			Iterator<Entry<String, Bindable>> it = mods.getBindEntrys().iterator();
			String s = "Bindables: "+it.next().getKey();
			while(it.hasNext()) {
				s+=", "+it.next().getKey();
			}
			Util.sendMessage(s);
		} else if(cmd.equalsIgnoreCase("#unbind")) {
			if(args.length < 2)
				Util.sendMessage("#unbind <key>");
			else
				Util.removeHotkeyFromList(hotkeys, Util.getKeyId(args[1]));
		} else if(cmd.equalsIgnoreCase("#say")) {
			if(args.length == 1) {
				Util.sendError("Please enter a message.");
				return;
			}
			String msg = args[1];
			for(int i = 2; i < args.length; i++)
				msg += " " + args[i];
			Util.sendChat(msg);
		} else if(cmd.equalsIgnoreCase("#binds")) {
			StringBuilder sb = new StringBuilder();
			for(Hotkey hk : hotkeys)
				sb.append((sb.toString() == "" ? "" : ", ") + Keyboard.getKeyName(hk.key) + ":" + hk.handler.getName());
			Util.sendMessage("Hotkeys: "+sb.toString());
		} else if(cmd.equalsIgnoreCase("#give"))
			try {
				Util.cheatItem(new ItemStack(Item.getByNameOrId(args[1])), 0);
			} catch(Exception e) {
				Util.sendMessage(e.toString());
			} else if(cmd.equalsIgnoreCase("#givebypass"))
			try {
				ItemStack itm = new ItemStack(Item.getByNameOrId("furnace"), 1);
				NBTTagCompound itmtag = new NBTTagCompound();
				NBTTagCompound blockentitytag = new NBTTagCompound();
				NBTTagList items = new NBTTagList();
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Count", (byte) 1);
				item.setShort("Damage", (short) 0);
				item.setString("id", args[1]);
				item.setByte("Slot", (byte) 2);
				items.appendTag(item);
				blockentitytag.setTag("Items", items);
				itmtag.setTag("BlockEntityTag", blockentitytag);
				itm.setTagCompound(itmtag);
				Util.cheatItem(itm, 0);
			} catch(Exception e) {
				Util.sendMessage(e.toString());
			} else if(cmd.equalsIgnoreCase("#debug")) {
			mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
			                            "random.explode", 4F, mc.theWorld.rand.nextFloat() * 0.1F + 0.9F);
			for(Addon a : addonManager.addons) {
				Util.sendMessage(a.name + " " + a.author + " " + a.version + " " + a.description);
			}
			Util.sendMessage("Hotkeys are " + (disableHotkeys ? "disabled" : "enabled"));
			Util.sendMessage(Consts.dotMinecraftPath);
		} else addonManager.execCmd(args);
	}

	public McLeaksSession getMcLeaksSession() {
		return mcLeaksSession;
	}

	public void setMcLeaksSession(McLeaksSession mcLeaksSession) {
		this.mcLeaksSession = mcLeaksSession;
	}

	public boolean hotkeysDisabled() {
		return disableHotkeys;
	}

	public void setDisableHotkeys(boolean disableHotkeys) {
		this.disableHotkeys = disableHotkeys;
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
