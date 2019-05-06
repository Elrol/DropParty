package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
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
		List<Location<World>> chests = config.getChests(name);
		for(Location<World> chest : chests) {
			Methods.getCarrier(chest).getInventory().clear();
		}
		return CommandResult.success();
	}

}
