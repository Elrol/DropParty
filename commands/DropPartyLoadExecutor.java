package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyLoadExecutor implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name") && args.hasAny("list")) {
			String name = args.<String>getOne("name").get();
			String list = args.<String>getOne("list").get();
			
			List<ItemStack> droplist = DropConfiguration.getInstance().getList(list);
			List<ExtendedBlockPos> chests = SetupConfiguration.getInstance().getChests(name);
			
			int i = 0;
			for(ExtendedBlockPos chest : chests) {
				Methods.getCarrier(chest).getInventory().clear();
			}
			for(ExtendedBlockPos chest : chests) {
				TextLibs.sendMessage(src, "Searching for Chest...");
				World world = Sponge.getServer().getWorld(chest.getDim()).get();
				if(!world.getBlock(chest.getX(), chest.getY(), chest.getZ()).getType().equals(BlockTypes.CHEST)) {
					TextLibs.pluginError("Chest at X:" + chest.getX() + " Y:" + chest.getY() + " Z:" + chest.getZ() + " is not found, skipping it.");
					continue;
				}
				TextLibs.sendMessage(src, "Chest Found.");
				Inventory sub = ((TileEntityCarrier)world.getTileEntity(chest.getX(), chest.getY(), chest.getZ()).get()).getInventory().query(GridInventory.class);
			    if(sub instanceof GridInventory) {
			        GridInventory grid = (GridInventory) sub;
			        for(int j = 0; j < grid.capacity(); j++) {
			        	Slot slot = grid.getSlot(j % 9, (j - (j % 9) / 9)).get();
		        		slot.offer(droplist.get(i));
		        		TextLibs.sendMessage(src, "Added " + droplist.get(i).getItem().getName() + " to chest");
		        		i++;
			        	
			        }
			    }
			}
			TextLibs.sendMessage(src, "Loading Complete. Loaded a total of " + droplist.size() + " items!");
		}
		return CommandResult.success();
	}

	
	
}
