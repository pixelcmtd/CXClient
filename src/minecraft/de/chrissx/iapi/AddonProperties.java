package de.chrissx.iapi;

public class AddonProperties {

	public final String name;
	public final String author;
	public final String version;
	public final String desc;
	public final String mainClass;
	
	public AddonProperties(String name, String author, String version, String desc, String mainClass)
	{
		this.name = name;
		this.author = author;
		this.version = version;
		this.desc = desc;
		this.mainClass = mainClass;
	}
}
