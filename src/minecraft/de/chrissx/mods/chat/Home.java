package de.chrissx.mods.chat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.chrissx.humanapis.HomeApi;
import de.chrissx.mods.Bindable;
import de.chrissx.mods.EapiModule;
import de.chrissx.util.Util;

public class Home extends EapiModule implements Bindable {

	public Home() {
		super("Home");
		sethomedir = getApiDir("sethome");
		homedir = getApiDir("home");
	}

	private File sethomedir;
	private File homedir;

	@Override
	public void onHotkey() {
		HomeApi.home("");
	}

	@Override
	public String getName() {
		return "Home";
	}

	@Override
	public void apiUpdate() {
		for(File f : sethomedir.listFiles())
			if(f.isFile()) {
				try {
					HomeApi.sethome(Util.chatFilter(Files.readFirstLine(f, Charsets.UTF_8)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				f.delete();
			}

		for(File f : homedir.listFiles())
			if(f.isFile()) {
				try {
					HomeApi.home(Util.chatFilter(Files.readFirstLine(f, Charsets.UTF_8)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				f.delete();
			}
	}
}
