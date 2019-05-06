package com.github.elrol.dropparty.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import com.github.elrol.dropparty.libs.TextLibs;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class TierConfiguration {

	private static TierConfiguration instance = new TierConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	private List<ItemStack> tier1Items = new ArrayList<ItemStack>();
	private List<ItemStack> tier2Items = new ArrayList<ItemStack>();
	private List<ItemStack> tier3Items = new ArrayList<ItemStack>();
	private List<ItemStack> tier4Items = new ArrayList<ItemStack>();
	private List<ItemStack> tier5Items = new ArrayList<ItemStack>();
	
	
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
					config.getNode("Tier 1").setComment("A list of Items that are Uncommon").setValue(new TypeToken<List<ItemStack>>() {}, tier1Items);
					config.getNode("Tier 2").setComment("A list of Items that are Rare").setValue(new TypeToken<List<ItemStack>>() {}, tier2Items);
					config.getNode("Tier 3").setComment("A list of Items that are Epic").setValue(new TypeToken<List<ItemStack>>() {}, tier3Items);
					config.getNode("Tier 4").setComment("A list of Items that are Legendary").setValue(new TypeToken<List<ItemStack>>() {}, tier4Items);
					config.getNode("Tier 5").setComment("A list of Items that are Mythic").setValue(new TypeToken<List<ItemStack>>() {}, tier5Items);
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
		tier1Items.add(ItemStack.builder().itemType(ItemTypes.IRON_INGOT).quantity(1).build());
		tier2Items.add(ItemStack.builder().itemType(ItemTypes.GOLD_INGOT).quantity(1).build());
		tier3Items.add(ItemStack.builder().itemType(ItemTypes.DIAMOND).quantity(1).build());
		tier4Items.add(ItemStack.builder().itemType(ItemTypes.EMERALD).quantity(1).build());
		tier5Items.add(ItemStack.builder().itemType(ItemTypes.NETHER_STAR).quantity(1).build());
	}
	
	@SuppressWarnings("serial")
	public void addItem(CommandSource src, int tier, ItemStack type) {
		loadConfig();
		try {
			List<ItemStack> items = new ArrayList<ItemStack>();
			if(getTier(tier) != null)
				items = getTier(tier);
			if(!isItemListed(type)) {
				items.add(type);
				config.getNode("Tier " + tier).setValue(new TypeToken<List<ItemStack>>() {}, items);
				saveConfig();
				TextLibs.sendMessage(src, "Successfully added '" + type.getTranslation().get() + "' to 'Tier " + tier + "'");
				return;
			}
			TextLibs.sendError(src, "Couldn't add '" + type + "' to 'Tier " + tier + "'");
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isItemListed(ItemStack type) throws ObjectMappingException {
		for(int i = 1; i < 6; i++) {
			if(isItemListed(i, type))
				return true;
		}
		return false;
	}
	
	public boolean isItemListed(int tier, ItemStack type) {
		loadConfig();
		if(tier == 0) {
			for(int i = 1; i < 6; i++) {
				if(getTier(i) == null)
					continue;
				for(ItemStack stack : getTier(i)) {
					if(stack.getType().equals(type.getType()) && ItemStackComparators.ITEM_DATA.compare(type, stack) == 0) {
						//System.out.println(type.getTranslation().get() + " is not in tier " + tier);
						return false;
					}
				}
			}
			return true;
		}
		List<ItemStack> items = getTier(tier);
		if(items != null && !items.isEmpty()) {
			for(ItemStack stack : items) {
				if(stack.getType().equals(type.getType()) && ItemStackComparators.ITEM_DATA.compare(type, stack) == 0) {
					//System.out.println(type.getTranslation().get() + " found in tier " + tier);
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	public List<ItemStack> getTier(int tier){
		List<ItemStack> list = new ArrayList<ItemStack>();
		loadConfig();
		try {
			List<ItemStack> temp = config.getNode("Tier " + tier).getValue(new TypeToken<List<ItemStack>>() {});
			if(temp != null)
				list = temp;
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("serial")
	public void removeItem(CommandSource src, int tier, ItemStack item) {
		loadConfig();
		List<ItemStack> items = getTier(tier);
		for(ItemStack stack : new ArrayList<ItemStack>(items)) {
			if(stack.getType().equals(item.getType()) && ItemStackComparators.ITEM_DATA.compare(item, stack) == 0) {
				try {
					items.remove(stack);
					config.getNode("Tier " + tier).setValue(new TypeToken<List<ItemStack>>() {}, items);
					TextLibs.sendMessage(src, "'" + item.getTranslation().get() + "' was removed from 'Tier " + tier + "'");
					saveConfig();
				} catch(ObjectMappingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	public void clearTier(CommandSource src, int tier) {
		loadConfig();
		List<ItemStack> items = new ArrayList<ItemStack>();
		try {
			config.getNode("Tier " + tier).setValue(new TypeToken<List<ItemStack>>() {}, items);
			TextLibs.sendMessage(src, "Cleared 'Tier " + tier + "' of all Items");
			saveConfig();
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
}
