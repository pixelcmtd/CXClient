package de.chrissx.iapi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.chrissx.util.Util;
import net.minecraft.client.Minecraft;

public class AddonManager {

	List<CommandExecutor> cmdExecs = new ArrayList<CommandExecutor>();

	Map<Addon, AddonProperties> addons = new HashMap<Addon, AddonProperties>();

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
	 * @param addonPath the path where the addons are located
	 */
	public void init(String addonPath)
	{
		path = addonPath;
		
		for(File f : new File(path).listFiles())
			try {
				AddonProperties p = loadProperties(f);
				addAddon((Addon) URLClassLoader.newInstance(new URL[] {f.toURI().toURL()}).loadClass(p.mainClass).newInstance(), p);
			} catch (Exception e) {
				mc.logger.fatal("Failed to load addon " + f);
				e.printStackTrace();
			}
	}

	/**
	 * Loads the addon's properties.
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
		ZipFile zip = new ZipFile(f);
		byte[] bfr = new byte[1024*1024]; //yea this means the c.addon file cannot be larger than 1 MiB
		int size = zip.getInputStream(zip.getEntry("c.addon")).read(bfr);
		String rawData = new String(bfr, 0, size, StandardCharsets.UTF_8);
		zip.close();
		for(String s : rawData.replace("\r", "").split("\n")) {
			String[] tokens = s.split(" ");
			if(tokens[0].equalsIgnoreCase("name"))
				name = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("author"))
				author = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("version"))
				version = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("desc"))
				desc = Util.combineParts(tokens, 1, " ");
			else if(tokens[0].equalsIgnoreCase("main"))
				main = tokens[1];
		}
		return new AddonProperties(name, author, version, desc, main);
	}

	/**
	 * gets an addon by a name
	 * @param name the name of the addon
	 * @return the addon specified by the name
	 */
	public Addon getAddon(String name) {
		for(Entry<Addon, AddonProperties> e : addons.entrySet())
			if(e.getValue().name == name)
				return e.getKey();
		return null;
	}

	/**
	 * gets the props by the addon
	 * @param a the addon
	 * @return the properties of the addon
	 */
	public AddonProperties getProps(Addon a)
	{
		return addons.get(a);
	}

	/**
	 * adds the addon and the properties to the internal maps
	 * @param a the addon
	 * @param p the props of the addon
	 */
	public void addAddon(Addon a, AddonProperties p) {
		addons.put(a, p);
	}

	/**
	 * adds the commandexecutor to the list
	 * @param cmdExec the commandexecutor
	 */
	public void registerCommandExecutor(CommandExecutor cmdExec) {
		cmdExecs.add(cmdExec);
	}

	/**
	 * tries to execute the command with every registered commandexecutor
	 * @param args the args of the processCommand
	 * @return true if any commandexecutor was able to execute the command, false if not
	 */
	public boolean execCmd(String[] args)
	{
		for(CommandExecutor ce : cmdExecs)
			if(ce.onCommand(args))
				return true;
		return false;
	}

	public Set<Addon> getAddons()
	{
		return addons.keySet();
	}

	public String getName(Addon a)
	{
		return addons.get(a).name;
	}
}
