package com.github.elrol.dropparty.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.config.TierConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyRunnable implements Runnable{

	private String name;
	private int itemsPerSec;
	
	public DropPartyRunnable(String name, int itemsPerSec) {
		this.name = name;
		this.itemsPerSec = itemsPerSec;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < itemsPerSec; i++) {
			TextLibs.sendConsoleMessage("trying to drop item " + i);
			Random rand = new Random();
			int selector = rand.nextInt(DefaultConfiguration.getInstance().getTotalChance());
			int[] range = DefaultConfiguration.getInstance().getRange();
			int tier;
			
			Map<ExtendedBlockPos, Integer> itemLocation = new HashMap<ExtendedBlockPos, Integer>();
			if(selector < range[0])
				tier = 0;
			else if(selector < range[1])
				tier = 1;
			else if(selector < range[2])
				tier = 2;
			else if(selector < range[3])
				tier = 3;
			else if(selector < range[4])
				tier = 4;
			else
				tier = 5;
			
			List<ExtendedBlockPos> chests = SetupConfiguration.getInstance().getChests(name);
			while(itemLocation.isEmpty())
				Main.getInstance().getLogger().debug("Running item check for tier " + tier);
				for(ExtendedBlockPos chest : chests) {
					Iterable<Inventory> slots = Methods.getCarrier(chest).getInventory().slots();
					int slotId = 0;
					for(Inventory slot : slots) {
						Optional<ItemStack> opStack = slot.peek();
						  if(!opStack.isPresent()){
						    continue;
						  }
						  ItemStack stack = opStack.get();
						  if(TierConfiguration.getInstance().getTier(tier).contains(stack)) {
							  System.out.println("Adding Item: " + stack.getType().getTranslation().get() + "to the list");
							  itemLocation.put(chest, slotId);
						  }
						  slotId += 1;
					}
				}
				if(tier == 0 && itemLocation.isEmpty()) {
					Main.getInstance().getEventManager().stopScheduledEvent();
				} else {
					tier--;
				}
				
			int validItems = itemLocation.size();
			int selectedItemLocation = rand.nextInt(validItems);
			ExtendedBlockPos chestLocation = (ExtendedBlockPos) itemLocation.keySet().toArray()[selectedItemLocation];
			int slotId = (int) itemLocation.values().toArray()[selectedItemLocation];
			Iterable<Inventory> finalChest = Methods.getCarrier(chestLocation).getInventory().slots();
			int slot = 0;
			for(Inventory slotInv : finalChest) {
				System.out.println("Attempting to spawn Item");
				if(slot < slotId)
					continue;
				ItemStack stack = slotInv.peek().get();
				SetupConfiguration.getInstance().spawnItemAtDrop(name, stack);
				if(stack.getQuantity() > 1)
					stack.setQuantity(stack.getQuantity() - 1);
				else
					stack = ItemStack.empty();
				slotInv.set(stack);
			}
		}
	}

}
