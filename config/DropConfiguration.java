package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import com.github.elrol.dropparty.libs.TextLibs;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class DropConfiguration {

	private static DropConfiguration instance = new DropConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	
	public static DropConfiguration getInstance() {
		return instance;
	}
	
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> loader) {
		this.loader = loader;
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
				saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			loadConfig();
		}
	}
	
	public CommentedConfigurationNode getConfig() {
		return config;
	}
	

	public void saveConfig() {
		try {
			loader.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		try {
			config = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createDropList(CommandSource src, String name, String playername) {
		if(!doesDropListExist(name)) {
			loadConfig();
			config.getNode("DropLists", name, "creator").setValue(playername);
			saveConfig();
			TextLibs.sendMessage(src, "Created the '" + name + "' Drop List");
		} else {
			TextLibs.sendError(src, "The DropList '" + name +"' already exists");
		}
	}
	
	public void removeDropList(CommandSource src, String name) {
		if(doesDropListExist(name)) {
			loadConfig();
			config.getNode("DropLists", name).setValue(null);
			saveConfig();
			TextLibs.sendMessage(src, "The DropList '" + name + "' was removed");
		} else {
			TextLibs.sendError(src, "The DropList '" + name + "' already exists");
		}
	}
	
	public boolean doesDropListExist(String name) {
		loadConfig();
		for(Object node : config.getChildrenMap().keySet()) {
			if(node.equals(name));
				return true;
		}
		return false;
	}
	
	public void addDropListItem(CommandSource src, String name, ItemStack type) {
		if(doesDropListExist(name)) {
			addItem(name, type);
			TextLibs.sendMessage(src, "'" + type.getTranslation().get() + "' was added to the DropList '" + name + "'");
		} else {
			TextLibs.sendError(src, "The DropList '" + name +"' doesn't exist");
		}
	}
	
	public void removeDropListItem(CommandSource src, String name, ItemStack type) {
		if(doesDropListExist(name)) {
			if(doesItemExist(name, type)) {
				removeItem(name, type);
				TextLibs.sendMessage(src, "'" + type.getTranslation().get() + "' was removed from the DropList '" + name + "'");				
			} else {
				TextLibs.sendError(src, "'" + type.getTranslation().get() + "' is not int the DropList '" + name + "'");
			}
		} else {
			TextLibs.sendError(src, "The DropList '" + name + "' doesn't exist");
		}
	}
	
	public boolean doesItemExist(String name, ItemStack type) {
		List<ItemStack> items = getList(name);
		if(items != null && !items.isEmpty()) {
			for(ItemStack stack : items) {
				if(stack.getType().equals(type.getType()) && ItemStackComparators.ITEM_DATA.compare(type, stack) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	public List<ItemStack> getList(String name){
		List<ItemStack> items = new ArrayList<ItemStack>();
		if(doesDropListExist(name)) {
			try {
				loadConfig();
				List<ItemStack> temp = config.getNode("DropLists", name, "Items").getValue(new TypeToken<List<ItemStack>> () {});
				if(temp != null)
					items = temp;
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}
		}
		return items; 
	}
	
	public Map<ItemStack, Integer> getMap(String name){
		Map<ItemStack, Integer> map = new HashMap<ItemStack, Integer>();
		List<ItemStack> list = getList(name);
		if(list != null && !list.isEmpty()) {
			for(ItemStack stack : list) {
				ItemStack single = stack.copy();
				single.setQuantity(1);
				if(map.containsKey(single)) {
					int qty = map.get(single);
					qty += stack.getQuantity();
					map.put(single, qty);
				} else {
					map.put(single, stack.getQuantity());
				}
			}
		}
		return map;
	}
	
	@SuppressWarnings("serial")
	public void addItem(String name, ItemStack type) {
		loadConfig();
		try {
			List<ItemStack> items = getList(name);
			items.add(type);
			config.getNode("DropLists", name, "Items").setValue(new TypeToken<List<ItemStack>>() {}, items);
			saveConfig();
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	public void removeItem(String name, ItemStack item) {
		loadConfig();
		List<ItemStack> items = getList(name);
		for(ItemStack stack : new ArrayList<ItemStack>(items)) {
			if(stack.getType().equals(item.getType()) && ItemStackComparators.ITEM_DATA.compare(item, stack) == 0) {
				try {
					items.remove(stack);
					config.getNode("DropLists", name, "Items").setValue(new TypeToken<List<ItemStack>>() {}, items);
					saveConfig();
				} catch(ObjectMappingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	public void removeAll(String name) {
		try {
			List<ItemStack> items = getList(name);
			items.clear();
			loadConfig();
			config.getNode("DropLists", name, "Items").setValue(new TypeToken<List<ItemStack>>() {}, items);
			saveConfig();
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getDrops() {
		loadConfig();
		List<String> list = new ArrayList<String>();
		for(Object node : config.getNode("DropLists").getChildrenMap().keySet()) {
			list.add((String)node);
		}
		return list;
	}
}
