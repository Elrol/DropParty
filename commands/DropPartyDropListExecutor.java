package com.github.elrol.dropparty.commands;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyDropListExecutor implements CommandExecutor {

	private int mode;
	
	public DropPartyDropListExecutor(int mode) {
		this.mode = mode;
	}

	@SuppressWarnings("deprecation")
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			if(mode == 0) {
				List<ItemStack> items = DropConfiguration.getInstance().getList(name);
				if(items != null) {
					TextLibs.sendMessage(src, "Current Items in the '" + name + "' DropList:");
					for(ItemStack item : items) {
						TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + item.getTranslation().get()));
					}
				} else {
					TextLibs.sendError(src, "The DropList '" + name + "' has not been created yet.");
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
					List<ExtendedBlockPos> chests = SetupConfiguration.getInstance().getChests(party);
					for(ExtendedBlockPos chest : chests) {
						World world = Sponge.getServer().getWorld(chest.getDim()).get();
						if(!world.getBlock(chest.getX(), chest.getY(), chest.getZ()).getType().equals(BlockTypes.CHEST)) {
							TextLibs.pluginError("Chest at X:" + chest.getX() + " Y:" + chest.getY() + " Z:" + chest.getZ() + " is not found, skipping it.");
							continue;
						}
						TileEntityCarrier carrier = (TileEntityCarrier)world.getTileEntity(chest.getX(), chest.getY(), chest.getZ()).get();
						
						Inventory sub = carrier.getInventory().query(GridInventory.class);
					    if(sub instanceof GridInventory) {
					        GridInventory grid = (GridInventory) sub;
					        grid.slots().forEach(slot -> {
					            Optional<ItemStack> stackOptional = slot.peek();
					            if (stackOptional.isPresent()) {
					            	ItemStack item = stackOptional.get();
					            	if(!DropConfiguration.getInstance().doesItemExist(name, item)) {
					            		DropConfiguration.getInstance().addDropListItem(src, name, item);
					            	}
					            }
					        });
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
