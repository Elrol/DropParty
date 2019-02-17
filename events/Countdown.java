package com.github.elrol.dropparty.events;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.title.Title;

import com.github.elrol.dropparty.Main;

public class Countdown {
	
	private int time;
	private Task titleTask;
	private Task secIntervalTask;
	private ServerBossBar bar;
	private Title title;
	
	public Countdown(Runnable runnable, int length, ServerBossBar bossBar, Title title, Main plugin) {
		plugin.getLogger().debug("Countdown");
		bar = bossBar;
		bar.setPercent(1.0F);
		bar.addPlayers(Sponge.getServer().getOnlinePlayers());
		time = length;
		
		if(title != null) {
			this.title = title;
			titleTask = Sponge.getScheduler().createTaskBuilder().delay((long) (length * 0.9), TimeUnit.SECONDS).execute(() -> {
				for(Player p : Sponge.getServer().getOnlinePlayers()) {
					p.sendTitle(title);
				}
			}).submit(plugin);
		}
		
		secIntervalTask = Sponge.getScheduler().createTaskBuilder().interval(1, TimeUnit.SECONDS).execute(() -> {
			if(time > 0) {
				time--;
				float percent = ((float)time / (float) length);
				bar.setPercent(percent).removePlayers(bar.getPlayers()).addPlayers(Sponge.getServer().getOnlinePlayers());
			} else {
				if(title != null) {
					titleTask.cancel();
				}
				bar.removePlayers(bar.getPlayers());
				runnable.run();
				secIntervalTask.cancel();
			}
		}).submit(plugin);
	}
	
	public void cancel() {
		bar.removePlayers(bar.getPlayers());
		secIntervalTask.cancel();
		if(title != null)
			titleTask.cancel();
	}
}
