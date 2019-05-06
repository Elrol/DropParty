package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyCreateExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			String playerName;
			if(SetupConfiguration.getInstance().doesPartyExist(name)) {
				src.sendMessage(TextLibs.pluginError("A Party by that name already exists."));
				return CommandResult.builder().successCount(0).build();
			}
			if(src instanceof Player)
				playerName = ((Player)src).getName();
			else
				playerName = "Console";
			SetupConfiguration.getInstance().createParty(src, name, playerName);
			src.sendMessage(TextLibs.pluginMessage("Successfully created party: " + name));
			if(args.hasAny("x") && args.hasAny("y") && args.hasAny("z")) {
				int x = args.<Integer>getOne("x").get();
				int y = args.<Integer>getOne("y").get();
				int z = args.<Integer>getOne("z").get();
				World world = Methods.getWorld(args);
				SetupConfiguration.getInstance().addChest(src, name, new Location<World>(world, x, y ,z));
				
			}
		}
		return CommandResult.success();
	}

}
