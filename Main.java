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

import com.github.elrol.dropparty.commands.CommandRegistry;
import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.TierConfiguration;
import com.github.elrol.dropparty.libs.PluginInfo;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESC)
public class Main {

	private static Main instance;
	
	@Inject
	private Logger logger;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private File defaultConfig;
	private File tierConfig = new File("config/" + PluginInfo.ID + "/tiers.conf");
	private File dropConfig = new File("config/" + PluginInfo.ID + "/drops.conf");
	
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	private ConfigurationLoader<CommentedConfigurationNode> tierManager = HoconConfigurationLoader.builder().setFile(tierConfig).build();
	private ConfigurationLoader<CommentedConfigurationNode> dropManager = HoconConfigurationLoader.builder().setFile(dropConfig).build();
	
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
		logger.info("Registering Configs");
		DefaultConfiguration.getInstance().setup(defaultConfig, configManager);
		TierConfiguration.getInstance().setup(tierConfig, tierManager);
		DropConfiguration.getInstance().setup(dropConfig, dropManager);
		
		CommandRegistry.setup(this);
	}
	
	@Listener
	public void postInit(GamePostInitializationEvent event){
		instance = this;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public Logger getLogger() {
		return logger;
	}
}
