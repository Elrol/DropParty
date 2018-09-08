package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyRenameExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String name = args.<String>getOne("name").get();
		String newname = args.<String>getOne("newname").get();
		String creator = src instanceof Player ? ((Player)src).getName() : "Console";
		SetupConfiguration config = SetupConfiguration.getInstance();
		config.createParty(src, newname, creator);
		
		List<ExtendedBlockPos> chests = config.getChests(name);
		for(int i = chests.size()-1; i >= 0; i--) {
			config.addChest(src, newname, chests.get(i));
			config.removeChest(src, name, i);
		}
		
		List<ExtendedBlockPos> drops = config.getDrops(name);
		for(int j = drops.size()-1; j >= 0; j--) {
			config.addDrop(src, newname, drops.get(j));
			config.removeDrop(src, name, j);
		}
		config.removeParty(src, name);
		TextLibs.sendMessage(src, "Successfully renamed DropParty " + name + " to " + newname + "!");
		return CommandResult.success();
	}

}
