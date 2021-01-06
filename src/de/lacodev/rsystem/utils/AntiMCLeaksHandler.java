package de.lacodev.rsystem.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AntiMCLeaksHandler {
	
	// Handler for anti-mcleaks.lacodev.de Services
	
	public AntiMCLeaksHandler() {
		super();
	}
	
	private ArrayList<String> cache = new ArrayList<>();
	
	public void cacheAccounts() {
		
		cache.clear();
		
		int totalAccounts = Integer.valueOf(readJsonFromUrl("https://anti-mcleaks.lacodev.de/api/v1/total/").get("total").toString());
		
		JSONObject allAccountsData = readJsonFromUrl("https://anti-mcleaks.lacodev.de/api/v1/accounts/");
		
		for(int i = 1; i <= totalAccounts; i++) {
			JSONObject data = (JSONObject) allAccountsData.get(String.valueOf(i));
			
			cache.add(data.get("uuid").toString());
		}
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §aSuccessfully §8cached §a" + totalAccounts + " Accounts");
		Bukkit.getConsoleSender().sendMessage("");
		
	}
	
	public boolean isAccountCached(String uuid) {
		return cache.contains(uuid);
	}
	
	public ArrayList<String> getAccountCache() {
		return cache;
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
