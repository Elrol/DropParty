package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyLoadExecutor implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name") && args.hasAny("list")) {
			String name = args.<String>getOne("name").get();
			String list = args.<String>getOne("list").get();
			
			List<ItemStack> droplist = DropConfiguration.getInstance().getList(list);
			List<Location<World>> chests = SetupConfiguration.getInstance().getChests(name);
			
			int i = 0;
			int qty = 0;
			for(Location<World> chest : chests) {
				Methods.getCarrier(chest).getInventory().clear();
			}
			for(Location<World> chest : chests) {
				TextLibs.sendMessage(src, "Searching for Chest...");
				if(!chest.getBlock().getType().equals(BlockTypes.CHEST)) {
					TextLibs.pluginError("Chest at X:" + chest.getBlockX() + " Y:" + chest.getBlockY() + " Z:" + chest.getBlockZ() + " is not found, skipping it.");
					continue;
				}
				TextLibs.sendMessage(src, "Chest Found.");
				Inventory inv = Methods.getCarrier(chest).getInventory();
				if(inv == null) {
					continue;
				}
				for(Inventory slot : inv.slots()) {
					if(i < droplist.size()) {
						slot.offer(droplist.get(i));
		        		String itemname = droplist.get(i).getType().getName();
		        		if(droplist.get(i).get(Keys.DISPLAY_NAME).isPresent())
		        			itemname = droplist.get(i).get(Keys.DISPLAY_NAME).get().toPlain();
		        		TextLibs.sendMessage(src, "Added " + itemname + " to chest");
		        		qty += droplist.get(i).getQuantity();
		        		i++;
					} else {
						break;
					}
			    }
			}
			TextLibs.sendMessage(src, "Loading Complete. Loaded a total of " + qty + " items!");
		}
		return CommandResult.success();
	}

	
	
}
