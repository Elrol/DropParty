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
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyClearExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		SetupConfiguration config = SetupConfiguration.getInstance();
		String name = args.<String>getOne("name").get();
		if(!config.getParties().contains(name)) {
			TextLibs.pluginError("Argument invalid name");
			return CommandResult.builder().successCount(0).build();
		}
		List<ExtendedBlockPos> chests = config.getChests(name);
		for(ExtendedBlockPos chest : chests) {
			World world = Sponge.getServer().getWorld(chest.getDim()).get();
			if(!world.getBlock(chest.getX(), chest.getY(), chest.getZ()).getType().equals(BlockTypes.CHEST)) {
				TextLibs.pluginError("Chest at X:" + chest.getX() + " Y:" + chest.getY() + " Z:" + chest.getZ() + " is not found, skipping it.");
				continue;
			}
			TileEntityCarrier carrier = (TileEntityCarrier)world.getTileEntity(chest.getX(), chest.getY(), chest.getZ()).get();
			carrier.getInventory().clear();
			
		}
		return null;
	}

}
