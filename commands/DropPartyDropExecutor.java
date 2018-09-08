package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyDropExecutor implements CommandExecutor {
	
	private int mode;
	
	/**Int mode
	 * 0: list, 1: add, 2: remove
	 * */
	public DropPartyDropExecutor(int mode) {
		this.mode = mode;
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			if(mode == 0) {
				src.sendMessage(TextLibs.pluginMessage("List of drops for " + name));
				List<ExtendedBlockPos> drops = SetupConfiguration.getInstance().getDrops(name);
				if(!drops.isEmpty()) {
					for(int i = 0; i < drops.size(); i++) {
						src.sendMessage(Text.of("[" + i + "] X:" + drops.get(i).getX() + ", Y:" + drops.get(i).getY() + ", Z:" + drops.get(i).getZ() + "(" + drops.get(i).getDim() + ")"));
					}
				} else {
					src.sendMessage(Text.of("No drops found"));
				}
				return CommandResult.success();
			}else if(mode == 1) {
				if(args.hasAny("x") && args.hasAny("y") && args.hasAny("z")) {
					int x = args.<Integer>getOne("x").get();
					int y = args.<Integer>getOne("y").get();
					int z = args.<Integer>getOne("z").get();
					String worldName = Methods.getWorldName(args);
					int id = SetupConfiguration.getInstance().getDropId(name);
					src.sendMessage(TextLibs.pluginMessage("set drop " + id + " for Party " + name));
					SetupConfiguration.getInstance().addDrop(src, name, new ExtendedBlockPos(x,y,z, worldName));
					return CommandResult.success();
				} else {
					if(src instanceof Player) {
						Player player = (Player)src;
						SetupConfiguration.getInstance().addDrop(src, name, Methods.getBlockPos(player));
						return CommandResult.success();
					} else {
						src.sendMessage(TextLibs.pluginError("You must set the coords of a drop while in console"));
						return CommandResult.builder().successCount(0).build();
					}
				}
			}else {
				if(args.hasAny("id")){
					int id = args.<Integer>getOne("id").get();
					SetupConfiguration.getInstance().removeDrop(src, name, id);
				}	
			}
		}
		return CommandResult.success();
	}

}
