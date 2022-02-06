package de.chrissx.mods.movement;

import de.chrissx.mods.Mod;
import de.chrissx.mods.options.EnumOption;
import de.chrissx.mods.options.Option;
import de.chrissx.util.Util;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

public class Sneak extends Mod {

	Option<SneakMode> mode = new EnumOption<SneakMode>(SneakMode.class, "mode", "Whether the sneaking is simulated or real", new SneakMode[] {SneakMode.PACKET, SneakMode.BYPASS});

	public Sneak() {
		super("Sneak", "Makes you sneak continuously");
		addOption(mode);
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
}
