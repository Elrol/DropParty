package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class SetupConfiguration {

	private static SetupConfiguration instance = new SetupConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	
	public static SetupConfiguration getInstance() {
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
	
	public void createParty(CommandSource src, String name, String playerName) {
		if(!doesPartyExist(name)) {
			loadConfig();
			config.getNode("DropParties", name);
			config.getNode("DropParties", name, "creator").setValue(playerName);
			config.getNode("DropParties", name, "settings", "chests");
			config.getNode("DropParties", name, "settings", "drops");
			saveConfig();
			TextLibs.sendMessage(src, "Created the '" + name + "' DropParty Setup");
		} else {
			TextLibs.sendError(src, "The DropParty Setup '" + name + "' already exists");
		}
	}
	
	public void removeParty(CommandSource src, String name) {
		if(doesPartyExist(name)) {
			loadConfig();
			config.getNode("DropParties", name).setValue(null);
			saveConfig();
			TextLibs.sendMessage(src, "Removed the '" + name + "' DropParty Setup");
		} else {
			TextLibs.sendError(src, "The DropParty Setup '" + name + "' does not exist");
		}
	}
	
	public void addChest(CommandSource src, String name, Location<World> loc) {
		if(doesPartyExist(name)) {
			int id = getChestId(name);
			if(doesChestExist(name, id, loc)) {
				TextLibs.sendError(src, "Duplicate chest located at: X:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ() + "(" + loc.getExtent().getName() + ")");
			} else {
				if(loc.getBlock().getType().equals(BlockTypes.CHEST) ) {
					config.getNode("DropParties", name, "settings", "chests", id, "x").setValue(loc.getBlockX());
					config.getNode("DropParties", name, "settings", "chests", id, "y").setValue(loc.getBlockY());
					config.getNode("DropParties", name, "settings", "chests", id, "z").setValue(loc.getBlockZ());
					config.getNode("DropParties", name, "settings", "chests", id, "dim").setValue(loc.getExtent().getUniqueId().toString());
					saveConfig();	
					TextLibs.sendMessage(src, "set chest '" + id + "' for Party '" + name +"'");
				} else {
					TextLibs.sendError(src, "No chest found at that location.");
				}
			}
		} else {
			TextLibs.sendError(src, "The DropParty Setup '" + name + "' does not exist");
		}
	}
	
	public void removeChest(CommandSource src, String name, int id) {
		if(doesPartyExist(name)) {
			if(doesChestExist(name, id)) {
				config.getNode("DropParties", name, "settings",  "chests", id).setValue(null);
				TextLibs.sendMessage(src, "Chest '" + id + "' from: '" + name + "' was removed");
				saveConfig();
				return;
			} else {
				TextLibs.sendError(src, "Chest '" + id + "' from: '" + name + "' was not found, could not remove");
				return;
			}
		} else {
			TextLibs.sendError(src, "The DropParty Setup '" + name + "' does not exist");
		}
	}
	
	public void addDrop(CommandSource src, String name, Location<World> loc) {
		if(doesPartyExist(name)) {
			int id = getDropId(name);
			if(doesDropExist(name, id, loc)) {
				TextLibs.sendError(src, "Duplicate drop located at: X:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ() + "(" + loc.getExtent().getName() + ")");
				return;
			}else {
				if(loc.getBlock().getType().equals(BlockTypes.AIR) ) {
					config.getNode("DropParties", name, "settings", "drops", id, "x").setValue(loc.getBlockX());
					config.getNode("DropParties", name, "settings", "drops", id, "y").setValue(loc.getBlockY());
					config.getNode("DropParties", name, "settings", "drops", id, "z").setValue(loc.getBlockZ());
					config.getNode("DropParties", name, "settings", "drops", id, "dim").setValue(loc.getExtent().getUniqueId().toString());
					saveConfig();	
					TextLibs.sendMessage(src, "set drop '" + id + "' for Party '" + name + "'");
					return;
				} else {
					TextLibs.sendError(src, "Location is blocked, remove block and try again.");
					return;
				}
			}
		} else {
			TextLibs.sendError(src, "The DropParty Setup '" + name + "' does not exist");
			return;
		}
	}
	
	public void removeDrop(CommandSource src, String name, int id) {
		if(doesPartyExist(name)) {
			if(doesDropExist(name, id)) {
				config.getNode("DropParties", name, "settings", "drops", id).setValue(null);
				TextLibs.sendMessage(src, "Drop '" + id + "' from: '" + name + "' was removed");
				saveConfig();
				return;
			} else {
				TextLibs.sendError(src, "Drop '" + id + "' from: '" + name + "' was not found, could not remove");
				return;
			}
		} else {
			TextLibs.sendError(src, "The DropParty Setup '" + name + "' does not exist");
		}
	}
	
	public int getChestId(String name) {
		List<? extends CommentedConfigurationNode> chests = config.getNode("DropParties", name, "settings", "chests").getChildrenList();
		if(chests.isEmpty())
			return 0;
		return chests.size();
	}
	
	public int getDropId(String name) {
		List<? extends CommentedConfigurationNode> drops = config.getNode("DropParties", name, "settings", "drops").getChildrenList();
		if(drops.isEmpty())
			return 0;
		return drops.size();
	}
	
	public boolean doesPartyExist(String name) {
		return config.getNode("DropParties", name, "creator").getString() != null;
	}
	
	public List<Location<World>> getChests(String name){
		loadConfig();
		int chestQty = config.getNode("DropParties", name, "settings", "chests").getChildrenList().size();
		List<Location<World>> chests = new ArrayList<Location<World>>();
		for(int id = 0; id < chestQty; id++) {
			try {
				int x = config.getNode("DropParties", name, "settings", "chests", id, "x").getInt();
				int y = config.getNode("DropParties", name, "settings", "chests", id, "y").getInt();
				int z = config.getNode("DropParties", name, "settings", "chests", id, "z").getInt();
				UUID dim = UUID.fromString(config.getNode("DropParties", name, "settings", "chests", id, "dim").getString());
				Optional<World> world = Sponge.getServer().getWorld(dim);
				if(!world.isPresent()) {
					TextLibs.sendConsoleError("UUID for world was invalid: " + dim);
					continue;
				}
				Location<World> loc = new Location<World>(world.get(), x, y, z);
				if(loc.getBlock().getType().equals(BlockTypes.CHEST) || loc.getBlock().getType().equals(BlockTypes.TRAPPED_CHEST)) {
					chests.add(loc);
				} else {
					removeChest(Sponge.getServer().getConsole(), name, id);
					TextLibs.sendConsoleError("Chest at X: " + x + ", Y: " + y + ", Z: " + z + " was missing, and was removed from the party");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return chests;
	}
	
	public boolean doesChestExist(String name, int id, Location<World> pos) {
		for(Location<World> chest : getChests(name)) {
			if(chest.getExtent().getUniqueId().equals(pos.getExtent().getUniqueId())) {
				if(chest.getBlockX() == pos.getBlockX() && chest.getBlockY() == pos.getBlockY() && chest.getBlockZ() == pos.getBlockZ()) {
					return true;
				}
			} else {
			
			}
		}
		return false;
	}
	
	public boolean doesChestExist(String name, int id) {
		return getChests(name).get(id) != null;
	}
	
	public List<Location<World>> getDrops(String name){
		loadConfig();
		int dropQty = config.getNode("DropParties", name, "settings", "drops").getChildrenList().size();
		List<Location<World>> drops = new ArrayList<Location<World>>();
		for(int id = 0; id < dropQty; id++) {
			try {
				int x = config.getNode("DropParties", name, "settings", "drops", id, "x").getInt();
				int y = config.getNode("DropParties", name, "settings", "drops", id, "y").getInt();
				int z = config.getNode("DropParties", name, "settings", "drops", id, "z").getInt();
				UUID dim = UUID.fromString(config.getNode("DropParties", name, "settings", "drops", id, "dim").getString());
				Optional<World> world = Sponge.getServer().getWorld(dim);
				if(!world.isPresent()) {
					TextLibs.sendConsoleError("UUID for world was invalid: " + dim);
					continue;
				}
				drops.add(new Location<World>(world.get(), x, y, z));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return drops;
	}
	
	public boolean doesDropExist(String name, int id, Location<World> loc) {
		int max = config.getNode("DropParties", name, "settings", "drops").getChildrenList().size();
		if(id < max && !config.getNode("DropParties", name, "settings", "drops", id, "y").isVirtual())
			return true;
		for(Location<World> dropPos : this.getDrops(name)) {
			if(dropPos.equals(loc))
				return true;
		}
		return false;
	}
	
	public boolean doesDropExist(String name, int id) {
		int max = config.getNode("DropParties", name, "settings", "drops").getChildrenList().size();
		if(id < max && !config.getNode("DropParties", name, "settings", "drops", id, "y").isVirtual())
			return true;
		return false;
	}
	
	public void listParties(CommandSource src) {
		loadConfig();
		for(Object node : config.getNode("DropParties").getChildrenMap().keySet()) {
			TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + node));
			TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + "Chests{"));
			if(getChests((String)node).isEmpty())
				TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + TextLibs.tab, TextColors.RED, "No Chests Set"));
			else {
				int i = 0;
				for(Location<World> chest : getChests((String)node)) {
					TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + TextLibs.tab + "[" + i + "] { Dim: " + chest.getExtent().getName() + " X: " + chest.getBlockX() + " Y: " + chest.getBlockY() + " Z: " + chest.getBlockZ() + " }"));
					i++;
				}
			}
			TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + "}"));
			TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + "Drops{"));
			if(getDrops((String)node).isEmpty())
				TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + TextLibs.tab, TextColors.RED, "No Drops Set"));
			else {
				int i = 0;
				for(Location<World> drop : getDrops((String)node)) {
					TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + TextLibs.tab + "[" + i + "] { Dim: " + drop.getExtent().getName() + " X: " + drop.getBlockX() + " Y: " + drop.getBlockY() + " Z: " + drop.getZ() + " }"));
					i++;
				}
			}
			TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + TextLibs.tab + "}"));
		}
	}
	
	public List<String> getParties() {
		loadConfig();
		List<String> list = new ArrayList<String>();
		for(Object node : config.getNode("DropParties").getChildrenMap().keySet()) {
			list.add((String)node);
		}
		return list;
	}
	
	public void spawnItemAtDrop(String name, ItemStack item) {
		Main.getInstance().getLogger().debug("attempting to spawn item");
		List<Location<World>> drops = getDrops(name);
		Random rand = new Random();
		Location<World> loc = drops.get(rand.nextInt(drops.size()));
		Entity itemEntity = loc.createEntity(EntityTypes.ITEM);
        Item items = (Item) itemEntity;
        items.offer( Keys.REPRESENTED_ITEM, item.createSnapshot() );
        loc.spawnEntity(items);
	}
	
	public int getPartyItemQty(String name) {
		List<Location<World>> chests = getChests(name);
		int totalItems = 0;
		for(Location<World> chest: chests) {
			for(Inventory inv : Methods.getCarrier(chest).getInventory()) {
				totalItems += inv.totalItems();
			}
		}
		TextLibs.sendConsoleMessage("Total Items: " + totalItems);
		return totalItems;
	}
	
	public void setPartyCost(String name, int cost) {
		config.getNode("DropParties", name, "settings", "cost").setValue(cost);
		saveConfig();
	}
	
	public int getPartyCost(String name) {
		return config.getNode("DropParties", name, "settings", "cost").getInt();
	}
	
}
