package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyStopExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Main.getInstance().getEventManager().stopScheduledEvent();
		TextLibs.sendMessage(src, "DropParty Stopped");
		return CommandResult.success();
	}

}
