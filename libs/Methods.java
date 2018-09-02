package com.github.elrol.dropparty.libs;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;

public class Methods {
	
	public static List<BlockPos> getChest(Player player) {
		List<BlockPos> chests = new ArrayList<BlockPos>();
		int originX = player.getLocation().getBlockX();
		int originY = player.getLocation().getBlockY();
		int originZ = player.getLocation().getBlockZ();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 2; j++) {
				for(int k = -1; k <= 1; k++) {
					BlockState block = player.getWorld().getBlock(originX + i, originY + j, originZ + k);
					if(block.getType().equals(BlockTypes.CHEST)) {
						chests.add(new BlockPos(originX + i, originY + j, originZ + k));
					}
				}	
			}	
		}
		return chests;
	}
	
}
