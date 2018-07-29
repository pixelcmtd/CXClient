package de.chrissx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.chrissx.mods.Bindable;

public class HotkeySaving {

	private static final HackedClient hc = HackedClient.getClient();
	
	public static void saveHotkeys(Path file, Map<Integer, Bindable> hotkeys) throws IOException {
		if(file.toFile().exists())
			Files.delete(file);
		BufferedWriter w = Files.newBufferedWriter(file);
		w.write(encodeHotkeys(hotkeys));
		w.close();
	}
	
	private static String encodeHotkeys(Map<Integer, Bindable> hotkeys) {
		StringBuilder sb = new StringBuilder();
		for(Entry<Integer, Bindable> e : hotkeys.entrySet()) {
			if(sb.toString() != "")
				sb.append("$");
			sb.append(hotkeyToBase64(e));
		}
		return Base64.getEncoder().encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
	}
	
	private static String bindableToBase64(Bindable b) {
		return Base64.getEncoder().encodeToString(b.getName().getBytes(StandardCharsets.UTF_8));
	}
	
	private static String keyToBase64(int key) {
		return Base64.getEncoder().encodeToString(ByteBuffer.allocate(4).putInt(key).array());
	}
	
	private static String hotkeyToBase64(Entry<Integer, Bindable> hotkey) {
		return Base64.getEncoder().encodeToString((keyToBase64(hotkey.getKey())+"$"+bindableToBase64(hotkey.getValue())).getBytes(StandardCharsets.UTF_8));
	}
	
	public static Map<Integer, Bindable> loadHotkeys(Path file) throws IOException {
		BufferedReader r = Files.newBufferedReader(file);
		String base64 = r.readLine();
		r.close();
		if(base64 == null)
			return new HashMap<Integer, Bindable>();
		return decodeHotkeys(base64);
	}
	
	private static Map<Integer, Bindable> decodeHotkeys(String base64) {
		String[] strs = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8).split("$");
		HashMap<Integer, Bindable> hotkeys = new HashMap<Integer, Bindable>();
		for(String s : strs) {
			if(s == "")
				continue;
			Object[] os = base64ToHotkey(s);
			hotkeys.put((Integer) os[0], (Bindable) os[1]);
		}
		return hotkeys;
	}
	
	private static Bindable base64ToBindable(String base64) {
		return hc.getMods().getBindable(new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8));
	}
	
	private static int base64ToKey(String base64) {
		return ByteBuffer.wrap(Base64.getDecoder().decode(base64)).getInt();
	}
	
	private static Object[] base64ToHotkey(String base64) {
		String[] strs = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8).split("$");
		return new Object[] {base64ToKey(strs[0]), base64ToBindable(strs[1])};
	}
}
