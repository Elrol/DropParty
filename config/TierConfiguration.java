package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import com.github.elrol.dropparty.libs.TextLibs;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class TierConfiguration {

	private static TierConfiguration instance = new TierConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	private List<ItemType> tier1Items = new ArrayList<ItemType>();
	private List<ItemType> tier2Items = new ArrayList<ItemType>();
	private List<ItemType> tier3Items = new ArrayList<ItemType>();
	private List<ItemType> tier4Items = new ArrayList<ItemType>();
	private List<ItemType> tier5Items = new ArrayList<ItemType>();
	
	
	public static TierConfiguration getInstance() {
		return instance;
	}
	
	@SuppressWarnings("serial")
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> loader) {
		this.loader = loader;
		loadDefaults();
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
				try {
					config.getNode("Tier 1").setComment("A list of Items that are Uncommon").setValue(new TypeToken<List<ItemType>>() {}, tier1Items);
					config.getNode("Tier 2").setComment("A list of Items that are Rare").setValue(new TypeToken<List<ItemType>>() {}, tier2Items);
					config.getNode("Tier 3").setComment("A list of Items that are Epic").setValue(new TypeToken<List<ItemType>>() {}, tier3Items);
					config.getNode("Tier 4").setComment("A list of Items that are Legendary").setValue(new TypeToken<List<ItemType>>() {}, tier4Items);
					config.getNode("Tier 5").setComment("A list of Items that are Mythic").setValue(new TypeToken<List<ItemType>>() {}, tier5Items);
				} catch(ObjectMappingException e) {}
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
	
	private void loadDefaults() {
		tier1Items.add(ItemTypes.IRON_INGOT);
		tier2Items.add(ItemTypes.GOLD_INGOT);
		tier3Items.add(ItemTypes.DIAMOND);
		tier4Items.add(ItemTypes.EMERALD);
		tier5Items.add(ItemTypes.NETHER_STAR);
	}
	
	@SuppressWarnings("serial")
	public void addItem(CommandSource src, int tier, ItemType type) {
		loadConfig();
		try {
			List<ItemType> items = config.getNode("Tier " + tier).getValue(new TypeToken<List<ItemType>>() {});
			if(!isItemListed(type)) {
				config.getNode("Tier " + tier).setValue(items.add(type));
				saveConfig();
				src.sendMessage(TextLibs.pluginMessage("Successfully added " + type + " to Tier " + tier));
				return;
			}
			src.sendMessage(TextLibs.pluginError("Couldn't add " + type + " to Tier " + tier));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	public void removeItem(CommandSource src, int tier, ItemType type) {
		try {
			if(isItemListed(tier, type)) {
				removeItem(src, tier, type);
			}
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	public void removeItem(CommandSource src, ItemType type) throws ObjectMappingException {
		for(int i = 1; i < 5; i++) {
			List<ItemType> items = config.getNode("Tier " + i).getValue(new TypeToken<List<ItemType>>() {});
			if(items.contains(type))
				items.remove(type);
		}
	}
	
	public boolean isItemListed(ItemType type) throws ObjectMappingException {
		for(int i = 1; i < 5; i++) {
			if(isItemListed(i, type))
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	public boolean isItemListed(int tier, ItemType type) throws ObjectMappingException {
		List<ItemType> items = config.getNode("Tier " + tier).getValue(new TypeToken<List<ItemType>>() {});
		if(items.contains(type)) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	public List<ItemType> getTier(int tier){
		try {
			return config.getNode("Tier " + tier).getValue(new TypeToken<List<ItemType>>() {});
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
