package com.github.elrol.dropparty.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.libs.TextLibs;

public class EventManager {

	private DropPartyEvent currentEvent;
	
	public boolean isRunning;
	private Countdown scheduledEvent;
	private Player initiator;
	private String partyName;
	
	public EventManager() {
		this.isRunning = false;
	}
	
	public void startParty(Player player, String name, int itemsPerSec, boolean isAdmin) {
		partyName = name;
		if(Sponge.getServer().getOnlinePlayers().size() >= 0) {
			currentEvent = new DropPartyEvent(player, partyName, itemsPerSec, isAdmin);
		} else {
			TextLibs.sendError(Sponge.getServer().getConsole(), "There are no players online, and you need people for a party");
		}
	}
	
	public DropPartyEvent getCurrentParty() {
		return currentEvent;
	}
	
	public Player getInitiator() {
		return this.initiator;
	}
	
	protected ServerBossBar nextEventBar(String name){
		return ServerBossBar.builder().color(BossBarColors.BLUE)
			.name(Text.of(TextColors.BLUE, "DropParty starting at " + name)).overlay(BossBarOverlays.PROGRESS).build();
	}
	
	public void scheduleEvent(Player player, String name, Title title, int delay, int itemsPerSec, boolean isAdmin) {
		if(isRunning) {
			TextLibs.sendConsoleMessage("A DropParty is already running, please wait until it has finished");
			return;
		}
		scheduledEvent = new Countdown(() -> startParty(player, name, itemsPerSec, isAdmin), delay * 60, nextEventBar(name), title, Main.getInstance());
		isRunning = true;
		initiator = player;
	}
	
	public void stopScheduledEvent() {
		isRunning = false;
		if(scheduledEvent != null) {
			scheduledEvent.cancel();
		}
		if(getCurrentParty() != null) {
			getCurrentParty().endEvent();
		}
	}
}
