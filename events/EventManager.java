package com.github.elrol.dropparty.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.libs.TextLibs;

public class EventManager {

	private DropPartyEvent currentEvent;
	
	public boolean isRunning;
	private Countdown scheduledEvent;
	
	private String partyName;
	
	private ServerBossBar nextEventBar = ServerBossBar.builder().color(BossBarColors.BLUE)
			.name(Text.of(TextColors.BLUE, "DropParty starting at " + partyName)).overlay(BossBarOverlays.PROGRESS).build();
	
	public EventManager() {
		this.isRunning = false;
	}
	
	public void startParty(String name, int itemsPerSec) {
		if(isRunning) {
			TextLibs.sendConsoleMessage("A DropParty is already running, please wait until it has finished");
			return;
		}
		partyName = name;
		if(Sponge.getServer().getOnlinePlayers().size() > 0) {
			currentEvent = new DropPartyEvent(partyName, itemsPerSec);
		}
	}
	
	public DropPartyEvent getCurrentParty() {
		return currentEvent;
	}
	
	public void scheduleEvent(String name, Title title, int delay, int itemsPerSec) {
		scheduledEvent = new Countdown(() -> startParty(name, itemsPerSec), delay * 60, nextEventBar, title, Main.getInstance());
	}
	
	public boolean stopScheduledEvent() {
		boolean running = false;
		//TODO Null being called 
		if(scheduledEvent != null)
			scheduledEvent.cancel();
		if(getCurrentParty() != null)
			getCurrentParty().endEvent();
		return running;
	}
}
