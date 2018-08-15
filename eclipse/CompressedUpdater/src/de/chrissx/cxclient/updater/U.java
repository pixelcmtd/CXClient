package de.chrissx.cxclient.updater;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

public class U extends JFrame {

	JProgressBar p;
	final String b;
	
	public U(String t, int w, int h, boolean a) {
		setTitle(t);
		b = t;
		setSize(w, h);
		setAlwaysOnTop(a);
		setLocation(700, 400);
		p = new JProgressBar();
		p.setValue(0);
		p.setVisible(true);
		p.setLocation(10, 10);
		add(p);
	}
	
	public void p(int n) {
		p.setValue(n);
	}
	
	public void t(String t) {
		setTitle(b + " - " + t);
	}
}
