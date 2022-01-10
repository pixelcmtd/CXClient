package de.chrissx.hotkeys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;

import de.chrissx.HackedClient;
import de.chrissx.mods.Bindable;

public class HotkeySaving {

	static HackedClient hc;
	static Decoder d;
	static Encoder e;

	public static void init(HackedClient c) {
		hc = c;
		d = Base64.getDecoder();
		e = Base64.getEncoder();
	}

	public static void saveHotkeys(File file, List<Hotkey> hotkeys) throws IOException {
		if (file.exists())
			file.delete();
		Files.write(file.toPath(), encodeHotkeys(hotkeys));
	}

	static byte[] encodeHotkeys(List<Hotkey> hotkeys) {
		StringBuilder sb = new StringBuilder();
		boolean b = false;
		for (Hotkey hk : hotkeys) {
			if (b)
				sb.append("$");
			else
				b = true;
			sb.append(hotkeyToBase64(hk));
		}
		return sb.toString().getBytes(StandardCharsets.UTF_8);
	}

	static String bindableToString(Bindable b) {
		return b.getName();
	}

	static byte[] keyToBinary(int key) {
		return new byte[] { (byte) (key >> 24), (byte) (key >> 16), (byte) (key >> 8), (byte) key };
	}

	static String hotkeyToBase64(Hotkey hk) {
		byte[] key = keyToBinary(hk.key);
		byte[] handler = bindableToString(hk.handler).getBytes(StandardCharsets.UTF_8);
		byte[] b = new byte[4 + handler.length];
		b[0] = key[0];
		b[1] = key[1];
		b[2] = key[2];
		b[3] = key[3];
		for (int i = 0; i < handler.length; i++)
			b[i + 4] = handler[i];
		return e.encodeToString(b);
	}

	public static List<Hotkey> loadHotkeys(Path file) throws IOException {
		BufferedReader r = Files.newBufferedReader(file);
		String base64 = r.readLine();
		r.close();
		return base64 == null || base64 == "" ? new ArrayList<Hotkey>() : decodeHotkeys(base64);
	}

	static List<Hotkey> decodeHotkeys(String base64) {
		String[] strs = new String(d.decode(base64), StandardCharsets.UTF_8).split("$");
		List<Hotkey> hotkeys = new ArrayList<Hotkey>();
		for (String s : strs) {
			if (s == "")
				continue;
			hotkeys.add(base64ToHotkey(s));
		}
		return hotkeys;
	}

	static Bindable stringToBindable(String s) {
		return hc.getMods().getBindable(s);
	}

	static int binaryToKey(byte b, byte c, byte d, byte e) {
		return (b << 24) | (c << 16) | (d << 8) | e;
	}

	static Hotkey base64ToHotkey(String base64) {
		byte[] b = d.decode(base64);
		return new Hotkey(binaryToKey(b[0], b[1], b[2], b[3]),
		                  stringToBindable(new String(b, 4, b.length - 4, StandardCharsets.UTF_8)));
	}
}
