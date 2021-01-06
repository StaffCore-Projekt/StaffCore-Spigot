package de.lacodev.rsystem.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.lacodev.rsystem.enums.Heads;
import de.lacodev.rsystem.enums.XMaterial;

public class Data {
	
	public static ItemStack getHead(String name) {
		for(Heads head : Heads.values()) {
			if(head.getName().equals(name)) {
				return head.getItemStack();
			}
		}
		return null;
	}
	
	public static ItemStack buildHead(String head, String name) {
		
		ItemStack stack = getHead(head.toLowerCase());
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
		
	}
	
	public static ItemStack buildHead(String head, String name, String lore1, String lore2) {
		
		ItemStack stack = getHead(head);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add("");
		meta.setLore(lore);
		stack.setItemMeta(meta);
		
		return stack;
		
	}
	
	public static ItemStack createSkull(String url, String name) {
		ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
		if(url.isEmpty()) return head;
		
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		
		profile.getProperties().put("textures", new Property("textures", url));
		
		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch(IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}
		head.setItemMeta(headMeta);
		
		return head;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildItemStack(Material mat, int size, int sh, String name, String lore1, String lore2) {
		ItemStack stack = new ItemStack(mat, size, (short)sh);
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack buildItemStack(XMaterial mat, String name, String lore1, String lore2) {
		ItemStack stack = mat.parseItem();
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildPlayerHead(String name, String skullowner) {
		ItemStack stack = XMaterial.PLAYER_HEAD.parseItem();
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(skullowner);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildPlayerHead(String name, String skullowner, String lore1) {
		ItemStack stack = XMaterial.PLAYER_HEAD.parseItem();
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setOwner(skullowner);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildItemStackLore(Material mat, int size, int sh, String name, String lore1, String lore2, String lore3) {
		ItemStack stack = new ItemStack(mat, size, (short)sh);
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add(lore3);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack buildItemStackLore(XMaterial mat, String name, String lore1, String lore2, String lore3) {
		ItemStack stack = mat.parseItem();
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add(lore3);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack buildPlace() {
		ItemStack stack = XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§8...");
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildItem(Material mat, int size, int sh, String name) {
		ItemStack stack = new ItemStack(mat, size, (short)sh);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack buildItem(XMaterial mat, String name) {
		ItemStack stack = mat.parseItem();
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}

}
