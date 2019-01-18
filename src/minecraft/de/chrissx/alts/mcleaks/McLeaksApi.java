package de.chrissx.alts.mcleaks;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class McLeaksApi {

	static Gson gson = new Gson();
	
	/**
	 * Redeems an MCLeaks token.
	 * @param token The McLeaks-Token
	 * @return The current McLeaks-Session
	 * @throws ConnectionNullException
	 * @throws ResultGettingException
	 * @throws CorruptedResultException
	 * @throws IOException 
	 */
	public static McLeaksSession redeemMcleaksToken(String token) throws ConnectException, ResultGettingException, CorruptedResultException, IOException {
		URLConnection con = preparePostRequest("http://auth.mcleaks.net/v1/redeem", "{\"token\":\"" + token + "\"}");
		if(con == null)
			throw new ConnectException("The connection is null, please check your internet connection!");
		Object o = getResult(con);
		if(o instanceof String)
			throw new ResultGettingException((String)o);
		JsonObject json = (JsonObject)o;
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
	 * @throws ConnectionNullException
	 * @throws ResultGettingException
	 * @throws IOException
	 */
	public static void joinServer(McLeaksSession mclssession, String serverHash, String server) throws ConnectException, ResultGettingException, IOException {
		URLConnection con = preparePostRequest("http://auth.mcleaks.net/v1/joinserver", "{\"session\":\"" + mclssession.getSession() + "\",\"mcname\":\"" + mclssession.getMcname() + "\",\"serverhash\":\"" + serverHash + "\",\"server\":\"" + server + "\"}");
		if(con == null)
			throw new ConnectException("The connection is null, please check your internet connection!");
		Object o = getResult(con);
		if(o instanceof String)
			throw new ResultGettingException((String)o);
	}
	
	static URLConnection preparePostRequest(final String url, final String body) {
        try {
            HttpURLConnection con;
            if (url.toLowerCase().startsWith("https://"))
                con = (HttpsURLConnection)new URL(url).openConnection();
            else
                con = (HttpURLConnection)new URL(url).openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            return con;
        }
        catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	static Object getResult(final URLConnection urlConnection) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
		final JsonElement jsonElement = (JsonElement)gson.fromJson(result.toString(), JsonElement.class);
        System.out.println(result.toString());
        if (!jsonElement.isJsonObject())
            return "The json element isn't a json object.";
        if(!jsonElement.getAsJsonObject().has("success"))
        	return "The json element doesn't have the success tag.";
        if (!jsonElement.getAsJsonObject().get("success").getAsBoolean())
            return jsonElement.getAsJsonObject().has("errorMessage") ? jsonElement.getAsJsonObject().get("errorMessage").getAsString() : "The operation didn't succeed but the error message isn't defined.";
        if (!jsonElement.getAsJsonObject().has("result"))
            return "Json doesn't contain the result.";
        return jsonElement.getAsJsonObject().get("result").isJsonObject() ? jsonElement.getAsJsonObject().get("result").getAsJsonObject() : null;
    }
}
