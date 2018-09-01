package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class DefaultConfiguration {

	private static DefaultConfiguration instance = new DefaultConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	
	public static DefaultConfiguration getInstance() {
		return instance;
	}
	
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> loader) {
		this.loader = loader;
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
				config.getNode("DropParty", "Tier Drops", "Tier 0").setValue(42).setComment("Drop chance of Common items");
				config.getNode("DropParty", "Tier Drops", "Tier 1").setValue(25).setComment("Drop chance of Uncommon items");
				config.getNode("DropParty", "Tier Drops", "Tier 2").setValue(17).setComment("Drop chance of rare items");
				config.getNode("DropParty", "Tier Drops", "Tier 3").setValue(10).setComment("Drop chance of epic items");
				config.getNode("DropParty", "Tier Drops", "Tier 4").setValue(5).setComment("Drop chance of legendary items");
				config.getNode("DropParty", "Tier Drops", "Tier 5").setValue(1).setComment("Drop chance of mythic items");
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
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
