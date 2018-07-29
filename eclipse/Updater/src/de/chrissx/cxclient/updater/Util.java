package de.chrissx.cxclient.updater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;

public class Util {

	public static Random random = new Random();
	private static volatile boolean downloading = false;
	private static volatile int total = 0;
	private static volatile UpdaterWindow window = null;
	private static volatile int count = 0;
	
	public static void downloadFile(String url, String file, UpdaterWindow window) throws IOException {
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
			final FileOutputStream fout = new FileOutputStream(file);
	        
			final byte data[] = new byte[65536];
			total = 0;
			downloading = true;
			Util.window = window;
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					long startTime = System.nanoTime();
					while(downloading) {
						try {
							Thread.sleep(5);
						} catch (InterruptedException e1) {}
						window.setProgress((int)(100l*(long)total/27000000l));
						try {
							window.setWindowTitle("Downloading new jar - "+Double.toString((((double)(total/1048576))/((double)(System.nanoTime()-startTime)/64000000D))/2).substring(0, 5)+"Mb/s(Avg)");
						} catch (Exception e) {}
					}
				}
			}).start();
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
				total += count;
			}
			downloading = false;
			fout.close();
	    }catch (Exception e) {}
	}
	
	public static String generateTempFile(String tempPath, String name, String ext) {
		String out = Paths.get(tempPath, name+"_"+random.nextInt()+ext).toString();
		File f = new File(out);
		while(f.exists())
			f = new File(out = Paths.get(tempPath, name+"_"+random.nextInt()+ext).toString());
		return out;
	}
}
