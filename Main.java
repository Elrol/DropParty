package com.github.elrol.dropparty;

import java.io.File;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

import com.github.elrol.dropparty.commands.CommandRegistry;
import com.github.elrol.dropparty.config.DefaultConfiguration;
import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.config.TierConfiguration;
import com.github.elrol.dropparty.events.EventManager;
import com.github.elrol.dropparty.libs.PluginInfo;
import com.google.inject.Inject;

import net.minecraftforge.fml.common.eventhandler.EventBus;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESC)
public class Main {

	private static Main instance;
	public EventBus EVENT_BUS = new EventBus();
	
	private Logger logger;
	
	private File defaultConfig;
	private File tierConfig;
	private File setupConfig; 
	private File dropConfig; 
	
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	private ConfigurationLoader<CommentedConfigurationNode> tierManager;
	private ConfigurationLoader<CommentedConfigurationNode> setupManager;
	private ConfigurationLoader<CommentedConfigurationNode> dropManager;
	
	private EventManager eventManager;
	
	@Inject
	public Main(Logger logger, @DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> loader, @ConfigDir(sharedRoot = false) File configDir){
		this.logger = logger;
		
		this.defaultConfig = new File(configDir + "/dropparty.conf");
		this.tierConfig = new File(configDir + "/tiers.conf");
		this.setupConfig = new File(configDir + "/setups.conf");
		this.dropConfig = new File(configDir + "/drops.conf");
		
		this.configManager =HoconConfigurationLoader.builder().setFile(defaultConfig).build();
		this.tierManager = HoconConfigurationLoader.builder().setFile(tierConfig).build();
		this.setupManager = HoconConfigurationLoader.builder().setFile(setupConfig).build();
		this.dropManager = HoconConfigurationLoader.builder().setFile(dropConfig).build();
		
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info("Started DropParty");
		eventManager = new EventManager();
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
		SetupConfiguration.getInstance().setup(setupConfig, setupManager);
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
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public EconomyService getEconService() {
		Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);
		if(!service.isPresent())
			return null;
		return service.get();
	}
}
