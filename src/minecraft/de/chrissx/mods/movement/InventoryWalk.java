package de.chrissx.mods.movement;

import org.lwjgl.input.Keyboard;

import de.chrissx.mods.Mod;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class InventoryWalk extends Mod {

	public InventoryWalk() {
		super("InventoryWalk");
	}

	//The inspiration for this is from the XIV client:
	//https://gitlab.com/Apteryx/XIV/blob/ae6f113fe29a0a62e6f9a9e0afc3720ef68503a5/src/main/java/pw/latematt/xiv/mod/mods/InventoryWalk.java
	@Override
	public void onTick()
	{
		if (!enabled || currentScreen() == null || currentScreen() instanceof GuiChat) return;

        KeyBinding[] movebinds = {
        		settings().keyBindForward,
        		settings().keyBindBack,
        		settings().keyBindLeft,
        		settings().keyBindRight,
        		settings().keyBindJump
        };

        for (KeyBinding kb : movebinds)
            KeyBinding.setKeyBindState(kb.getKeyCode(), Keyboard.isKeyDown(kb.getKeyCode()));

        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            player().rotationPitch -= 2;

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            player().rotationPitch += 2;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            player().rotationYaw -= 2;

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            player().rotationYaw += 2;
	}

}
