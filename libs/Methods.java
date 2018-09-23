package com.github.elrol.dropparty.libs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.FireworkShape;
import org.spongepowered.api.item.FireworkShapes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.config.TierConfiguration;
import com.google.common.collect.Lists;

public class Methods {
	
	public static List<ExtendedBlockPos> getChest(Player player) {
		List<ExtendedBlockPos> chests = new ArrayList<ExtendedBlockPos>();
		String world = player.getWorld().getName();
		int originX = player.getLocation().getBlockX();
		int originY = player.getLocation().getBlockY();
		int originZ = player.getLocation().getBlockZ();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 2; j++) {
				for(int k = -1; k <= 1; k++) {
					BlockState block = player.getWorld().getBlock(originX + i, originY + j, originZ + k);
					if(block.getType().equals(BlockTypes.CHEST)) {
						chests.add(new ExtendedBlockPos(originX + i, originY + j, originZ + k, world));
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
	 
	public static ExtendedBlockPos getBlockPos(Player player) {
		int x = player.getLocation().getBlockX();
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ();
		String dim = player.getWorld().getName();
		return new ExtendedBlockPos(x, y, z, dim);
	}
	
	public static Map<String, String> getPartyNames(){
		 Map<String, String> partyNames = new LinkedHashMap<String, String>();
		 for(String party: SetupConfiguration.getInstance().getParties()) {
			 partyNames.put(party, party);
		 }
		 return partyNames;
	}
	
	public static void broadcastTitle(Title title) {
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			p.sendTitle(title);
		}
	}

	public static void broadcastBossBar(ServerBossBar bar) {
		for (Player p : Sponge.getServer().getOnlinePlayers()) {
			bar.addPlayer(p);
		}
	}
	
	public static TileEntityCarrier getCarrier(ExtendedBlockPos pos) {
		World world = Sponge.getServer().getWorld(pos.getDim()).get();
		if(!world.getBlock(pos.getX(), pos.getY(), pos.getZ()).getType().equals(BlockTypes.CHEST)) {
			TextLibs.pluginError("Chest at X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ() + " is not found, skipping it.");
			return null;
		}
		return (TileEntityCarrier)world.getTileEntity(pos.getX(), pos.getY(), pos.getZ()).get();
		
	}
	
	public static boolean doesPartyHaveItems(String name) {
		List<ExtendedBlockPos> chests = SetupConfiguration.getInstance().getChests(name);
		for(ExtendedBlockPos chest : chests) {
			for(Inventory slot : Methods.getCarrier(chest).getInventory().slots()){
				if(slot.size() == 1)
					return true;
			}
		}
		return false;
	}
	
	public static List<ItemStack> getListFromTier(Inventory chests, int tier){
		List<ItemStack> validOptions = new ArrayList<ItemStack>();
		for(Inventory slot : chests.slots()) {
			Optional<ItemStack> opStack = slot.peek();
			  if(!opStack.isPresent())
			    continue;
			  ItemStack stack = opStack.get();
			  if(TierConfiguration.getInstance().isItemListed(tier, stack)){
			    continue;
			  }
			  validOptions.add(stack);
		}
		return validOptions;
	}
	
	private static final FireworkShape[] fireworkShapes = new FireworkShape[] {FireworkShapes.BALL, FireworkShapes.BURST, FireworkShapes.CREEPER, FireworkShapes.LARGE_BALL, FireworkShapes.STAR};
	
	public static void spawnFirework(Location<World> location) {
		Random rand = new Random();
		FireworkEffect effect = FireworkEffect.builder().color(Color.ofRgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255))).flicker(rand.nextBoolean()).shape(fireworkShapes[rand.nextInt(fireworkShapes.length)]).trail(rand.nextBoolean()).build();
		Entity firework = location.getExtent().createEntity(EntityTypes.FIREWORK, location.getPosition());
	    firework.offer(Keys.FIREWORK_EFFECTS, Lists.newArrayList(effect));
	    location.getExtent().spawnEntity(firework);
	}
}
