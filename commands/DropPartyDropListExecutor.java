package com.github.elrol.dropparty.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyDropListExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		int mode = 0;
		if(args.hasAny("sub"))
			mode = args.<Integer>getOne("sub").get();
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			if(mode == 0) {
				Map<ItemStack, Integer> map = DropConfiguration.getInstance().getMap(name);
				if(map != null && !map.isEmpty()) {
					TextLibs.sendMessage(src, "Current Items in the '" + name + "' DropList:");
					for(ItemStack item : map.keySet()) {
						TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + item.getTranslation().get() + " (x" + map.get(item) + ")"));
					}
				} else {
					TextLibs.sendError(src, "The DropList '" + name + "' has not been created yet, or the list is empty.");
				}
				return CommandResult.success();
			} else if(mode == 1) {
				if(src instanceof Player) {
					Player player = (Player)src;
					if(player.getEquipped(EquipmentTypes.MAIN_HAND).isPresent()) {
						ItemStack type = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
						DropConfiguration.getInstance().addDropListItem(src, name, type);
						return CommandResult.success();
					} else {
						TextLibs.sendError(src, "You must be holding an Item in your hand to add it to the '" + name + "' DropList");
						return CommandResult.builder().successCount(0).build();
					}
				} else {
					TextLibs.sendError(src, "You must be a player to add items to the '" + name + "' DropList");
					return CommandResult.builder().successCount(0).build();
				}
			} else if(mode == 2) {
				if(src instanceof Player) {
					Player player = (Player)src;
					if(player.getEquipped(EquipmentTypes.MAIN_HAND).isPresent()) {
						ItemStack type = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
						DropConfiguration.getInstance().removeDropListItem(src, name, type);
						return CommandResult.success();
					} else {
						TextLibs.sendError(src, "You must be holding an Item in your hand to remove it from the '" + name + "' DropList");
						return CommandResult.builder().successCount(0).build();
					}
				} else {
					TextLibs.sendError(src, "You must be a player to remove items from the '" + name + "' DropList");
					return CommandResult.builder().successCount(0).build();
				}
			} else if(mode == 3){
				DropConfiguration.getInstance().removeAll(name);
				TextLibs.sendMessage(src, "Cleared all Items from the '" + name + "' DropList");
				return CommandResult.success();
			} else if(mode == 4){
				if(args.hasAny("party")) {
					String party = args.<String>getOne("party").get();
					if(!SetupConfiguration.getInstance().doesPartyExist(party)) {
						TextLibs.sendError(src, "Party does not exist");
						return CommandResult.empty();
					}
					List<Location<World>> chests = SetupConfiguration.getInstance().getChests(party);
					if(chests.isEmpty() || chests == null) {
						TextLibs.sendError(src, "There are no chests defined for the party");
						return CommandResult.empty();
					}
					for(Location<World> chest : chests) {
						if(!chest.getBlock().getType().equals(BlockTypes.CHEST)) {
							TextLibs.pluginError("Chest at X:" + chest.getBlockX() + " Y:" + chest.getBlockY() + " Z:" + chest.getBlockZ() + " is not found, skipping it.");
							continue;
						}
						Inventory inv = Methods.getCarrier(chest).getInventory();
						if(inv == null) {
							continue;
						}
						for(Inventory slot : inv.slots()) {
					        Optional<ItemStack> stackOptional = slot.peek();
				            if (stackOptional.isPresent()) {
				            	ItemStack item = stackOptional.get();
				            	DropConfiguration.getInstance().addDropListItem(src, name, item);
				            	TextLibs.sendConsoleMessage("Added Item to droplist");
				            	
				            }
					    }
					}
				} else {
					TextLibs.sendError(src, "You must specify a DropParty Setup to add Items from");
				}
			} else {
				if(!DropConfiguration.getInstance().doesDropListExist(name)) {
					String playername;
					if(src instanceof Player)
						playername = ((Player)src).getName();
					else
						playername = "Console";
					DropConfiguration.getInstance().createDropList(src, name, playername);
					return CommandResult.builder().successCount(0).build();
				} else {
					TextLibs.sendError(src, "The DropList '" + name + "' already exists");
					return CommandResult.success();
				}
			}
		}
		return CommandResult.builder().successCount(0).build();
	}
	
}
