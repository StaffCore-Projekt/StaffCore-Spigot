package de.lacodev.rsystem.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TranslationHandler {
	
	// Handler for translate.lacodev.de Services
	
	private HashMap<String, String> fallback = new HashMap<>();
	private HashMap<String, String> language = new HashMap<>();
	private HashMap<String, String> custom = new HashMap<>();
	private ArrayList<String> keys = new ArrayList<>();
	
	public TranslationHandler() {
		super();
	}
	
	public void init() {
		
		fallback.clear();
		
		JSONObject message_keys = readJsonFromUrl("https://translate.lacodev.de/api/v1/keys");
		int totalKeys = message_keys.size();
		
		for(int i = 1; i <= totalKeys; i++) {
			
			JSONObject data = (JSONObject) message_keys.get(String.valueOf(i));
			
			keys.add(data.get("key").toString());
		}
		
		JSONObject translation = readJsonFromUrl("https://translate.lacodev.de/api/v1/all/lang/us");
		
		for(String key : keys) {
			
			try {
				
				JSONObject msg = (JSONObject) translation.get(key);
				
				fallback.put(key, msg.get("translation").toString());
				
			} catch (NullPointerException e) {
				
			}
		}
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §aSuccessfully §8cached §a" + fallback.size() + " Fallback-Translations§8(§7US§8)");
		Bukkit.getConsoleSender().sendMessage("");
		
	}
	
	public void fetch(String lang) {
		language.clear();
		
		JSONObject translation = readJsonFromUrl("https://translate.lacodev.de/api/v1/all/lang/"+ lang);
		
		for(String key : keys) {
			
			try {
				JSONObject msg = (JSONObject) translation.get(key);
				
				language.put(key, msg.get("translation").toString());
			} catch(NullPointerException e) {
				
			}
		}
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §aSuccessfully §8cached §a" + language.size() + " Translations§8(§7"+ lang.toUpperCase() +"§8)");
		Bukkit.getConsoleSender().sendMessage("");
		
	}
	
	public void fetchCustom(String restapikey) {
		custom.clear();
		
		JSONObject translation = readJsonFromUrl("https://translate.lacodev.de/api/v1/restricted/key/"+ restapikey);
		
		for(String key : keys) {
			
			try {
				JSONObject msg = (JSONObject) translation.get(key);
				
				custom.put(key, msg.get("text_message").toString());
			} catch(NullPointerException e) {
				
			}
		}
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §aSuccessfully §8cached §a" + custom.size() + " Messages§8(§7CUSTOM§8)");
		Bukkit.getConsoleSender().sendMessage("");
		
	}
	
	public String getTranslation(String key) {
		
		if(custom.containsKey(key)) {
			return custom.get(key);
		} else if(!language.containsKey(key)) {
			return fallback.get(key);
		} else {
			return language.get(key);
		}
		
	}
	
	private JSONObject readJsonFromUrl(String url) {
		JSONParser parser = new JSONParser();
		
		try {
			
			JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(new URL(url).openStream(), Charset.forName("UTF-8")));
			
			return jsonObject;
		} catch (IOException | ParseException | ClassCastException e) {
			
		}
		return null;
	}

}
