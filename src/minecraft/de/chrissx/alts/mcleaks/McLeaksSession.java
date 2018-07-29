package de.chrissx.alts.mcleaks;

public class McLeaksSession {
	String session = "null";
	String mcname = "null";
	
	public McLeaksSession() {}
	
	public McLeaksSession(String session, String mcname) {
		this.session = session;
		this.mcname = mcname;
	}
	
	public String getSession() {
		return session;
	}
	
	public void setSession(String session) {
		this.session = session;
	}
	
	public String getMcname() {
		return mcname;
	}
	
	public void setMcname(String mcname) {
		this.mcname = mcname;
	}
	
	@Override
	public String toString() {
		return session;
	}
}
