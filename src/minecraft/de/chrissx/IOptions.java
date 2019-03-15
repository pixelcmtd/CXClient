package de.chrissx;

import java.io.File;

public interface IOptions {
	public void init(File config);
	public void set(String[] args);
	public void get(String[] args);
	public void list(String[] args);
	public void stop(File config);
}
