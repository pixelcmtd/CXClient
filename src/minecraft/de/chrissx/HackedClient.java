package de.chrissx;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.google.common.io.Files;

import de.chrissx.alts.Alt;
import de.chrissx.alts.AltManager;
import de.chrissx.alts.CxcsvParser;
import de.chrissx.alts.mcleaks.McLeaksApi;
import de.chrissx.alts.mcleaks.McLeaksSession;
import de.chrissx.iapi.Addon;
import de.chrissx.iapi.AddonManager;
import de.chrissx.iapi.AddonProperties;
import de.chrissx.mods.Bindable;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;

public class HackedClient {
	static HackedClient instance = null;
	boolean invis = false;
	
	Map<Integer, Bindable> hotkeys = new HashMap<Integer, Bindable>();
	final ModList mods;
	final AltManager altManager;
	List<Integer> lastPressed = new ArrayList<Integer>();
	final Minecraft mc = Minecraft.getMinecraft();
	McLeaksSession mcLeaksSession = null;
	boolean disableHotkeys = true;
	final AddonManager addonManager;
	final Thread updateThread;
	
	public boolean stopPackets()
	{
		return mods.lag.isEnabled();
	}
	
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
			altManager.onShutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			HotkeySaving.saveHotkeys(Paths.get(Consts.hotkeyFile), hotkeys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//Disabled due to not working again (LimaCity basically f...ed us completely and we're getting around it in C# but decompression, debase64ing and whatsoever in java...nope, pay 4 ddl space...nop)
			//But it will be enabled again the the near future because with github.io we can do it
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		updateThread.stop();
	}
	
	public HackedClient()
	{
		instance = this;
		
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
		
		f = new File(Consts.runningFile);
		
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		f.deleteOnExit();
		
		for(Mod m : mods)
		{
			Util.checkIfExistsAndMake(Paths.get(Consts.modsPath, m.getName()).toString(), m.getName() + "Path");
		}
		
		updateThread = new Thread(new Runnable() {
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
								File f = Paths.get(Consts.togglePath, m.getName()).toFile();
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
									b.put(StandardCharsets.US_ASCII.encode(m.getName()).array());
									b.put((byte) (m.isEnabled() ? 1 : 0));
									b.put((byte) 11); //[VT] (Vertical Tab)
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							b.put((byte) 4);
							enabledFile.createNewFile();
							Files.write(b.array(), enabledFile);
							for(Mod m : mods)
								m.apiUpdate();
							invis = disableIAUI.exists();
						} catch (Exception e) {
							mc.logger.warn(e);
						}
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					mc.logger.fatal(e);
				}
			}
		});
		updateThread.start();
	}
	
	public void guiRenameWorld(String input, GuiRenameWorld gui) {
		try {
			String[] args = input.split(" ");
			String cmd = args[0];
			if(cmd.equalsIgnoreCase("#login")) {
				if(args.length == 2) {
					altManager.login(args[1], "");
					gui.setText("Logged into cracked account.");
				}else if(args.length == 1)
					gui.setText("#login <email> [password] - don't use password if account is cracked.");
				else {
					String pass = args[2];
					for(int i = 3; i < args.length; i++)
						pass+=" "+args[i];
					altManager.login(args[1], pass);
					gui.setText("Logged into premium account.");
				}
			}else if(cmd.equalsIgnoreCase("#help"))
				gui.setText("Alt-commands: #login, #help, #load, #mcleaks, #alts, #cxcsv");
			else if(cmd.equalsIgnoreCase("#load")) {
				altManager.loadAlt(args[1]);
				gui.setText("Logged into " + (altManager.currentAlt.isCracked() ? "cracked" : "premium") + " account.");
			}else if(cmd.equalsIgnoreCase("#mcleaks")) {
				String token = args[1];
				for(int i = 2; i < args.length; i++)
					token += " " + args[i];
				mcLeaksSession = McLeaksApi.redeemMcleaksToken(token);
				altManager.login(mcLeaksSession.getMcname(), "");
			}else if(cmd.equalsIgnoreCase("#alts"))
				for(Alt a : altManager.getAlts())
					gui.setText((gui.getText() == input ? "" : gui.getText() + ", ") + altManager.getName(a));
			else if(cmd.equalsIgnoreCase("#cxcsv")) {
				String s = args[1];
				for(int i = 2; i < args.length; i++)
					s += " " + args[i];
				altManager.getAlts().addAll(CxcsvParser.loadAlts(Paths.get(s)));
			}else
				guiRenameWorld("#help", gui);
		} catch (Exception e) {
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
		for(Entry<Integer, Bindable> hotkey : hotkeys.entrySet())
			if(Keyboard.isKeyDown(hotkey.getKey()) && !lastPressed.contains(hotkey.getKey()) && !(mc.currentScreen instanceof GuiChat))
				hotkey.getValue().onHotkey();
		
		lastPressed = new ArrayList<Integer>();
		for(int i : hotkeys.keySet())
			if(Keyboard.isKeyDown(i))
				lastPressed.add(i);
	}
	
	public AddonManager getAddonManager() {
		return addonManager;
	}
	
	public void onCommand(final String[] args) {
		final String cmd = args[0];
		
		if (cmd.equalsIgnoreCase("#text"))
			mods.text.processCommand(args);
		else if(cmd.equalsIgnoreCase("#changelog"))
			for(String s : Consts.changelog)
				Util.sendMessage(s);
      	else if(cmd.equalsIgnoreCase("#multitext"))
			mods.multiText.processCommand(args);
		else if(cmd.equalsIgnoreCase("#killpotion"))
			mods.killPotion.processCommand(args);
		else if(cmd.equalsIgnoreCase("#spam"))
			mods.spam.processCommand(args);
		else if(cmd.equalsIgnoreCase("#skinblink"))
			mods.skinBlinker.processCommand(args);
		else if(cmd.equalsIgnoreCase("#fastplace"))
			mods.fastPlace.processCommand(args);
		else if(cmd.equalsIgnoreCase("#cmdblock")) {
			if(args.length < 2) {
				Util.sendMessage("Please enter a command!");
				return;
			}
			String Cmd = args[1];
			for(int i = 2; i < args.length; i++)
				Cmd += " " + args[i];
			Util.cheatCmdBlock(Cmd);
		}else if(cmd.equalsIgnoreCase("#fastbreak"))
			mods.fastBreak.processCommand(args);
		else if(cmd.equalsIgnoreCase("#nofall"))
			mods.nofall.processCommand(args);
		else if(cmd.equalsIgnoreCase("#fullbright"))
			mods.fullbright.processCommand(args);
		else if(cmd.equalsIgnoreCase("#xray"))
			mods.xray.processCommand(args);
		else if(args[0].equalsIgnoreCase("#fasthit"))
			mods.fasthit.processCommand(args);
		else if(args[0].equalsIgnoreCase("#autoclicker"))
			mods.autoclicker.processCommand(args);
		else if(cmd.equalsIgnoreCase("#noswing"))
			mods.noswing.processCommand(args);
		else if(cmd.equalsIgnoreCase("#bind")) {
			if(args.length != 3) {
				Util.sendMessage("#bind <key> <mod-name>");
				return;
			}
			else if(hotkeys.containsKey(Keyboard.getKeyIndex(args[1])))
				Util.sendMessage("\u00a74Key already registered.");
			else if(mods.getBindable(args[2].toLowerCase()) == null)
				Util.sendMessage("\u00a74Mod-Name not correct.");
			else
				hotkeys.put(Keyboard.getKeyIndex(args[1]), mods.getBindable(args[2].toLowerCase()));
		}else if(cmd.equalsIgnoreCase("#mods")) {
			Iterator<Entry<String, Bindable>> it = mods.getBindEntrys().iterator();
			String s = "Bindables: "+it.next().getKey();
			while(it.hasNext()) {
				s+=", "+it.next().getKey();
			}
			Util.sendMessage(s);
		}else if(cmd.equalsIgnoreCase("#authmecrack"))
			mods.authMeCrack.processCommand(args);
		else if(cmd.equalsIgnoreCase("#antiafk"))
			mods.antiAfk.processCommand(args);
		else if(cmd.equalsIgnoreCase("#autosteal"))
			mods.autosteal.processCommand(args);
		else if(cmd.equalsIgnoreCase("#killaura"))
			mods.killaura.processCommand(args);
		else if(cmd.equalsIgnoreCase("#nuker"))
			mods.nuker.processCommand(args);
		else if(cmd.equalsIgnoreCase("#sneak"))
			mods.sneak.processCommand(args);
		else if(cmd.equalsIgnoreCase("#tracer"))
			mods.tracer.processCommand(args);
		else if(cmd.equalsIgnoreCase("#clearspam"))
			mods.spam.processCommand(args);
		else if(cmd.equalsIgnoreCase("#panic")) {
			mods.panic.processCommand(args);
		}else if(cmd.equalsIgnoreCase("#throw"))
			mods.thrower.processCommand(args);
		else if(cmd.equalsIgnoreCase("#flyvanilla"))
			mods.vanillaFly.processCommand(args);
		else if(cmd.equalsIgnoreCase("#masstpa"))
			mods.massTpa.processCommand(args);
		else if(cmd.equalsIgnoreCase("#autoarmor"))
			mods.autoArmor.processCommand(args);
		else if(cmd.equalsIgnoreCase("#trollpotion"))
			mods.trollPotion.processCommand(args);
		else if(cmd.equalsIgnoreCase("#twerk"))
			mods.twerk.processCommand(args);
		else if(cmd.equalsIgnoreCase("#unbind"))
			if(args.length < 2)
				Util.sendMessage("#unbind <key>");
			else
				hotkeys.remove(Keyboard.getKeyIndex(args[1]));
		else if(cmd.equalsIgnoreCase("#fastladder"))
			mods.fastLadder.processCommand(args);
		else if(cmd.equalsIgnoreCase("#reach"))
			mods.reach.processCommand(args);
		else if(cmd.equalsIgnoreCase("#velocity"))
			mods.velocity.processCommand(args);
		else if(cmd.equalsIgnoreCase("#flyac1"))
			mods.acFly1.processCommand(args);
		else if(cmd.equalsIgnoreCase("#flyac2"))
			mods.acFly2.processCommand(args);
		else if(cmd.equalsIgnoreCase("#timer"))
			mods.timer.processCommand(args);
		else if(cmd.equalsIgnoreCase("#speedac1"))
			mods.acSpeed1.processCommand(args);
		else if(cmd.equalsIgnoreCase("#sprint"))
			mods.autosprint.processCommand(args);
		else if(cmd.equalsIgnoreCase("#say")) {
			if(args.length == 1) {
				Util.sendMessage("\u00a74Please enter a message.");
				return;
			}
			String msg = args[1];
			for(int i = 2; i < args.length; i++)
				msg += " " + args[i];
			Util.sendChat(msg);
		}else if(cmd.equalsIgnoreCase("#bedfucker"))
			mods.bedFucker.processCommand(args);
		else if(cmd.equalsIgnoreCase("#speedlegit"))
			mods.legitSpeed.processCommand(args);
		else if(cmd.equalsIgnoreCase("#experimental"))
			mods.freecam.processCommand(args);
		else if(cmd.equalsIgnoreCase("#aimbot"))
			mods.aimbot.processCommand(args);
		else if(cmd.equalsIgnoreCase("#jailsmcbot"))
			mods.jailsmcBot.processCommand(args);
		else if(cmd.equalsIgnoreCase("#binds")) {
			StringBuilder sb = new StringBuilder();
			for(Entry<Integer, Bindable> hotkey : hotkeys.entrySet())
				sb.append((sb.toString() == "" ? "" : ", ")+Keyboard.getKeyName(hotkey.getKey())+":"+hotkey.getValue().getName());
			Util.sendMessage("Hotkeys: "+sb.toString());
		}else if(cmd.equalsIgnoreCase("#norender"))
			mods.noRender.processCommand(args);
		else if(cmd.equalsIgnoreCase("#give"))
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
		else if(cmd.equalsIgnoreCase("#stepjump"))
			mods.stepJump.processCommand(args);
		else if(cmd.equalsIgnoreCase("#debug"))
		{
			mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "random.explode", 4F, mc.theWorld.rand.nextFloat() * 0.1F + 0.9F);
			for(Addon a : addonManager.getAddons())
			{
				AddonProperties ap = addonManager.getProps(a);
				Util.sendMessage(a.getName() + " " + ap.getName() + " " + ap.getAuthor() + " " + ap.getVersion() + " " + ap.getMainClass() + " " + ap.getDesc());
			}
			Util.sendMessage("Hotkeys: " + (disableHotkeys ? "disabled" : "enabled"));
		}
		else if(cmd.equalsIgnoreCase("#credits"))
		{
			for(String s : Consts.credits)
				Util.sendMessage(s);
		}
		else if(cmd.equalsIgnoreCase("#jetpack"))
			mods.jetpack.processCommand(args);
		else if(cmd.equalsIgnoreCase("#regen"))
			mods.regen.processCommand(args);
		else if(cmd.equalsIgnoreCase("#flip"))
			mods.flip.processCommand(args);
		else if(cmd.equalsIgnoreCase("#lag"))
			mods.lag.processCommand(args);
		else if(cmd.equalsIgnoreCase("#scaffoldwalk"))
			mods.scaffoldWalk.processCommand(args);
		else if(cmd.equalsIgnoreCase("#fastfall"))
			mods.fastFall.processCommand(args);
		else if(cmd.equalsIgnoreCase("#fasteat"))
			mods.fastEat.processCommand(args);
		else if(cmd.equalsIgnoreCase("#autoswitch"))
			mods.autoSwitch.processCommand(args);
		else if(cmd.equalsIgnoreCase("#tired"))
			mods.tired.processCommand(args);
		else if(cmd.equalsIgnoreCase("#derp"))
			mods.derp.processCommand(args);
		else if(cmd.equalsIgnoreCase("#antipotion"))
			mods.antiPotion.processCommand(args);
		else if(cmd.equalsIgnoreCase("#nocobweb"))
			mods.noCobweb.processCommand(args);
		else if(cmd.equalsIgnoreCase("#parkour"))
			mods.parkour.processCommand(args);
		else if(cmd.equalsIgnoreCase("#phase"))
			mods.phase.processCommand(args);
		else if(cmd.equalsIgnoreCase("#fastbow"))
			mods.fastBow.processCommand(args);
		else if(cmd.equalsIgnoreCase("#spider"))
			mods.spider.processCommand(args);
		else if(cmd.equalsIgnoreCase("#antifire"))
			mods.antiFire.processCommand(args);
		else if(cmd.equalsIgnoreCase("#highjump"))
			mods.highJump.processCommand(args);
		else if(cmd.equalsIgnoreCase("#autowalk"))
			mods.autoWalk.processCommand(args);
		else if(cmd.equalsIgnoreCase("#autorespawn"))
			mods.autoRespawn.processCommand(args);
		else if(cmd.equalsIgnoreCase("#dolphin"))
			mods.dolphin.processCommand(args);
		else if(cmd.equalsIgnoreCase("#kaboom"))
			mods.kaboom.processCommand(args);
		else if(cmd.equalsIgnoreCase("#glide"))
			mods.glide.processCommand(args);
		else if(cmd.equalsIgnoreCase("#rollhead"))
			mods.rollHead.processCommand(args);
		else if(addonManager.execCmd(args))
			;
		else
			Util.sendMessage(Consts.help);
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

	public Map<Integer, Bindable> getHotkeys() {
		return hotkeys;
	}

	public ModList getMods() {
		return mods;
	}

	public AltManager getAltManager() {
		return altManager;
	}
}