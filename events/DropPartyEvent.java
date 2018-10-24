package com.github.elrol.dropparty.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.World;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
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

    private ServerBossBar bar;
	
	protected DropPartyEvent(String name, int itemsPerSec, boolean isAdmin) {
		this.name = name;
		bar = ServerBossBar.builder().color(BossBarColors.PURPLE).overlay(BossBarOverlays.PROGRESS).name(Text.of(TextColors.LIGHT_PURPLE, "DropParty '" + name + "'")).build();
		this.duration = Math.round((float)SetupConfiguration.getInstance().getPartyItemQty(name) / (float)itemsPerSec);
		Main.getInstance().getEventManager().isRunning = true;
		
		dropPartyAnnouncement = Title.builder().title(Text.of(TextColors.BLUE, "DropParty '" + name + "'"))
				.subtitle(Text.of(TextColors.AQUA, "Starting Now")).stay(100).build();
		
		if(!isAdmin)
			cd = new Countdown(() -> endEvent(), duration, bar, null, Main.getInstance());
		
		Methods.broadcastTitle(dropPartyAnnouncement);
		Sponge.getServer().getBroadcastChannel().send(Text.of(TextLibs.pluginHeader(), TextColors.GREEN, "DropParty '" + name + "' has started!"));
		
		timeLeft = duration - 5;
		
		timeRemainingLoop = Sponge.getScheduler().createTaskBuilder().execute(() -> {
			if(timeLeft >0) {
				remainingTime(timeLeft);
				timeLeft -= 2;
 			} else {
 				timeRemainingLoop.cancel();
 			}
		}).delay(5, TimeUnit.MINUTES).interval(2, TimeUnit.MINUTES).submit(Main.getInstance());
		
		TextLibs.sendConsoleMessage("Starting the party");
		winnerEffects();
		dropParty = Sponge.getScheduler().createTaskBuilder().execute(() -> {
			for(int i = 0; i < itemsPerSec; i++) {
				//System.out.println("#" + i + " in a second");
				//TextLibs.sendConsoleMessage("trying to drop item");
				int tier = 0;
				int range = rand.nextInt(DefaultConfiguration.getInstance().getTotalChance());
				List<Inventory> chests = new ArrayList<Inventory>();
				for(int j = 5; j > 0; j--) {
					if(DefaultConfiguration.getInstance().getRange()[j] >= range && (j != 0 ? DefaultConfiguration.getInstance().getRange()[j-1] +1 < range : true)) {
						tier = j;
						//System.out.println("Tier " + tier + ", RNG is " + range + ", Range is [" + (tier == 0 ? "0" : DefaultConfiguration.getInstance().getRange()[j-1] + 1) + "-" + DefaultConfiguration.getInstance().getRange()[j] + "]");
						break;
					}
				}
				for(ExtendedBlockPos pos : SetupConfiguration.getInstance().getChests(name)){
					Inventory chest = Methods.getCarrier(pos).getInventory();
					chests.add(chest);
				}
				List<ItemStack> validOptions = new ArrayList<ItemStack>();
				while(validOptions.isEmpty()) {
					for(Inventory chest : chests) {
						if(chest.size() == 0)
							continue;
						validOptions.addAll(Methods.getListFromTier(chest, tier));
					}
					if(validOptions.isEmpty() && tier > 0) {
						//System.out.println("Tier " + tier + " Items not found, trying Tier " + (tier -1));
						tier--;
					}
				}
				//System.out.println("There are " + validOptions.size() + " options");
				for(Inventory chest : chests) {
					if(validOptions.isEmpty())
						endEvent();
					ItemStack randomStack = validOptions.get(rand.nextInt(validOptions.size()));
					if(chest.size() == 0)
						continue;
					for(Inventory slot : chest.slots()) {
						if(slot.size() == 1 && slot.contains(randomStack)) {
							ItemStack stack;
							if(isAdmin)
								stack = slot.peek(1).get();
							else
								stack = slot.poll(1).get();
							SetupConfiguration.getInstance().spawnItemAtDrop(name, stack);
						}
					}
				}
			}
		}
			).interval(1, TimeUnit.SECONDS).submit(Main.getInstance());
		//new DropPartyRunnable(name, itemsPerSec)
		Main.getInstance().getLogger().info("Starting DropParty '" + name + "'");
	}
	
	private void remainingTime(long time) {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.AQUA, "There are ", Math.round(time), " minutes left of the '" + name + "' DropParty"));
		}
	}
	
	private void endingParty(String name) {
		for(Player player : Sponge.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.AQUA, "DropParty ", name, " has ended. Thanks for playing!"));
		}
	}
	
	public void endEvent() {
		endingParty(name);
		cd.cancel();
		timeRemainingLoop.cancel();
		dropParty.cancel();
		winnerEffects();
		Main.getInstance().getEventManager().isRunning = false;
	}
	
	protected void winnerEffects() {
		List<ExtendedBlockPos> drops = SetupConfiguration.getInstance().getDrops(name);
		for(ExtendedBlockPos drop : drops) {
			Task firework1 = Sponge.getScheduler().createTaskBuilder().execute(() -> {
				Methods.spawnFirework(drop.getLocation());
	        }).delay(1, TimeUnit.SECONDS).interval(750, TimeUnit.MILLISECONDS).submit(Main.getInstance());
	
	        Sponge.getScheduler().createTaskBuilder().execute(() -> {
	            firework1.cancel();
	        }).delay(7, TimeUnit.SECONDS).submit(Main.getInstance());
			World world = Sponge.getServer().getWorld(drop.getDim()).get();
	        world.playSound(SoundTypes.ENTITY_PLAYER_LEVELUP, drop.getLocation().getPosition(), 1);
		}
	}
}
