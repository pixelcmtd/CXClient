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
	public static final String VERSION_URL = "https://chrissx.lima-city.de/cxclient/version.dl";
	public static final String CLIENT_URL = "https://chrissx.lima-city.de/cxclient/client.dl";
	public static final String tempPath = System.getProperty("java.io.tmpdir");
	public static final String versionPath = Util.generateTempFile(tempPath, "cxclient_version", ".dl");
	
	//updater.jar [running_file] [jar]
	public static void main(String[] args) {
		try {
			if(args.length == 0) {
				UpdaterWindow window = new UpdaterWindow("Debug-Window", 500, 75, true);
				window.setVisible(true);
				for(int n = 5; n <= 100; n+=5) {
					Thread.sleep(200);
					window.setProgress(n);
				}
				window.setVisible(false);
				System.exit(0);
			}else {
				String jar_ = args[1];
				for(int i = 2; i < args.length; i++)
					jar_ += " " + args[i];
				File running = new File(args[0]);
				File jar = new File(jar_);
				while(running.exists())
					Thread.sleep(1);
				UpdaterWindow window = new UpdaterWindow("CXClient-Updater", 500, 75, true);
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
				
				if(bldnum > 0)
				{
					jar.delete();
				}
				else if(bldnum < 0)
				{
					
				}
				System.exit(0);
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.exit(0);
		}
	}
}
