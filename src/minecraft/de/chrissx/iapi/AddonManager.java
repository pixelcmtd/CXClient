package de.chrissx.iapi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.chrissx.Util;
import net.minecraft.client.Minecraft;

public class AddonManager {
	
	List<CommandExecutor> cmdExecs = new ArrayList<CommandExecutor>();
	
	Map<String, Addon> addons = new HashMap<String, Addon>();
	Map<Addon, AddonProperties> props = new HashMap<Addon, AddonProperties>();
	
	/**
	 * the path in which the addons are located
	 */
	String path;
	
	/**
	 * the minecraft instant
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
				addAddon((Addon) URLClassLoader.newInstance(new URL[] {f.toURI().toURL()}).loadClass(p.getMainClass()).newInstance(), p);
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
		AddonProperties props = new AddonProperties();
		ZipFile zip = new ZipFile(f);
		byte[] bfr = new byte[1024*1024]; //yea this means the c.addon file cannot be larger than 1 MiB
		int size = zip.getInputStream(zip.getEntry("c.addon")).read(bfr);
		String rawData = new String(bfr, 0, size, StandardCharsets.UTF_8);
		zip.close();
		for(String s : rawData.replace("\r", "").split("\n")) {
			String[] tokens = s.split(" ");
			if(tokens[0].equalsIgnoreCase("name"))
				props.setName(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("author"))
				props.setAuthor(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("version"))
				props.setVersion(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("desc"))
				props.setDesc(Util.combineParts(tokens, 1, " "));
			else if(tokens[0].equalsIgnoreCase("main"))
				props.setMainClass(tokens[1]);
		}
		return props;
	}
	
	/**
	 * gets an addon by a name
	 * @param name the name of the addon
	 * @return the addon specified by the name
	 */
	public Addon getAddon(String name) {
		return addons.get(name.toLowerCase());
	}
	
	/**
	 * gets the props by the addon
	 * @param a the addon
	 * @return the properties of the addon
	 */
	public AddonProperties getProps(Addon a)
	{
		return props.get(a);
	}
	
	/**
	 * adds the addon and the properties to the internal maps
	 * @param a the addon
	 * @param p the props of the addon
	 */
	public void addAddon(Addon a, AddonProperties p) {
		addons.put(a.getName().toLowerCase(), a);
		props.put(a, p);
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
	
	public Collection<Addon> getAddons()
	{
		return addons.values();
	}
}
