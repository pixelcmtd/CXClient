package de.chrissx.cxclient.updater;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

public class UpdaterWindow extends JFrame {

	JProgressBar progress;
	final String beginOfTitle;
	
	public UpdaterWindow(String title, int width, int height, boolean alwaysOnTop) {
		setTitle(title);
		beginOfTitle = title;
		setSize(width, height);
		setAlwaysOnTop(alwaysOnTop);
		setLocation(700, 400);
		progress = new JProgressBar();
		progress.setValue(0);
		progress.setVisible(true);
		progress.setLocation(10, 10);
		add(progress);
	}
	
	public void setProgress(int n) {
		progress.setValue(n);
	}
	
	public void setWindowTitle(String title) {
		setTitle(beginOfTitle + " - " + title);
	}
}
