package de.chrissx.iapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.lwjgl.input.Keyboard;

import de.chrissx.ChatGuiRenameWorld;
import de.chrissx.HackedClient;
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
	public final List<Command> commands = new ArrayList<Command>();

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
			commands.add(new Command(ce.getArgv0(), (t) -> ce.processCommand(t)));

		commands.add(new Command("alt", (t) -> {
			String s = Util.combineParts(t, 1, " ");
			HackedClient.getClient().guiRenameWorld(s, new ChatGuiRenameWorld(s));
		}));

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
			if (args.length < 2) {
				Util.sendError("Please enter a command!");
				return;
			}
			String Cmd = args[1];
			for (int i = 2; i < args.length; i++)
				Cmd += " " + args[i];
			Util.cheatCmdBlock(Cmd);
		}));

		commands.add(new Command("print-players", (args) -> {
			for (EntityPlayer p : mc.theWorld.playerEntities)
				Util.sendMessage(p.getName() + " (X: " + (long) p.posX + ", Y: " + (long) p.posY + ", Z: "
				                 + (long) p.posZ + ")");
		}));

		commands.add(new Command("bind", (args) -> {
			if (args.length != 3) {
				Util.sendMessage("#bind <key> <mod-name>");
				return;
			}
			int keyId = Util.getKeyId(args[1]);
			Bindable bindable = hc.getMods().getBindable(args[2].toLowerCase());
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
			if (args.length < 2)
				Util.sendMessage("#unbind <key>");
			else
				Util.removeHotkeyFromList(hc.getHotkeys(), Util.getKeyId(args[1]));
		}));

		commands.add(new Command("say", (args) -> {
			if (args.length == 1) {
				Util.sendError("Please enter a message.");
				return;
			}
			String msg = args[1];
			for (int i = 2; i < args.length; i++)
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
			try {
				Util.cheatItem(new ItemStack(Item.getByNameOrId(args[1])), 0);
			} catch (Exception e) {
				Util.sendMessage(e.toString());
			}
		}));

		commands.add(new Command("givebypass", (args) -> {
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
			} catch (Exception e) {
				Util.sendMessage(e.toString());
			}
		}));

		commands.add(new Command("debug", (args) -> {
			mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "random.explode",
			                            4F, mc.theWorld.rand.nextFloat() * 0.1F + 0.9F);
			for (Addon a : addons) {
				Util.sendMessage(a.name + " " + a.author + " " + a.version + " " + a.description);
			}
			Util.sendMessage("Hotkeys are " + (hc.hotkeysDisabled() ? "disabled" : "enabled"));
			Util.sendMessage(Consts.dotMinecraftPath);
		}));

		commands.add(new Command("help", (args) ->
		                         Util.sendMessage(getHelp())
		                        ));
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
		String cmd = args[0];
		if (cmd.charAt(0) == '#')
			cmd = cmd.substring(1);
		for (Command c : commands)
			if (c.cmd.equalsIgnoreCase(cmd)) {
				c.handler.accept(args);
				return;
			}
		Util.sendError(getHelp());
	}

	public int getBuildNumber() {
		return Consts.BLDNUM;
	}

	public String getHelp() {
		StringBuilder s = new StringBuilder("Commands:");
		for (Command c : commands) {
			s.append(' ');
			s.append(c.cmd);
		}
		return s.toString();
	}
}
