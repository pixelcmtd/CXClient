package de.chrissx.alts.mcleaks;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class McLeaksApi {

	static Gson gson = new Gson();

	/**
	 * Redeems an MCLeaks token.
	 * @param token The McLeaks-Token
	 * @return The current McLeaks-Session
	 * @throws Exception 
	 */
	public static McLeaksSession redeemMcleaksToken(String token) throws Exception {
		URLConnection con = preparePostRequest("https://auth.mcleaks.net/v1/redeem", "{\"token\":\"" + token + "\"}");
		if(con == null)
			throw new ConnectException("The connection is null, please check your internet connection!");
		JsonObject json = getResult(con);
		if(json == null) throw new Exception("Server didn't return the session.");
		if(!json.has("mcname"))
			throw new CorruptedResultException("The result doesn't contain the name.");
		if(!json.has("session"))
			throw new CorruptedResultException("The result doesn't contain the session.");
		return new McLeaksSession(json.get("session").getAsString(), json.get("mcname").getAsString());
	}

	/**
	 * 
	 * @param session The current session
	 * @param mcName The player-name
	 * @param serverHash The mc-server-hash
	 * @param server The server to connect to
	 * @throws Exception 
	 */
	public static void joinServer(McLeaksSession mclssession, String serverHash, String server) throws Exception {
		URLConnection con = preparePostRequest("https://auth.mcleaks.net/v1/joinserver", "{\"session\":\"" + mclssession.getSession() + "\",\"mcname\":\"" + mclssession.getMcname() + "\",\"serverhash\":\"" + serverHash + "\",\"server\":\"" + server + "\"}");
		if(con == null)
			throw new ConnectException("The connection couldn't be established, please check your internet connection!");
		getResult(con);
	}

	static URLConnection preparePostRequest(final String url, final String body) {
        try {
            HttpURLConnection con = (HttpsURLConnection)new URL(url).openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            System.out.println("[MCLeaksAPI] Sending POST-request with body \"" + body + "\" to URL \"" + url + "\"");
            final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();
            return con;
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }

	static JsonObject getResult(final URLConnection urlConnection) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            result.append(line);
        reader.close();
		final JsonElement jsonElement = (JsonElement)gson.fromJson(result.toString(), JsonElement.class);
        System.out.println("[MCLeaksAPI] Got result: \"" + result + "\"");
        if (!jsonElement.isJsonObject() || 
        	!jsonElement.getAsJsonObject().has("success") ||
        	!jsonElement.getAsJsonObject().get("success").getAsBoolean() ||
        	!jsonElement.getAsJsonObject().has("result"))
            	throw new Exception("Getting the result didn't succeed.");
        return jsonElement.getAsJsonObject().get("result").isJsonObject() ? jsonElement.getAsJsonObject().get("result").getAsJsonObject() : null;
    }
}
