package de.chrissx.iapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.lwjgl.input.Keyboard;

import de.chrissx.HackedClient;
import de.chrissx.alts.Alt;
import de.chrissx.alts.mcleaks.McLeaksApi;
import de.chrissx.hotkeys.Hotkey;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.CommandExecutor;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AddonManager {

	public final List<Addon> addons = new ArrayList<Addon>();
	public final List<CommandExecutor> commands = new ArrayList<CommandExecutor>();

	/**
	 * the path in which the addons are located
	 */
	String path;

	/**
	 * the minecraft instance
	 */
	final Minecraft mc = Minecraft.getMinecraft();

	final HackedClient hc = HackedClient.getClient();

	// TODO: something like a #enabled command
	// TODO: commands to get `Consts.mcVersion`, `mc.getVersion()`, `Consts.version` and `Consts.APIVER`
	// TODO: also a way to get the values of the mods
	// TODO: make all (ALL) commands # agnostic
	/**
	 * initializes the addonmanager and loads the addons from the path
	 *
	 * @param addonPath the path where the addons are located
	 */
	public AddonManager(String addonPath) {
		path = addonPath;

		for (File f : new File(path).listFiles())
			try {
				addAddon((Addon) URLClassLoader.newInstance(new URL[] { f.toURI().toURL() }).loadClass(getMainClass(f))
				         .newInstance());
			} catch (Exception e) {
				Util.fatal("Failed to load addon " + f);
				e.printStackTrace();
			}

		for (final CommandExecutor ce : HackedClient.getClient().getMods().commandExecutors)
			commands.add(new Command(ce.argv0(), (t) -> ce.processCommand(t)));

		commands.add(new Command("changelog", (t) -> {
			for (String s : Consts.changelog)
				Util.sendMessage(s);
		}));

		commands.add(new Command("credits", (t) -> {
			for (String s : Consts.credits)
				Util.sendMessage(s);
		}));

		// TODO: for all below think about putting them in their own commandexecutors
		commands.add(new Command("cmdblock", (args) -> {
			if (args.length < 1) {
				Util.sendError("Please enter a command!");
				return;
			}
			String Cmd = args[0];
			for (int i = 1; i < args.length; i++)
				Cmd += " " + args[i];
			Util.cheatCmdBlock(Cmd);
		}));

		commands.add(new Command("print-players", (args) -> {
			for (EntityPlayer p : mc.theWorld.playerEntities)
				Util.sendMessage(p.getName() + " (X: " + (long) p.posX + ", Y: " + (long) p.posY + ", Z: "
				                 + (long) p.posZ + ")");
		}));

		commands.add(new Command("bind", (args) -> {
			if (args.length != 2) {
				Util.sendMessage("#bind <key> <mod-name>");
				return;
			}
			int keyId = Util.getKeyId(args[0]);
			Bindable bindable = hc.getMods().getBindable(args[1].toLowerCase());
			if (keyId == Keyboard.KEY_NONE)
				Util.sendError("LWJGL can't find that key.");
			else if (Hotkey.containsKey(hc.getHotkeys(), keyId))
				Util.sendError("Key already registered.");
			else if (bindable == null)
				Util.sendError("That Bindable does not exist.");
			else
				hc.getHotkeys().add(new Hotkey(keyId, bindable));
		}));

		commands.add(new Command("mods", (args) -> {
			Iterator<Bindable> it = hc.getMods().bindables.iterator();
			String s = "Bindables: " + it.next().getName();
			while (it.hasNext()) {
				s += ", " + it.next().getName();
			}
			Util.sendMessage(s);
		}));

		commands.add(new Command("unbind", (args) -> {
			if (args.length < 1)
				Util.sendMessage("#unbind <key>");
			else
				Util.removeHotkeyFromList(hc.getHotkeys(), Util.getKeyId(args[1]));
		}));

		commands.add(new Command("say", (args) -> {
			if (args.length < 1) {
				Util.sendError("Please enter a message.");
				return;
			}
			String msg = args[0];
			for (int i = 1; i < args.length; i++)
				msg += " " + args[i];
			Util.sendChat(msg);
		}));

		commands.add(new Command("binds", (args) -> {
			StringBuilder sb = new StringBuilder();
			for (Hotkey hk : hc.getHotkeys())
				sb.append((sb.toString() == "" ? "" : ", ") + Keyboard.getKeyName(hk.key) + ":"
				          + hk.handler.getName());
			Util.sendMessage("Hotkeys: " + sb.toString());
		}));

		commands.add(new Command("give", (args) -> {
			// TODO: checks
			try {
				Util.cheatItem(new ItemStack(Item.getByNameOrId(args[0])), 0);
			} catch (Exception e) {
				Util.sendMessage(e.toString());
			}
		}));

		commands.add(new Command("givebypass", (args) -> {
			// TODO: checks
			try {
				ItemStack itm = new ItemStack(Item.getByNameOrId("furnace"), 1);
				NBTTagCompound itmtag = new NBTTagCompound();
				NBTTagCompound blockentitytag = new NBTTagCompound();
				NBTTagList items = new NBTTagList();
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Count", (byte) 1);
				item.setShort("Damage", (short) 0);
				item.setString("id", args[0]);
				item.setByte("Slot", (byte) 2);
				items.appendTag(item);
				blockentitytag.setTag("Items", items);
				itmtag.setTag("BlockEntityTag", blockentitytag);
				itm.setTagCompound(itmtag);
				Util.cheatItem(itm, 0);
			} catch (Exception e) {
				Util.sendMessage(e.toString());
			}
		}));

		// FIXME: this is more verbose than every other command ever. is that better or worse?
		commands.add(Command.fromSubcommands("alt", new Command[] {
		new Command("login", (args) -> {
			try {
				if(args.length == 1) {
					hc.getAltManager().login(args[0], "");
					Util.sendMessage("Logged into cracked account.");
				} else if(args.length == 0)
					Util.sendMessage("login <email> [password] (don't use password if account is cracked)");
				else {
					hc.getAltManager().login(args[0], args[1]);
					Util.sendMessage("Logged into premium account.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Util.sendError(e.getMessage());
			}
		}),
		// TODO: remove
		new Command("help", (args) -> Util.sendMessage("Alt-commands: login, help, load, mcleaks, list, cxcsv, vault")),
		new Command("load", (args) -> {
			if(args.length == 1) {
				Util.sendError("Usage: load <alt>");
			} else {
				try {
					hc.getAltManager().loadAlt(args[0]);
					Util.sendMessage("Logged into " + (hc.getAltManager().currentAlt.isCracked() ? "cracked" : "premium") + " account.");
				} catch (Exception e) {
					e.printStackTrace();
					Util.sendError(e.getMessage());
				}
			}
		}),
		new Command("mcleaks", (args) -> {
			if(args.length < 1) {
				Util.sendMessage("mcleaks [token]");
				return;
			}
			try {
				hc.mcLeaksSession = McLeaksApi.redeemMcleaksToken(args[0]);
				hc.getAltManager().login(hc.mcLeaksSession.getMcname(), "");
				Util.sendMessage("Success.");
			} catch (Exception e) {
				e.printStackTrace();
				Util.sendError(e.getMessage());
			}
		}),
		new Command("list", (args) -> {
			String s = "";
			for(Alt a : hc.getAltManager().getAlts())
				try {
					s += (s == "" ? "" : ", ") + hc.getAltManager().getName(a);
				} catch (Exception e) {
					e.printStackTrace();
					Util.sendError(e.getMessage());
				}
			Util.sendMessage(s);
		}),
		Command.fromSubcommands("cxcsv", new Command[] {
			new Command("load", (args) -> {
				// TODO: check number of arguments
				try {
					hc.getAltManager().loadCxcsv(Paths.get(args[0]));
				} catch (Exception e) {
					e.printStackTrace();
					Util.sendError(e.getMessage());
				}
			}),
			new Command("store", (args) -> {
				// TODO: check number of arguments
				try {
					hc.getAltManager().saveCxcsv(args[0]);
				} catch (Exception e) {
					e.printStackTrace();
					Util.sendError(e.getMessage());
				}
			}),
			new Command("help", (args) -> Util.sendMessage("cxcsv load/store [file]")),
		}),
		new Command("clear", (args) -> hc.getAltManager().clear()),
		Command.fromSubcommands("vault", new Command[] {
			new Command("load", (args) -> {
				try {
					hc.getAltManager().loadVault(args[1], args[0]);
					Util.sendMessage("Load successful.");
				} catch (Exception e) {
					e.printStackTrace();
					Util.sendError(e.getMessage());
				}
			}),
			new Command("store", (args) -> {
				try {
					hc.getAltManager().storeVault(args[1], args[0]);
					Util.sendMessage("Store successful.");
				} catch (Exception e) {
					e.printStackTrace();
					Util.sendError(e.getMessage());
				}
			}),
			new Command("help", (args) -> Util.sendMessage("vault load/store [password] [file]"))
		}),
		                                     }));

		commands.add(new Command("debug", (args) -> {
			mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "random.explode",
			                            4F, mc.theWorld.rand.nextFloat() * 0.1F + 0.9F);
			for (Addon a : addons) {
				Util.sendMessage(a.name + " " + a.author + " " + a.version + " " + a.description);
			}
			Util.sendMessage("Hotkeys are " + (hc.disableHotkeys ? "disabled" : "enabled"));
			Util.sendMessage(Consts.dotMinecraftPath);
		}));

		commands.add(new Command("help", (a) -> Util.sendMessage(getHelp())));
	}

	/**
	 * Loads the addon's main class from `addon.main`.
	 *
	 * @param f The file that should contain the addon
	 * @return The main class of that addon
	 * @throws ZipException
	 * @throws IOException
	 */
	String getMainClass(File f) throws ZipException, IOException {
		ZipFile zip = new ZipFile(f);
		String main = new BufferedReader(
		    new InputStreamReader(zip.getInputStream(zip.getEntry("addon.main")), StandardCharsets.UTF_8))
		.readLine();
		zip.close();
		return main;
	}

	/**
	 * gets an addon by a name
	 *
	 * @param name the name of the addon
	 * @return the addon specified by the name
	 */
	public Addon getAddon(String name) {
		for (Addon a : addons)
			if (a.name == name)
				return a;
		return null;
	}

	/**
	 * adds the addon to the internal list
	 *
	 * @param a the addon
	 */
	public void addAddon(Addon a) {
		addons.add(a);
	}

	public void registerCommand(Command c) {
		commands.add(c);
	}

	/**
	 * executes the command
	 *
	 * @param args the ' '-splitted args
	 */
	public void execCmd(String[] args) {
		// TODO: merge with subcommands
		String cmd = args[0];
		if (cmd.charAt(0) == '#')
			cmd = cmd.substring(1);
		for (CommandExecutor c : commands)
			if (c.argv0().equalsIgnoreCase(cmd)) {
				c.processCommand(Arrays.copyOfRange(args, 1, args.length));
				return;
			}
		Util.sendError("Unknown command: " + cmd);
	}

	public int getBuildNumber() {
		return Consts.BLDNUM;
	}

	public String getHelp() {
		StringBuilder s = new StringBuilder("Commands:");
		for (CommandExecutor c : commands) {
			s.append(' ');
			s.append(c.argv0());
		}
		return s.toString();
	}
}
