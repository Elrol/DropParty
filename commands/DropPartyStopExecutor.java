package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.libs.PluginInfo.Permissions;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyStopExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(Main.getInstance().getEventManager().isRunning) {
			Player player = Main.getInstance().getEventManager().getInitiator();
			if(src instanceof ConsoleSource || src.hasPermission(Permissions.dropPartyStopOverride) || (player != null && src instanceof Player && player.getUniqueId().equals(((Player)src).getUniqueId())))
			Main.getInstance().getEventManager().stopScheduledEvent();
			TextLibs.sendMessage(src, "DropParty Stopped");
		} else {
			TextLibs.sendError(src, "No DropParty is running");
		}
		return CommandResult.success();
	}

}
