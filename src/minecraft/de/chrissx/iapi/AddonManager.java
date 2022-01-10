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

	Map<Addon, AddonProperties> addons = new HashMap<Addon, AddonProperties>();
	List<Command> commands = new ArrayList<Command>();

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
	public void init(String addonPath) {
		path = addonPath;

		for (File f : new File(path).listFiles())
			try {
				AddonProperties p = loadProperties(f);
				addAddon((Addon) URLClassLoader.newInstance(new URL[] { f.toURI().toURL() }).loadClass(p.mainClass)
				         .newInstance(), p);
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
	 * Loads the addon's properties.
	 *
	 * @param f The file that should contain the addon
	 * @return The loaded props
	 * @throws ZipException
	 * @throws IOException
	 */
	AddonProperties loadProperties(File f) throws ZipException, IOException {
		String name = "";
		String author = "";
		String version = "";
		String desc = "";
		String main = "";
		String s;
		ZipFile zip = new ZipFile(f);
		BufferedReader br = new BufferedReader(
		    new InputStreamReader(zip.getInputStream(zip.getEntry("CXCLIENT-ADDON")), StandardCharsets.UTF_8));

		while ((s = br.readLine()) != null) {
			String[] tokens = s.split(" ");
			if (tokens[0].equalsIgnoreCase("name"))
				name = Util.combineParts(tokens, 1, " ");
			else if (tokens[0].equalsIgnoreCase("author"))
				author = Util.combineParts(tokens, 1, " ");
			else if (tokens[0].equalsIgnoreCase("version"))
				version = Util.combineParts(tokens, 1, " ");
			else if (tokens[0].equalsIgnoreCase("desc"))
				desc = Util.combineParts(tokens, 1, " ");
			else if (tokens[0].equalsIgnoreCase("main"))
				main = tokens[1];
			else
				Util.info("Can't read addon config line \"" + s + "\".");
		}
		zip.close();
		return new AddonProperties(name, author, version, desc, main);
	}

	/**
	 * gets an addon by a name
	 *
	 * @param name the name of the addon
	 * @return the addon specified by the name
	 */
	public Addon getAddon(String name) {
		for (Entry<Addon, AddonProperties> e : addons.entrySet())
			if (e.getValue().name == name)
				return e.getKey();
		return null;
	}

	/**
	 * gets the props by the addon
	 *
	 * @param a the addon
	 * @return the properties of the addon
	 */
	public AddonProperties getProps(Addon a) {
		return addons.get(a);
	}

	/**
	 * adds the addon and the properties to the internal maps
	 *
	 * @param a the addon
	 * @param p the props of the addon
	 */
	public void addAddon(Addon a, AddonProperties p) {
		addons.put(a, p);
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

	public Set<Addon> getAddons() {
		return addons.keySet();
	}

	public String getName(Addon a) {
		return addons.get(a).name;
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
