package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.PluginInfo.Permissions;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyCostExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			if(args.hasAny("cost") && src.hasPermission(Permissions.dropPartyCostSet)) {
				int cost = args.<Integer>getOne("cost").get();
				if(cost < -1) {
					TextLibs.sendError(src, "The cost can't be lower then -1, Defaulting to -1");
					SetupConfiguration.getInstance().setPartyCost(name, -1);
				}
				SetupConfiguration.getInstance().setPartyCost(name, cost);
				return CommandResult.success();
			}
			TextLibs.sendMessage(src, "The cost to start the " + name + " Drop Party is " + SetupConfiguration.getInstance().getPartyCost(name));
			return CommandResult.success();
		}
		return CommandResult.success();
	}

}
