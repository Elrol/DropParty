package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyRemoveExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		SetupConfiguration config = SetupConfiguration.getInstance();
		String name = args.<String>getOne("name").get();
		if(config.doesPartyExist(name)) {
			config.removeParty(src, name);
			TextLibs.sendMessage(src, "Party " + name + " has been removed.");
		} else {
			TextLibs.sendError(src, "Party " + name + " does not exist, and therfore cannot be removed.");
			return CommandResult.builder().successCount(0).build();
		}
		return CommandResult.success();
	}

}
