package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.libs.BlockPos;
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
				List<BlockPos> chests = DropConfiguration.getInstance().getChests(name);
				if(!chests.isEmpty()) {
					for(int i = 0; i < chests.size(); i++) {
						src.sendMessage(Text.of("[" + i + "] X:" + chests.get(i).getX() + ", Y:" + chests.get(i).getY() + ", Z:" + chests.get(i).getZ()));
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
					int id = DropConfiguration.getInstance().getChestId(name);
					src.sendMessage(TextLibs.pluginMessage("set chest " + id + " for Party " + name));
					DropConfiguration.getInstance().addChest(src, name, id, new BlockPos(x,y,z));
					return CommandResult.success();
				}
				if(src instanceof Player) {
					Player player = (Player)src;
					List<BlockPos> chests = Methods.getChest(player);
					for(BlockPos pos : chests) {
						int id = DropConfiguration.getInstance().getChestId(name);
						
					}
					return CommandResult.success();
				} else {
					src.sendMessage(TextLibs.pluginError("You must set the coords of a chest in console"));
				}
			}else {
				
			}
		}
		return CommandResult.success();
	}

}
