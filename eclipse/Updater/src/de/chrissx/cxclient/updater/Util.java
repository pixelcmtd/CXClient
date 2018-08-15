package de.chrissx.cxclient.updater;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {

	public static Random rand = new Random(System.currentTimeMillis());
	static volatile boolean downloading = false;
	static volatile long total;
	static volatile UpdaterWindow window = null;
	static volatile int count = 0;
	
	public static void downloadFile(String url, File file, UpdaterWindow window) {
		try {
			URL u = new URL(url);
			URLConnection c = u.openConnection();
			c.connect();
			long len = c.getContentLength();
			BufferedInputStream bis = new BufferedInputStream(u.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			byte data[] = new byte[512];
			total = 0;
			downloading = true;
			Util.window = window;
			new Thread(new Runnable() {
				@Override
				public void run() {
					long stm = System.currentTimeMillis();
					while(downloading) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						window.setProgress((int)(total / len * 100));
						try {
							window.setWindowTitle("Downloading new jar - " + Double.toString(total / 131.072D / (double)(System.currentTimeMillis() - stm)).substring(0, 5) + "Mib/s(Avg)");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			while ((count = bis.read(data, 0, data.length)) != -1) {
				fos.write(data, 0, count);
				total += count;
			}
			downloading = false;
			fos.close();
	    }catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static byte[] downloadBytes(String url) {
		try {
			URL u = new URL(url);
			URLConnection c = u.openConnection();
			c.connect();
			long len = c.getContentLength();
			BufferedInputStream bis = new BufferedInputStream(u.openStream());
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			byte data[] = new byte[512];
			while ((count = bis.read(data, 0, data.length)) != -1)
				bytes.write(data, 0, count);
			return bytes.toByteArray();
	    }catch (Exception e) {
	    	e.printStackTrace();
	    	return new byte[] { (byte) 0xFA, 0x11 };
	    }
	}
	
	public static String generateTempFile(String name, String ext) {
		String tmp = System.getProperty("java.io.tmpdir");
		File f = new File(Paths.get(tmp, name + "_" + rand.nextLong() + ext).toString());
		while(f.exists())
			f = new File(Paths.get(tmp, name + "_" + rand.nextLong() + ext).toString());
		return f.toString();
	}
}
