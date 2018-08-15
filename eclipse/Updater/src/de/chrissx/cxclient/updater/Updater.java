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

public class Updater {
	public static final String VERSION_URL = "version url";
	public static final String CLIENT_URL = "client url";
	public static final String versionPath = Util.generateTempFile("cxclient_version", ".txt");
	
	//updater.jar [running_file] [jar]
	public static void main(String[] args) {
		try {
			UpdaterWindow window;
			if(args.length == 0) {
				window = new UpdaterWindow("Debug-Window", 500, 75, true);
				window.setVisible(true);
				for(int n = 5; n <= 100; n += 5) {
					Thread.sleep(200);
					window.setProgress(n);
				}
			}else {
				String jar_ = args[1];
				for(int i = 2; i < args.length; i++)
					jar_ += " " + args[i];
				File running = new File(args[0]);
				File jar = new File(jar_);
				while(running.exists())
					Thread.sleep(1);
				window = new UpdaterWindow("CXClient-Updater", 500, 75, true);
				window.setVisible(true);
				window.setWindowTitle("Finding current build number.");
				window.setProgress(0);
				ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
				window.setProgress(20);
				ZipEntry entry;
				while((entry = zip.getNextEntry()).getName() != "BLDNUM")
					zip.closeEntry();
				window.setProgress(50);
				byte[] b = new byte[(int) entry.getSize()];
				window.setProgress(60);
				zip.read(b);
				zip.closeEntry();
				zip.close();
				window.setProgress(80);
				int bldnum = Integer.parseInt(new String(b));
				window.setProgress(100);
				window.setWindowTitle("Getting server build number.");
				window.setProgress(0);
				int svrbld = Integer.parseInt(new String(Util.downloadBytes(VERSION_URL)));
				window.setProgress(100);
				if(bldnum > 0)
				{
					jar.delete();
					Util.downloadFile(CLIENT_URL, jar, window);
				}
				else if(bldnum < 0)
					JOptionPane.showMessageDialog(null, "Your build number is " + bldnum + ", but the server's build number is " + svrbld + ", this should not happen usually.");
			}
			window.setVisible(false);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
		}
	}
}
