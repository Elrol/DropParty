package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandSource;

import com.github.elrol.dropparty.Main;
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
		config.getNode(name, "settings", "chests");
		config.getNode(name, "settings", "drops");
		saveConfig();
	}
	
	public void removeParty(String name) {
		config.getNode(name).setValue(null);
		saveConfig();
	}
	
	public void addChest(CommandSource src, String name, BlockPos pos) {
		int id = getChestId(name);
		if(doesChestExist(name, id, pos)) {
			src.sendMessage(TextLibs.pluginError("Duplicate chest located at: X:" + pos.getX() + ", Y:" + pos.getY() + ", Z:" + pos.getZ() + "(" + pos.getDim() + ")"));
		}else {
			if(Sponge.getServer().getWorld(pos.getDim()).get().getBlock(pos.getX(), pos.getY(), pos.getZ()).getType().equals(BlockTypes.CHEST) ) {
				config.getNode(name, "settings", "chests", id, "x").setValue(pos.getX());
				config.getNode(name, "settings", "chests", id, "y").setValue(pos.getY());
				config.getNode(name, "settings", "chests", id, "z").setValue(pos.getZ());
				config.getNode(name, "settings", "chests", id, "dim").setValue(pos.getDim());
				saveConfig();	
				src.sendMessage(TextLibs.pluginMessage("set chest " + id + " for Party " + name));
			} else {
				src.sendMessage(TextLibs.pluginError("No chest found at that location."));
			}
		}
	}
	
	public void removeChest(CommandSource src, String name, int id) {
		if(doesChestExist(name, id)) {
			config.getNode(name, "settings",  "chests", id).setValue(null);
			src.sendMessage(TextLibs.pluginMessage("Chest " + id + " from: " + name + " was removed"));
			saveConfig();
			return;
		} else {
			src.sendMessage(TextLibs.pluginError("Chest " + id + " from: " + name + " was not found, could not remove"));
			return;
		}
	}
	
	public void addDrop(CommandSource src, String name, BlockPos pos) {
		int id = getDropId(name);
		if(doesDropExist(name, id, pos)) {
			src.sendMessage(TextLibs.pluginError("Duplicate drop located at: X:" + pos.getX() + ", Y:" + pos.getY() + ", Z:" + pos.getZ() + "(" + pos.getDim() + ")"));
		}else {
			if(Sponge.getServer().getWorld(pos.getDim()).get().getBlock(pos.getX(), pos.getY(), pos.getZ()).getType().equals(BlockTypes.AIR) ) {
				config.getNode(name, "settings", "drops", id, "x").setValue(pos.getX());
				config.getNode(name, "settings", "drops", id, "y").setValue(pos.getY());
				config.getNode(name, "settings", "drops", id, "z").setValue(pos.getZ());
				config.getNode(name, "settings", "drops", id, "dim").setValue(pos.getDim());
				saveConfig();	
				src.sendMessage(TextLibs.pluginMessage("set drop " + id + " for Party " + name));
			} else {
				src.sendMessage(TextLibs.pluginError("Location is blocked, remove block and try again."));
			}
		}
	}
	
	public void removeDrop(CommandSource src, String name, int id) {
		if(doesDropExist(name, id)) {
			config.getNode(name, "settings", "drops", id).setValue(null);
			src.sendMessage(TextLibs.pluginMessage("Drop " + id + " from: " + name + " was removed"));
			saveConfig();
			return;
		} else {
			src.sendMessage(TextLibs.pluginError("Drop " + id + " from: " + name + " was not found, could not remove"));
			return;
		}
	}
	
	public int getChestId(String name) {
		List<? extends CommentedConfigurationNode> chests = config.getNode(name, "settings", "chests").getChildrenList();
		if(chests.isEmpty())
			return 0;
		return chests.size();
	}
	
	public int getDropId(String name) {
		List<? extends CommentedConfigurationNode> drops = config.getNode(name, "settings", "drops").getChildrenList();
		if(drops.isEmpty())
			return 0;
		return drops.size();
	}
	
	public boolean doesPartyExist(String name) {
		return config.getNode(name, "creator").getString() != null;
	}
	
	public List<BlockPos> getChests(String name){
		loadConfig();
		int chestQty = config.getNode(name, "settings", "chests").getChildrenList().size();
		List<BlockPos> chests = new ArrayList<BlockPos>();
		for(int id = 0; id < chestQty; id++) {
			try {
				int x = config.getNode(name, "settings", "chests", id, "x").getInt();
				int y = config.getNode(name, "settings", "chests", id, "y").getInt();
				int z = config.getNode(name, "settings", "chests", id, "z").getInt();
				String dim = config.getNode(name, "settings", "chests", id, "dim").getString();
				chests.add(new BlockPos(x, y, z, dim));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return chests;
	}
	
	public boolean doesChestExist(String name, int id, BlockPos pos) {
		for(BlockPos chest : getChests(name)) {
			if(chest.getDim().equalsIgnoreCase(pos.getDim())) {
				if(chest.getX() == pos.getX() && chest.getY() == pos.getY() && chest.getZ() == pos.getZ()) {
					return true;
				} else {
					Main.getInstance().getLogger().debug(chest.toString() + " is not the same as " + pos.toString());
				}
			} else {
			
			}
		}
		return false;
	}
	
	public boolean doesChestExist(String name, int id) {
		return getChests(name).get(id) != null;
	}
	
	public List<BlockPos> getDrops(String name){
		loadConfig();
		int dropQty = config.getNode(name, "settings", "drops").getChildrenList().size();
		List<BlockPos> drops = new ArrayList<BlockPos>();
		for(int id = 0; id < dropQty; id++) {
			try {
				int x = config.getNode(name, "settings", "drops", id, "x").getInt();
				int y = config.getNode(name, "settings", "drops", id, "y").getInt();
				int z = config.getNode(name, "settings", "drops", id, "z").getInt();
				String dim = config.getNode(name, "settings", "drops", id, "dim").getString();
				drops.add(new BlockPos(x, y, z, dim));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return drops;
	}
	
	public boolean doesDropExist(String name, int id, BlockPos pos) {
		int max = config.getNode(name, "settings", "drops").getChildrenList().size();
		if(id < max && !config.getNode(name, "settings", "drops", id, "y").isVirtual())
			return true;
		for(BlockPos dropPos:this.getDrops(name)) {
			if(dropPos.equals(pos))
				return true;
		}
		return false;
	}
	
	public boolean doesDropExist(String name, int id) {
		int max = config.getNode(name, "settings", "drops").getChildrenList().size();
		if(id < max && !config.getNode(name, "settings", "drops", id, "y").isVirtual())
			return true;
		return false;
	}
}
