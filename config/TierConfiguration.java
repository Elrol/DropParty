package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class TierConfiguration {

	private static TierConfiguration instance = new TierConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	public List<ItemType> tier1Items = new ArrayList<ItemType>();
	public List<ItemType> tier2Items = new ArrayList<ItemType>();
	public List<ItemType> tier3Items = new ArrayList<ItemType>();
	public List<ItemType> tier4Items = new ArrayList<ItemType>();
	public List<ItemType> tier5Items = new ArrayList<ItemType>();
	
	
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
	
}