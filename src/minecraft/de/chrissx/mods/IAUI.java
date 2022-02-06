package de.chrissx.mods;

import de.chrissx.util.Consts;

public class IAUI extends Mod {

	public IAUI() {
		super("#IAUI", "The in-app user interface (currently only the text in the upper-left corner)");
		setArgv0("iaui");
		enabled = true;
	}

	@Override
	public String getRenderstring() {
		return "\u00a7a\u00a7l[" + Consts.clientName + " " + Consts.version + "]";
	}
}
