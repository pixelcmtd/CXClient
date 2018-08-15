package de.chrissx.cxclient.updater;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

public class M {
	public static final String v = "version url";
	public static final String c = "client url";
	public static final String V = T.generateTempFile("cxclient_version", ".txt");
	
	public static void main(String[] a) {
		try {
			U w;
			if(a.length == 0) {
				w = new U("Debug-Window", 500, 75, true);
				w.setVisible(true);
				for(int n = 5; n <= 100; n += 5) {
					Thread.sleep(200);
					w.p(n);
				}
			}else {
				String j = a[1];
				for(int i = 2; i < a.length; i++)
					j += " " + a[i];
				File r = new File(a[0]);
				File J = new File(j);
				while(r.exists())
					Thread.sleep(1);
				w = new U("CXClient-Updater", 500, 75, true);
				w.setVisible(true);
				w.t("Finding current build number.");
				w.p(0);
				ZipInputStream z = new ZipInputStream(new FileInputStream(J));
				w.p(20);
				ZipEntry e;
				while((e = z.getNextEntry()).getName() != "BLDNUM")
					z.closeEntry();
				w.p(50);
				byte[] b = new byte[(int) e.getSize()];
				w.p(60);
				z.read(b);
				z.closeEntry();
				z.close();
				w.p(80);
				int B = Integer.parseInt(new String(b));
				w.p(100);
				w.t("Getting server build number.");
				w.p(0);
				int s = Integer.parseInt(new String(T.d(v)));
				w.p(100);
				if(B > 0)
				{
					J.delete();
					T.d(c, J, w);
				}
				else if(B < 0)
					JOptionPane.showMessageDialog(null, "Your build number is " + B + ", but the server's build number is " + s + ", this should not happen usually.");
			}
			w.setVisible(false);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}
}
