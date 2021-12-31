package de.chrissx.mods.movement;

import java.io.File;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Util;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

public class Sneak extends Mod {

	Option<SneakMode> mode = new Option<SneakMode>("mode", "Whether the sneaking is simulated or real",
			SneakMode.PACKET) {
		@Override
		public void set(String value) {
			this.value = value == "" ? (this.value == SneakMode.PACKET ? SneakMode.BYPASS : SneakMode.PACKET)
					: SneakMode.valueOf(value.toUpperCase());
		}
	};
	File mf;

	public Sneak() {
		super("Sneak", "sneak", "Makes you sneak continuously");
		addOption(mode);
		mf = getApiFile("mode");
	}

	@Override
	public void onTick() {
		if (mode.value.equals(SneakMode.BYPASS))
			settings().keyBindSneak.pressed = true;
		else if (!mode.value.equals(SneakMode.PACKET))
			Util.sendMessage(
					"Currently your mode is not supported, this should be a bug, please report this on the bug tracker!");
	}

	@Override
	public void toggle() {
		enabled = !enabled;
		if (mode.value.equals(SneakMode.PACKET))
			sendPacket(new C0BPacketEntityAction(player(), enabled ? Action.START_SNEAKING : Action.STOP_SNEAKING));
	}

	@Override
	public String getRenderstring() {
		return name + "(" + mode + ")";
	}

	@Override
	public void apiUpdate() {
		write(mf, mode.value.b);
	}
}
