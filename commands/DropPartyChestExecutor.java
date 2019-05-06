package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyChestExecutor implements CommandExecutor {
	
	private int mode;
	
	/**Int mode
	 * 0: list, 1: add, 2: remove
	 * */
	public DropPartyChestExecutor(int mode) {
		this.mode = mode;
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			if(mode == 0) {
				src.sendMessage(TextLibs.pluginMessage("List of chests for " + name));
				List<Location<World>> chests = SetupConfiguration.getInstance().getChests(name);
				if(!chests.isEmpty()) {
					for(int i = 0; i < chests.size(); i++) {
						src.sendMessage(Text.of("[" + i + "] X:" + chests.get(i).getBlockX() + ", Y:" + chests.get(i).getBlockY() + ", Z:" + chests.get(i).getBlockZ() + "(" + chests.get(i).getExtent().getName() + ")"));
					}
				} else {
					src.sendMessage(Text.of("No chests found"));
				}
				return CommandResult.success();
			}else if(mode == 1) {
				if(args.hasAny("x") && args.hasAny("y") && args.hasAny("z")) {
					int x = args.<Integer>getOne("x").get();
					int y = args.<Integer>getOne("y").get();
					int z = args.<Integer>getOne("z").get();
					World world = Methods.getWorld(args);
					int id = SetupConfiguration.getInstance().getChestId(name);
					src.sendMessage(TextLibs.pluginMessage("set chest " + id + " for Party " + name));
					SetupConfiguration.getInstance().addChest(src, name, new Location<World>(world, x,y,z));
					return CommandResult.success();
				} else {
					if(src instanceof Player) {
						Player player = (Player)src;
						List<Location<World>> chests = Methods.getChest(player);
						for(Location<World> loc : chests) {
							SetupConfiguration.getInstance().addChest(src, name, loc);
						}
						return CommandResult.success();
					} else {
						src.sendMessage(TextLibs.pluginError("You must set the coords of a chest in console"));
						return CommandResult.builder().successCount(0).build();
					}
				}
			}else {
				if(args.hasAny("id")){
					int id = args.<Integer>getOne("id").get();
					SetupConfiguration.getInstance().removeChest(src, name, id);
				}	
			}
		}
		return CommandResult.success();
	}

}
