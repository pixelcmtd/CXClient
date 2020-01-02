package de.chrissx;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.google.common.io.Files;

import de.chrissx.alts.Alt;
import de.chrissx.alts.AltManager;
import de.chrissx.alts.mcleaks.McLeaksApi;
import de.chrissx.alts.mcleaks.McLeaksSession;
import de.chrissx.hotkeys.Hotkey;
import de.chrissx.hotkeys.HotkeySaving;
import de.chrissx.iapi.Addon;
import de.chrissx.iapi.AddonManager;
import de.chrissx.iapi.AddonProperties;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.Mod;
import de.chrissx.mods.ModList;
import de.chrissx.mods.RenderedObject;
import de.chrissx.mods.StopListener;
import de.chrissx.mods.TickListener;
import de.chrissx.options.Options;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class HackedClient {
	static HackedClient instance = null;
	boolean invis = false;
	List<Hotkey> hotkeys = new ArrayList<Hotkey>();
	final ModList mods;
	final AltManager altManager;
	List<Integer> lastPressed = new ArrayList<Integer>();
	final Minecraft mc = Minecraft.getMinecraft();
	McLeaksSession mcLeaksSession = null;
	boolean disableHotkeys = true;
	final AddonManager addonManager;
	final Options options;

	public void onDraw(FontRenderer r)
	{
		if(!invis)
		{
			//can't use the paragraph char because git/github (don't know where the problem is coming from yet)
			r.drawString("\u00a7a\u00a7l[" + Consts.clientName + " " + Consts.version + "]", 4, 4, Color.WHITE.getRGB());
			int i = 1;
			for(RenderedObject o : mods.renderedObjects)
				if(o.onRender(r, 4, i * 8 + 4))
					i++;
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
		
		options.stop(new File(Consts.optionsFile));
		options.eapi.stop(new File(Consts.eapiOptionsFile));
	}

	public HackedClient() throws IOException
	{
		instance = this;
		
		Util.init();
		
		HotkeySaving.init(this);
		
		Util.checkIfExistsAndMake(Consts.configPath, "configPath");
		Util.checkIfExistsAndMake(Consts.addonPath, "addonPath");
		Util.checkIfExistsAndMake(Consts.eapiPath, "eapiPath");
		Util.checkIfExistsAndMake(Consts.togglePath, "enablePath");
		
		File f = new File(Consts.hotkeyFile);
		
		if(f.exists())
			try {
				hotkeys = HotkeySaving.loadHotkeys(f.toPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		altManager = new AltManager();
		mods = new ModList();
		addonManager = new AddonManager();
		addonManager.init(Consts.addonPath);
		
		options = new Options();
		options.init(new File(Consts.optionsFile));
		options.eapi.init(new File(Consts.optionsFile));
		
		f = new File(Consts.runningFile);
		
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		f.deleteOnExit();

		f = new File(Consts.eapiVersionFile);
		int i = Consts.APIVER;
		Files.write(new byte[] {(byte)(i >> 24), (byte)(i >> 16), (byte)(i >> 8), (byte)i}, f);

		Files.write(StandardCharsets.UTF_8.encode(Consts.mcVersion).array(), new File(Consts.mcVersionFile));
		Files.write(StandardCharsets.UTF_8.encode(mc.getVersion()).array(), new File(Consts.launchedVersionFile));
		Files.write(StandardCharsets.UTF_8.encode(Consts.version).array(), new File(Consts.cxclientVersionFile));

		for(Mod m : mods)
			Util.checkIfExistsAndMake(Paths.get(Consts.modsPath, m.getName()).toString(), m.getName() + "Path");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final File enabledFile = new File(Consts.enabledPath);
					final File disableIAUI = Paths.get(Consts.eapiPath, "disable_iaui").toFile();
					while(true)
					{
						try {
							for(Mod m : mods)
							{
								final File f = Paths.get(Consts.togglePath, m.getName()).toFile();
								if(f.exists())
								{
									m.toggle();
									f.delete();
								}
							}
							ByteBuffer b = ByteBuffer.allocate(mods.length);
							for(Mod m : mods)
							{
								try {
									b.put(m.getName().getBytes(StandardCharsets.US_ASCII));
									b.put((byte) (m.isEnabled() ? 1 : 0));
									b.put((byte) 11); //[VT] (Vertical Tab)
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							//does this do anything but break some libraries?
							//b.put((byte) 4);
							enabledFile.createNewFile();
							Files.write(b.array(), enabledFile);
							for(Mod m : mods)
								m.apiUpdate();
							invis = disableIAUI.exists();
						} catch (Exception e) {
							Minecraft.logger.warn(e);
						}
						Thread.sleep(options.eapi.sleep);
					}
				} catch (Exception e) {
					Minecraft.logger.fatal(e);
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

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
				}else if(args.length == 1)
					gui.setText("login <email> [password] - don't use password if account is cracked.");
				else {
					String pass = args[2];
					for(int i = 3; i < args.length; i++)
						pass += " " + args[i];
					altManager.login(args[1], pass);
					gui.setText("Logged into premium account.");
				}
			}else if(cmd.equalsIgnoreCase("help"))
				gui.setText("Alt-commands: login, help, load, mcleaks, alts, cxcsv, vault");
			else if(cmd.equalsIgnoreCase("load")) {
				altManager.loadAlt(args[1]);
				gui.setText("Logged into " + (altManager.currentAlt.isCracked() ? "cracked" : "premium") + " account.");
			}else if(cmd.equalsIgnoreCase("mcleaks")) {
				if(args.length < 2)
					throw new Exception("mcleaks [token]");
				String token = args[1];
				for(int i = 2; i < args.length; i++)
					token += " " + args[i];
				mcLeaksSession = McLeaksApi.redeemMcleaksToken(token);
				altManager.login(mcLeaksSession.getMcname(), "");
			}else if(cmd.equalsIgnoreCase("alts"))
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
			}
			else if(cmd.equalsIgnoreCase("clear"))
				altManager.clear();
			else if(cmd.equalsIgnoreCase("vault"))
			{
				if(args.length < 4)
					throw new Exception("Not enough arguments.");
				String s = args[3];
				for(int i = 4; i < args.length; i++)
					s += " " + args[i];
				if(args[1].charAt(0) == 'l')
				{
					altManager.loadVault(s, args[2]);
					gui.setText("Load successful.");
				}
				else if(args[1].charAt(0) == 's')
				{
					altManager.storeVault(s, args[2]);
					gui.setText("Store successful.");
				}
				else
					gui.setText("vault l/s [password (no spaces!)] [file]");
			}
			else
				guiRenameWorld("help", gui);
		} catch (Exception e) {
			e.printStackTrace();
			gui.setText(e.getMessage());
		}
	}

	public void onTick() {
		for(TickListener l : mods.tickListeners)
			l.onTick();
		
		if(!disableHotkeys)
			updateKeyboard();
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
		}else if(cmd.equalsIgnoreCase("#bind")) {
			if(args.length != 3) {
				Util.sendMessage("#bind <key> <mod-name>");
				return;
			}
			else if(Hotkey.containsKey(hotkeys, Keyboard.getKeyIndex(args[1])))
				Util.sendMessage("\u00a74Key already registered.");
			else if(mods.getBindable(args[2].toLowerCase()) == null)
				Util.sendMessage("\u00a74Bindable does not exist.");
			else
				hotkeys.add(new Hotkey(Keyboard.getKeyIndex(args[1]), mods.getBindable(args[2].toLowerCase())));
		}else if(cmd.equalsIgnoreCase("#mods")) {
			Iterator<Entry<String, Bindable>> it = mods.getBindEntrys().iterator();
			String s = "Bindables: "+it.next().getKey();
			while(it.hasNext()) {
				s+=", "+it.next().getKey();
			}
			Util.sendMessage(s);
		}else if(cmd.equalsIgnoreCase("#unbind"))
			if(args.length < 2)
				Util.sendMessage("#unbind <key>");
			else
				Util.removeHotkeyFromList(hotkeys, Keyboard.getKeyIndex(args[1]));
		else if(cmd.equalsIgnoreCase("#say")) {
			if(args.length == 1) {
				Util.sendMessage("\u00a74Please enter a message.");
				return;
			}
			String msg = args[1];
			for(int i = 2; i < args.length; i++)
				msg += " " + args[i];
			Util.sendChat(msg);
		}else if(cmd.equalsIgnoreCase("#binds")) {
			StringBuilder sb = new StringBuilder();
			for(Hotkey hk : hotkeys)
				sb.append((sb.toString() == "" ? "" : ", ") + Keyboard.getKeyName(hk.key) + ":" + hk.handler.getName());
			Util.sendMessage("Hotkeys: "+sb.toString());
		}else if(cmd.equalsIgnoreCase("#give"))
			try {
				Util.cheatItem(new ItemStack(Item.getByNameOrId(args[1])), 0);
			}catch(Exception e) {
				Util.sendMessage(e.toString());
			}
		else if(cmd.equalsIgnoreCase("#givebypass"))
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
			}catch(Exception e) {
				Util.sendMessage(e.toString());
			}
		else if(cmd.equalsIgnoreCase("#debug"))
		{
			mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
					"random.explode", 4F, mc.theWorld.rand.nextFloat() * 0.1F + 0.9F);
			for(Addon a : addonManager.getAddons())
			{
				AddonProperties ap = addonManager.getProps(a);
				Util.sendMessage(a.getName() + " " + ap.name + " " + ap.author + " " + ap.version + " " + ap.mainClass + " " + ap.desc);
			}
			Util.sendMessage("Hotkeys are " + (disableHotkeys ? "disabled" : "enabled"));
			Util.sendMessage(Consts.dotMinecraftPath);
			Util.sendMessage(mods.authMeCrack.getCrs());
		}
		else if(cmd.equalsIgnoreCase("#set"))
			options.set(args);
		else if(cmd.equalsIgnoreCase("#get"))
			options.get(args);
		else if(cmd.equalsIgnoreCase("#list"))
			options.list(args);
		else addonManager.execCmd(args);
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

	public boolean isInvis() {
		return invis;
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
