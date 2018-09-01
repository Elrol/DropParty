package com.github.elrol.dropparty;

import java.io.File;

import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.libs.PluginInfo;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESC)
public class Main {

	@Inject
	public Main instance;
	
	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = false)
	private File configuration;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info("Started DropParty");
	}
	
	@Listener
	public void onServerStop(GameStoppedServerEvent event) {
		logger.info("Stopping DropParty");
	}
	
	@Listener
	public void preInit(GamePreInitializationEvent event){
		
	}
	
	@Listener
	public void init(GameInitializationEvent event){
		DefaultConfiguration.getInstance().setup(configuration, configManager);
	}
	
	@Listener
	public void postInit(GamePostInitializationEvent event){
		
	}
}
