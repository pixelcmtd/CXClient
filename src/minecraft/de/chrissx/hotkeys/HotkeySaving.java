package de.chrissx.hotkeys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;

import de.chrissx.HackedClient;
import de.chrissx.mods.Bindable;

public class HotkeySaving {

	static HackedClient hc;
	static Decoder d;
	static Encoder e;

	public static void init(HackedClient c)
	{
		hc = c;
		d = Base64.getDecoder();
		e = Base64.getEncoder();
	}
	
	public static void saveHotkeys(Path file, List<Hotkey> hotkeys) throws IOException {
		if(file.toFile().exists())
			Files.delete(file);
		BufferedWriter w = Files.newBufferedWriter(file);
		w.write(encodeHotkeys(hotkeys));
		w.close();
	}
	
	static String encodeHotkeys(List<Hotkey> hotkeys) {
		StringBuilder sb = new StringBuilder();
		boolean b = false;
		for(Hotkey hk : hotkeys) {
			if(b)
				sb.append("$");
			else
				b = true;
			sb.append(hotkeyToBase64(hk));
		}
		return e.encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
	}

	static String bindableToBase64(Bindable b) {
		return e.encodeToString(b.getName().getBytes(StandardCharsets.UTF_8));
	}

	static String keyToBase64(int key) {
		return e.encodeToString(ByteBuffer.allocate(4).putInt(key).array());
	}

	static String hotkeyToBase64(Hotkey hk) {
		return e.encodeToString((keyToBase64(hk.key) + "$" + bindableToBase64(hk.handler)).getBytes(StandardCharsets.UTF_8));
	}

	public static List<Hotkey> loadHotkeys(Path file) throws IOException {
		BufferedReader r = Files.newBufferedReader(file);
		String base64 = r.readLine();
		r.close();
		if(base64 == null || base64 == "")
			return new ArrayList<Hotkey>();
		else
			return decodeHotkeys(base64);
	}

	static List<Hotkey> decodeHotkeys(String base64) {
		String[] strs = new String(d.decode(base64), StandardCharsets.UTF_8).split("$");
		List<Hotkey> hotkeys = new ArrayList<Hotkey>();
		for(String s : strs) {
			if(s == "")
				continue;
			hotkeys.add(base64ToHotkey(s));
		}
		return hotkeys;
	}

	static Bindable base64ToBindable(String base64) {
		return hc.getMods().getBindable(new String(d.decode(base64), StandardCharsets.UTF_8));
	}

	static int base64ToKey(String base64) {
		return ByteBuffer.wrap(d.decode(base64)).getInt();
	}

	static Hotkey base64ToHotkey(String base64) {
		String[] strs = new String(d.decode(base64), StandardCharsets.UTF_8).split("$");
		return new Hotkey(base64ToKey(strs[0]), base64ToBindable(strs[1]));
	}
}
