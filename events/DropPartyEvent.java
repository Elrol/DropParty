package com.github.elrol.dropparty.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.FireworkShapes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.ExtendedBlockPos;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyEvent {
	private Title dropPartyAnnouncement;
	protected String name;
	
	List<FireworkEffect> fireworks = new ArrayList<FireworkEffect>();
	
	Task timeRemainingLoop;
	Task dropParty;
	
	private int timeLeft;
	protected int duration;
	private Countdown cd;
	private Random rand = new Random();

    private ServerBossBar bar;
	
	protected DropPartyEvent(String name, int itemsPerSec) {
		this.name = name;
		bar = ServerBossBar.builder().color(BossBarColors.PURPLE).overlay(BossBarOverlays.PROGRESS).name(Text.of(TextColors.LIGHT_PURPLE, "DropParty '" + name + "'")).build();
		this.duration = SetupConfiguration.getInstance().getDuration(name, itemsPerSec);
		Main.getInstance().getEventManager().isRunning = true;
		
		dropPartyAnnouncement = Title.builder().title(Text.of(TextColors.BLUE, "DropParty '" + name + "'"))
				.subtitle(Text.of(TextColors.AQUA, "Starting Now")).stay(100).build();
		
		fireworks.add(Sponge.getRegistry().createBuilder(FireworkEffect.Builder.class)
				.color(org.spongepowered.api.util.Color.BLUE).shape(FireworkShapes.STAR).build());
		
		
		//Main.getInstance().EVENT_BUS.register(this);
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
		dropParty = Sponge.getScheduler().createTaskBuilder().execute(() -> {
			for(int i = 0; i < itemsPerSec; i++) {
				System.out.println("#" + i + " in a second");
				TextLibs.sendConsoleMessage("trying to drop item");
				int tier = 0;
				int range = rand.nextInt(DefaultConfiguration.getInstance().getTotalChance());
				Inventory chests = null;
				for(int j = 5; j > 0; j--) {
					if(DefaultConfiguration.getInstance().getRange()[j] >= range 
							&& (j != 0 ? DefaultConfiguration.getInstance().getRange()[j-1] < range : true)) {
						tier = j;
						System.out.println("Tier " + tier + " range is " + range);
						break;
					}
				}
				for(ExtendedBlockPos pos : SetupConfiguration.getInstance().getChests(name)){
					Inventory chest = Methods.getCarrier(pos).getInventory();
					if(chests == null) {
						chests = chest;
					} else {
						chests.union(chest);
					}
				}
				List<ItemStack> validOptions = new ArrayList<ItemStack>();
				while(validOptions.isEmpty()) {
					validOptions = Methods.getListFromTier(chests, tier);
					if(validOptions.isEmpty() && tier > 0) {
						System.out.println("Tier " + tier + " Items not found, trying Tier " + (tier -1));
						tier--;
					} else if (validOptions.isEmpty() && tier == 0) {
						for(int k = 1; k < 5; k++) {
							if(!Methods.getListFromTier(chests, k).isEmpty()) {
								validOptions = Methods.getListFromTier(chests, k);
								break;
							}
							if(k == 5 && Methods.getListFromTier(chests, k).isEmpty()) {
								System.out.print("Chests are empty, ending party");
								endEvent();
							}
						}
					}
				}
				ItemStack randomStack = validOptions.get(rand.nextInt(validOptions.size()-1));
				for(Inventory slot : chests.slots()) {
					if(slot.contains(randomStack)) {
						SetupConfiguration.getInstance().spawnItemAtDrop(name, slot.poll(1).get());
						System.out.println("Dropping item at drop");
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
	
	public void endEvent() {
		cd.cancel();
		timeRemainingLoop.cancel();
		dropParty.cancel();
		Main.getInstance().getEventManager().isRunning = false;
	}
	
	protected void winnerEffects(Player p) {
		ParticleType fw = ParticleTypes.FIREWORKS;

        ParticleType heart = ParticleTypes.HEART;



        Task firework1 = Sponge.getScheduler().createTaskBuilder().execute(() -> {

            p.spawnParticles(

                    Sponge.getRegistry().createBuilder(ParticleEffect.Builder.class).type(fw)

                            .option(ParticleOptions.FIREWORK_EFFECTS, fireworks).quantity(5).build(),

                    p.getLocation().getPosition().add(0, 1, 0), 50);

        }).delay(1, TimeUnit.SECONDS).interval(750, TimeUnit.MILLISECONDS).submit(Main.getInstance());



        Task firework2 = Sponge.getScheduler().createTaskBuilder().execute(() -> {

            p.spawnParticles(

                    Sponge.getRegistry().createBuilder(ParticleEffect.Builder.class).type(heart).quantity(8).build(),

                    p.getLocation().getPosition().add(0, 1, 0), 50);

        }).delay(2, TimeUnit.SECONDS).interval(750, TimeUnit.MILLISECONDS).submit(Main.getInstance());



        Sponge.getScheduler().createTaskBuilder().execute(() -> {

            firework1.cancel();

            firework2.cancel();

        }).delay(7, TimeUnit.SECONDS).submit(Main.getInstance());



        p.playSound(SoundTypes.ENTITY_PLAYER_LEVELUP, p.getLocation().getPosition(), 1);
	}
	
	protected Task dropPartyTask() {
		Task dropPartyTask = Sponge.getScheduler().createTaskBuilder().execute(name -> {
			
		}).interval(1, TimeUnit.SECONDS).submit(Main.getInstance());
		
		return dropPartyTask;
	}
}
