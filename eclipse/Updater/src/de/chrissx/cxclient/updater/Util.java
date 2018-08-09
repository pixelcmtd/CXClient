package de.chrissx.cxclient.updater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Random;

public class Util {

	public static Random random = new Random();
	static volatile boolean downloading = false;
	static volatile long total;
	static volatile UpdaterWindow window = null;
	static volatile int count = 0;
	
	public static void downloadFile(String url, String file, UpdaterWindow window) throws IOException {
		try {
			URL u = new URL(url);
			URLConnection c = u.openConnection();
			c.connect();
			long len = c.getContentLength();
			BufferedInputStream bis = new BufferedInputStream(u.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			final byte data[] = new byte[512];
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
	
	public static String generateTempFile(String tempPath, String name, String ext) {
		File f = new File(Paths.get(tempPath, name + "_" + random.nextInt() + ext).toString());
		while(f.exists())
			f = new File(Paths.get(tempPath, name + "_" + random.nextInt() + ext).toString());
		return f.toString();
	}
}
