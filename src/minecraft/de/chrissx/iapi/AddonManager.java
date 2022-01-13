package de.chrissx.iapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.chrissx.ChatGuiRenameWorld;
import de.chrissx.HackedClient;
import de.chrissx.mods.CommandExecutor;
import de.chrissx.util.Consts;
import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

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
			commands.add(new Command(ce.getArgv0(), new Consumer<String[]>() {
			@Override
			public void accept(String[] t) {
				ce.processCommand(t);
			}
		}));

		commands.add(new Command("#alt", new Consumer<String[]>() {
			@Override
			public void accept(String[] t) {
				String s = Util.combineParts(t, 1, " ");
				HackedClient.getClient().guiRenameWorld(s, new ChatGuiRenameWorld(s));
			}
		}));

		commands.add(new Command("#changelog", new Consumer<String[]>() {
			@Override
			public void accept(String[] t) {
				for (String s : Consts.changelog)
					Util.sendMessage(s);
			}
		}));

		commands.add(new Command("#credits", new Consumer<String[]>() {
			@Override
			public void accept(String[] t) {
				for (String s : Consts.credits)
					Util.sendMessage(s);
			}
		}));
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
		    new InputStreamReader(zip.getInputStream(zip.getEntry("addon.main")), StandardCharsets.UTF_8)).readLine();
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
		Util.sendMessage(getHelp() + Consts.extraHelp);
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
