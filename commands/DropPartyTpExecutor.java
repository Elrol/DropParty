package com.github.elrol.dropparty.commands;

import java.util.List;
import java.util.Random;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyTpExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String name = args.<String>getOne("name").get();
		Random rand = new Random();
		Player player;
		if(args.hasAny("player")) {
			player = args.<Player>getOne("player").get();
		} else {
			if(src instanceof Player) {
				player = (Player)src;
			} else {
				TextLibs.sendError(src, "When using console, you must specify a player.");
				return CommandResult.builder().successCount(0).build();
			}
		}
		List<Location<World>> drops = SetupConfiguration.getInstance().getDrops(name);
		Location<World> drop = drops.get(rand.nextInt(drops.size()));
		player.transferToWorld(drop.getExtent());
		for(int y = drop.getBlockY(); y > 0; y--) {
			if(!(drop.getBlock().getType().equals(BlockTypes.AIR))) {
				player.setLocation(new Location<World>(player.getWorld(), drop.getX() + 0.5D, y + 1.5D, drop.getZ() + 0.5D));
				TextLibs.sendPlayerMessage(player, "Welcome to the " + name + " party!");
				TextLibs.sendConsoleMessage("Teleported " + player.getName() + " to the " + name + " party!");
				break;
			}
		}
		return CommandResult.success();
	}

}
