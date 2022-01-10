package de.chrissx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import de.chrissx.options.IOptions;
import de.chrissx.util.Util;

public class EapiOptions implements IOptions {
	public int sleep = 1000;

	@Override
	public void init(File config) {
		if(!config.exists()) return;
		try {
			FileInputStream fis = new FileInputStream(config);
			byte[] b = new byte[4];
			fis.read(b);
			fis.close();
			sleep = (b[0] >> 24) | (b[1] >> 16) | (b[2] >> 8) | (b[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void set(String[] args) {
		if(args.length > 2 && args[2].equalsIgnoreCase("sleep"))
			sleep = Integer.parseInt(args[3]);
		else list(args);
	}

	@Override
	public void get(String[] args) {
		if(args.length > 2 && args[2].equalsIgnoreCase("sleep"))
			Util.sendMessage(Integer.toString(sleep));
		else list(args);
	}

	@Override
	public void list(String[] args) {
		Util.sendMessage("Options: sleep");
	}

	@Override
	public void stop(File config) {
		try {
			FileOutputStream fos = new FileOutputStream(config);
			fos.write(new byte[] {(byte)(sleep << 24), (byte)(sleep << 16), (byte)(sleep << 8), (byte)sleep});
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
