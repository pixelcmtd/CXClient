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

public class T {

	public static Random r = new Random(System.currentTimeMillis());
	static volatile boolean d = false;
	static volatile long t;
	static volatile U w = null;
	static volatile int C = 0;
	
	public static void d(String U, File f, U W) {
		try {
			URL u = new URL(U);
			URLConnection c = u.openConnection();
			c.connect();
			long l = c.getContentLength();
			BufferedInputStream b = new BufferedInputStream(u.openStream());
			FileOutputStream F = new FileOutputStream(f);
			byte D[] = new byte[512];
			t = 0;
			d = true;
			w = W;
			new Thread(new Runnable() {
				@Override
				public void run() {
					long stm = System.currentTimeMillis();
					while(d) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						W.p((int)(t / l * 100));
						try {
							W.t("Downloading new jar - " + Double.toString(t / 131.072D / (double)(System.currentTimeMillis() - stm)).substring(0, 5) + "Mib/s(Avg)");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			while ((C = b.read(D, 0, D.length)) != -1) {
				F.write(D, 0, C);
				t += C;
			}
			d = false;
			F.close();
	    }catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static byte[] d(String U) {
		try {
			URL u = new URL(U);
			URLConnection c = u.openConnection();
			c.connect();
			long l = c.getContentLength();
			BufferedInputStream b = new BufferedInputStream(u.openStream());
			ByteArrayOutputStream B = new ByteArrayOutputStream();
			byte d[] = new byte[512];
			while ((C = b.read(d, 0, d.length)) != -1)
				B.write(d, 0, C);
			return B.toByteArray();
	    }catch (Exception e) {
	    	e.printStackTrace();
	    	return new byte[] { (byte) 0xFA, 0x11 };
	    }
	}
	
	public static String generateTempFile(String n, String e) {
		String t = System.getProperty("java.io.tmpdir");
		File f = new File(Paths.get(t, n + "_" + r.nextLong() + e).toString());
		while(f.exists())
			f = new File(Paths.get(t, n + "_" + r.nextLong() + e).toString());
		return f.toString();
	}
}
