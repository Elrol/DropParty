package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.libs.BlockPos;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyCreateExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			String playerName;
			if(DropConfiguration.getInstance().doesPartyExist(name)) {
				src.sendMessage(TextLibs.pluginError("A Party by that name already exists."));
				return CommandResult.builder().successCount(0).build();
			}
			if(src instanceof Player)
				playerName = ((Player)src).getName();
			else
				playerName = "Console";
			DropConfiguration.getInstance().createParty(name, playerName);
			src.sendMessage(TextLibs.pluginMessage("Successfully created party: " + name));
			if(args.hasAny("x") && args.hasAny("y") && args.hasAny("z")) {
				int x = args.<Integer>getOne("x").get();
				int y = args.<Integer>getOne("y").get();
				int z = args.<Integer>getOne("z").get();
				int id = DropConfiguration.getInstance().getChestId(name);
				DropConfiguration.getInstance().addChest(src, name, id, new BlockPos(x, y ,z));
				
			}
		}
		return CommandResult.success();
	}

}
