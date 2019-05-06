package com.github.elrol.dropparty.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyEvent {
	private Title dropPartyAnnouncement;
	protected String name;
	
	Task timeRemainingLoop;
	Task dropParty;
	
	private int timeLeft;
	protected int duration;
	private Countdown cd;
	private Random rand = new Random();
	private Player initiator;
	
    private ServerBossBar bar;
	
	protected DropPartyEvent(Player player, String name, int itemsPerSec, boolean isAdmin) {
		this.name = name;
		this.initiator = player;
		bar = ServerBossBar.builder().color(BossBarColors.PURPLE).overlay(BossBarOverlays.PROGRESS).name(Text.of(TextColors.LIGHT_PURPLE, "DropParty '" + name + "' has started! Use '/dp tp " + name + "' to join!")).build();
		this.duration = Math.round(((float)SetupConfiguration.getInstance().getPartyItemQty(name) / (float)itemsPerSec) + 0.5F);
		//TextLibs.sendMessage(player, "Duration: " + SetupConfiguration.getInstance().getPartyItemQty(name) + "/" + itemsPerSec + " = " + duration);
		List<Inventory> chests = new ArrayList<Inventory>();
		String initName = "Console";
		if(this.initiator != null)
			initName = this.initiator.getName();
		dropPartyAnnouncement = Title.builder().title(Text.of(TextColors.BLUE, "Starting DropParty '" + name + "'"))
				.subtitle(Text.of(TextColors.AQUA, "Hosted by: " + initName)).stay(100).build();
		
		if(!isAdmin) {
			cd = new Countdown(() -> endEvent(), duration, bar, null, Main.getInstance());
		} else {
			bar = ServerBossBar.builder().color(BossBarColors.YELLOW).overlay(BossBarOverlays.PROGRESS).name(Text.of(TextColors.GOLD, "DropParty '" + name + "' has started! Use '/dp tp " + name + "' to join!")).build();
			cd = new Countdown(() -> endEvent(), -1, bar, null, Main.getInstance());
		}
		Methods.broadcastTitle(dropPartyAnnouncement);
		Sponge.getServer().getBroadcastChannel().send(Text.of(TextLibs.pluginHeader(), TextColors.GREEN, "DropParty '" + name + "' has started! Use '/dp tp " + name + "' to join!"));
		
		timeLeft = duration;
		
		timeRemainingLoop = Sponge.getScheduler().createTaskBuilder().execute(() -> {
			if(timeLeft > 0) {
				remainingTime(timeLeft);
				timeLeft -= 120;
 			} else {
 				timeRemainingLoop.cancel();
 			}
		}).delay(5, TimeUnit.MINUTES).interval(2, TimeUnit.MINUTES).submit(Main.getInstance());
		
		TextLibs.sendConsoleMessage("Starting the party");
		winnerEffects();
		
		for(Location<World> loc : SetupConfiguration.getInstance().getChests(name)){
			Inventory chest = Methods.getCarrier(loc).getInventory();
			if(chest == null) {
				continue;
			}
			chests.add(chest);
		}
		dropParty = Sponge.getScheduler().createTaskBuilder().execute(() -> {
			for(int i = 0; i < itemsPerSec; i++) {
				List<List<ItemStack>> tierItems = Methods.getTierItemsFromParty(chests);
				//TextLibs.sendConsoleMessage("trying to drop item " + i);
				int rng = rand.nextInt(DefaultConfiguration.getInstance().getTotalChance());
				int tier = 0;
				//TextLibs.sendConsoleMessage("Random Number Gen is: " + rng + "/" + DefaultConfiguration.getInstance().getTotalChance());
				for(int m = 0; m < 6 ; m++) {
					if(rng < DefaultConfiguration.getInstance().getTierRange(m)) {
						//TextLibs.sendConsoleMessage("Tier is equal to: " + m);
						tier = m;
						break;
					}
				}
				
				while(tierItems.get(tier).isEmpty()) {
					//TextLibs.sendConsoleMessage("Tier is: " + tier);
					if(tier >= 5 && !Methods.hasLowerTiers(tierItems, 5)) {
						TextLibs.sendConsoleMessage("All items gone, ending party");
						endEvent();
						return;
					}
					if(Methods.hasLowerTiers(tierItems, tier)) {
						TextLibs.sendConsoleMessage("Items in lower tier found");
						tier--;
					} else {
						TextLibs.sendConsoleMessage("Items not found in lower tier, going higher tier");
						if(tier < 5)
							tier++;
					}
				}
				
				for(Inventory chest : chests) {
					if(chest.size() == 0)
						continue;
					List<ItemStack> items = tierItems.get(tier);
					
					if(items.isEmpty() || items == null)
						TextLibs.sendConsoleMessage("Item List is empty, this is a bug!");
					int randNum;
					if(items.size() < 1) {
						randNum = 0;
					} else {
						randNum = rand.nextInt(items.size());
					}
					if(items.size() == 0) {
						continue;
					}
					ItemStack randomStack = items.get(randNum);
					//TextLibs.sendConsoleMessage(randomStack.getType().getName());
					for(Inventory slot : chest.slots()) {
						if(slot.size() == 1 && randomStack.getType().equals(slot.peek().get().getType()) && ItemStackComparators.ITEM_DATA.compare(slot.peek().get(), randomStack) == 0) {
							ItemStack stack;
							if(isAdmin) {
								stack = slot.peek(1).get();
							} else {
								stack = slot.poll(1).get();
								if(slot.size() == 0) {
									List<ItemStack> temp = tierItems.get(tier);
									temp.remove(stack);
									tierItems.set(tier, temp);
								}
							}
							//System.out.print("Test");
							SetupConfiguration.getInstance().spawnItemAtDrop(name, stack);
							break;
						}
					}
				}
			}
		}).interval(1, TimeUnit.SECONDS).submit(Main.getInstance());
		//new DropPartyRunnable(name, itemsPerSec)
		TextLibs.sendConsoleMessage("Starting DropParty '" + name + "'");
	}
	
	private void remainingTime(long time) {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.AQUA, "There are ", Math.round((float)time/60.0F), " minutes left of the '" + name + "' DropParty"));
		}
	}
	
	private void endingParty(String name) {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.AQUA, "DropParty ", name, " has ended. Thanks for playing!"));
		}
	}
	
	public void endEvent() {
		endingParty(name);
		Main.getInstance().getLogger().debug("DropParty Ending");
		if(cd != null)
			cd.cancel();
		if(timeRemainingLoop != null)
			timeRemainingLoop.cancel();
		if(dropParty != null)
			dropParty.cancel();
		if(bar != null)
			bar.removePlayers(bar.getPlayers());
		winnerEffects();
		Main.getInstance().getEventManager().isRunning = false;
	}
	
	protected void winnerEffects() {
		List<Location<World>> drops = SetupConfiguration.getInstance().getDrops(name);
		for(Location<World> drop : drops) {
			for(int y = drop.getBlockY(); y > 0; y--) {
				if(!drop.getBlock().getType().equals(BlockTypes.AIR)) {
					Location<World> loc = new Location<World>(drop.getExtent(), drop.getX() + 0.5D, y + 1.5D, drop.getZ() + 0.5D);
					Task firework1 = Sponge.getScheduler().createTaskBuilder().execute(() -> {
						Methods.spawnFirework(loc);
			        }).delay(1, TimeUnit.SECONDS).interval(750, TimeUnit.MILLISECONDS).submit(Main.getInstance());
					Sponge.getScheduler().createTaskBuilder().execute(() -> {
			            firework1.cancel();
			        }).delay(3, TimeUnit.SECONDS).submit(Main.getInstance());
					loc.getExtent().playSound(SoundTypes.ENTITY_PLAYER_LEVELUP, drop.getPosition(), 1);
			        break;
				}
			}
		}
	}
	
	public String getPartyName() {
		return this.name;
	}
	
	public List<Location<World>> getChests() {
		List<Location<World>> chests = new ArrayList<Location<World>>();
		for(Location<World> pos : SetupConfiguration.getInstance().getChests(name))
			chests.add(pos);
		return chests;
	}
	
	public Player getInitiator() {
		return this.initiator;
	}
}
