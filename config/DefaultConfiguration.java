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
				config.getNode("DropParty", "Tier Drops").setComment("All values must be integers");
				config.getNode("DropParty", "Tier Drops", "Tier 0").setValue(50);
				config.getNode("DropParty", "Tier Drops", "Tier 1").setValue(25);
				config.getNode("DropParty", "Tier Drops", "Tier 2").setValue(15);
				config.getNode("DropParty", "Tier Drops", "Tier 3").setValue(10);
				config.getNode("DropParty", "Tier Drops", "Tier 4").setValue(5);
				config.getNode("DropParty", "Tier Drops", "Tier 5").setValue(1);
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
	
	public int getTierChance(int tier) {
		return config.getNode("DropParty", "Tier Drops", "Tier " + tier).getInt();
	}
	
	public int getTotalChance() {
		int total = 0;
		String output = "";
		for(int i = 0; i < 6; i++) {
			output += "Tier " + i + " [" + getTierChance(i) + "], ";
			total += getTierChance(i);
		}
		output += total;
		System.out.println(output);
		return total;
	}
	
	public int getTierRange(int tier) {
		int range = 0;
		for(int i = 0; i <= tier; i++) {
			range += getTierChance(i);
		}
		return range;
	}
	public int[] getRange() {
		return new int[] {
				getTierRange(0),
				getTierRange(1),
				getTierRange(2),
				getTierRange(3),
				getTierRange(4),
				getTierRange(5)
		};
	}
	
}
