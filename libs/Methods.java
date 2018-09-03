package com.github.elrol.dropparty.libs;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

public class Methods {
	
	public static List<BlockPos> getChest(Player player) {
		List<BlockPos> chests = new ArrayList<BlockPos>();
		String world = player.getWorld().getName();
		int originX = player.getLocation().getBlockX();
		int originY = player.getLocation().getBlockY();
		int originZ = player.getLocation().getBlockZ();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 2; j++) {
				for(int k = -1; k <= 1; k++) {
					BlockState block = player.getWorld().getBlock(originX + i, originY + j, originZ + k);
					if(block.getType().equals(BlockTypes.CHEST)) {
						chests.add(new BlockPos(originX + i, originY + j, originZ + k, world));
					}
				}	
			}	
		}
		if(chests.isEmpty())
			player.sendMessage(TextLibs.pluginError("No chests nearby found"));
		return chests;
	}
	
	public static boolean isWorldNameValid(String dimName) {
		for(World world : Sponge.getServer().getWorlds()) {
			if(world.getName().equalsIgnoreCase(dimName))
				return true;
		}
		return false;
	}

	public static String getWorldName(CommandContext args) {
		String worldName;
		if(args.hasAny("dim") && Methods.isWorldNameValid(args.<String>getOne("dim").get()))
			worldName = args.<String>getOne("dim").get();
		else
			worldName = Sponge.getServer().getDefaultWorldName();
		return worldName;
	}
	
	public static BlockPos getBlockPos(Player player) {
		int x = player.getLocation().getBlockX();
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ();
		String dim = player.getWorld().getName();
		return new BlockPos(x, y, z, dim);
	}
	
}
