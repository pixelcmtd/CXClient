package de.chrissx.cxclient.updater;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

public class UpdaterWindow extends JFrame {

	private JProgressBar progressBar;
	private String generalTitle;
	
	public UpdaterWindow(String title, int width, int height, boolean alwaysOnTop) {
		this.setTitle(title);
		generalTitle = title;
		this.setSize(width, height);
		this.setAlwaysOnTop(alwaysOnTop);
		this.setLocation(700, 400);
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setVisible(true);
		progressBar.setLocation(10, 10);
		this.add(progressBar);
	}
	
	public void setProgress(int n) {
		progressBar.setValue(n);
	}
	
	public void setWindowTitle(String title) {
		this.setTitle(generalTitle+" - "+title);
	}
}
