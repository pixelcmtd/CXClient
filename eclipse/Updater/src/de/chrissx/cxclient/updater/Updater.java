package de.chrissx.cxclient.updater;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

public class Updater {
	public static final String VERSION_URL = "https://chrissx.lima-city.de/cxclient/version.dl";
	public static final String CLIENT_URL = "https://chrissx.lima-city.de/cxclient/client.dl";
	public static final String tempPath = System.getProperty("java.io.tmpdir");
	public static final String versionPath = Util.generateTempFile(tempPath, "cxclient_version", ".dl");
	
	//old format:
	//current_build_number current_jar (does not work because getting the jar is...broken)
	//downloads version_url and parses int as server_build_number
	//if server_build_number > current_build_number
	//download client_url to current_jar
	
	//new format: current_build_number .minecraft_dir running_file
	
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
				int currentBuild = Integer.parseInt(args[0]);
				String outFile = args[1];
				for(int i = 2; i < args.length; i++)
					outFile += " " + args[i];
				Thread.sleep(500);
				UpdaterWindow window = new UpdaterWindow("CXClient-Updater", 500, 75, true);
				window.setVisible(true);
				window.setWindowTitle("Retreiving newest version number");
				Files.copy(new URL(VERSION_URL).openStream(), Paths.get(versionPath), StandardCopyOption.REPLACE_EXISTING);
				window.setProgress(50);
				int serverBuild = Integer.parseInt(Files.readAllLines(Paths.get(versionPath)).get(0));
				window.setProgress(100);
				if(!(serverBuild > currentBuild)) {
					window.setVisible(false);
					System.exit(0);
				}
				window.setProgress(0);
				window.setWindowTitle("Deleting local jar");
				if(new File(outFile).exists())
					Files.delete(Paths.get(outFile));
				window.setProgress(100);
				window.setWindowTitle("Downloading new jar");
				window.setProgress(0);
				Util.downloadFile(CLIENT_URL, outFile, window);
				window.setProgress(100);
				window.setWindowTitle("Finishing up.");
				for(int i = 0; i <= 100; i++) {
					Thread.sleep(5);
					window.setProgress(i);
				}
				window.setVisible(false);
				System.exit(0);
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString());
			System.exit(0);
		}
	}
}
