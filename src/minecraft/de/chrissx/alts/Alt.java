package de.chrissx.alts;

public class Alt {

	private String email;
	private String pass;
	
	public Alt(String email, String pass) {
		this.email = email;
		this.pass = pass;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return pass;
	}

	public void setPassword(String pass) {
		this.pass = pass;
	}

	public boolean isCracked() {
		return pass == null || pass.length() == 0;
	}
}