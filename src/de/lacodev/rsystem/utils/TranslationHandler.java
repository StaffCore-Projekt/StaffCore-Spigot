package de.lacodev.rsystem.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GREEN + "Successfully " + ChatColor.DARK_GRAY + "cached " + ChatColor.GREEN + fallback.size() + " Fallback-Translations" + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "US" + ChatColor.DARK_GRAY + ")");
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
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System " + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GREEN + "Successfully " + ChatColor.DARK_GRAY + "cached " + ChatColor.GREEN + language.size() + " Translations" + ChatColor.DARK_GRAY + "(" +ChatColor.GRAY + lang.toUpperCase() + ChatColor.DARK_GRAY + ")");
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
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "System" + ChatColor.DARK_GRAY + "\u00BB " + ChatColor.GREEN + "Successfully " + ChatColor.DARK_GRAY + "cached " + ChatColor.GREEN + custom.size() + " Messages" + ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + "CUSTOM" + ChatColor.DARK_GRAY + ")");
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
