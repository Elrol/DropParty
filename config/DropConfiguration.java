package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;

import com.github.elrol.dropparty.libs.BlockPos;
import com.github.elrol.dropparty.libs.TextLibs;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

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
	
	public void createParty(String name, String playerName) {
		loadConfig();
		config.getNode(name);
		config.getNode(name, "creator").setValue(playerName);
		config.getNode(name, "chests");
		config.getNode(name, "drops");
		saveConfig();
	}
	
	public void removeParty(String name) {
		config.getNode(name).setValue(null);
		saveConfig();
	}
	
	public void addChest(CommandSource src, String name, int id, BlockPos pos) {
		if(doesChestExist(name, id, pos)) {
			src.sendMessage(TextLibs.pluginError("Duplicate chest located at: X:" + pos.getX() + ", Y:" + pos.getY() + ", Z:" + pos.getZ()));
		}else {
			config.getNode(name, "chests", id, "x").setValue(pos.getX());
			config.getNode(name, "chests", id, "y").setValue(pos.getY());
			config.getNode(name, "chests", id, "z").setValue(pos.getZ());
			saveConfig();	
			src.sendMessage(TextLibs.pluginMessage("set chest " + id + " for Party " + name));
		}
	}
	
	public void removeChest(String name, int id) {
		config.getNode(name, "chests", id).setValue(null);
		saveConfig();
	}
	
	public void addDrop(String name, int id, BlockPos pos) {
		config.getNode(name, "drops", id, "x").setValue(pos.getX());
		config.getNode(name, "drops", id, "y").setValue(pos.getY());
		config.getNode(name, "drops", id, "z").setValue(pos.getZ());
		saveConfig();
	}
	
	public void removeDrop(String name, int id) {
		config.getNode(name, "drops", id).setValue(null);
		saveConfig();
	}
	
	public int getChestId(String name) {
		List<? extends CommentedConfigurationNode> chests = config.getNode(name, "chests").getChildrenList();
		if(chests.isEmpty())
			return 0;
		return chests.size();
	}
	
	public int getDropId(String name) {
		List<? extends CommentedConfigurationNode> drops = config.getNode(name, "drops").getChildrenList();
		if(drops.isEmpty())
			return 0;
		return drops.size();
	}
	
	public boolean doesPartyExist(String name) {
		return config.getNode(name, "creator").getString() != null;
	}
	
	public List<BlockPos> getChests(String name){
		loadConfig();
		int chestQty = config.getNode(name, "chests").getChildrenList().size();
		List<BlockPos> chests = new ArrayList<BlockPos>();
		for(int id = 0; id < chestQty; id++) {
			try {
				int x = config.getNode(name, "chests", id, "x").getInt();
				int y = config.getNode(name, "chests", id, "y").getInt();
				int z = config.getNode(name, "chests", id, "z").getInt();
				chests.add(new BlockPos(x, y, z));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return chests;
	}
	
	public boolean doesChestExist(String name, int id, BlockPos pos) {
		int max = config.getNode(name, "chests").getChildrenList().size();
		if(id < max)
			return true;
		for(BlockPos chestPos:this.getChests(name)) {
			if(chestPos.equals(pos))
				return true;
		}
		return false;
	}
}
