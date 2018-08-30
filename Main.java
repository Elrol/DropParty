package com.github.elrol.dropparty;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.format.TextColors;

import com.github.elrol.dropparty.libs.PluginInfo;
import com.google.inject.Inject;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESC)
public class Main {

	@Inject
	private Logger logger;
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info(TextColors.WHITE + "Started " + TextColors.BLUE + "DropParty" + TextColors.WHITE);
	}
	
	@Listener
	public void onServerStop(GameStoppedServerEvent event) {
		logger.info(TextColors.WHITE + "Stopping " + TextColors.BLUE + "DropParty" + TextColors.WHITE);
	}
	
	@Listener
	public void preInit(GamePreInitializationEvent event){
		
	}
	
	@Listener
	public void init(GameInitializationEvent event){
		
	}
	
	@Listener
	public void postInit(GamePostInitializationEvent event){
		
	}
}
