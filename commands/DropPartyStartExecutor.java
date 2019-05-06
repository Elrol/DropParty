package com.github.elrol.dropparty.commands;

import java.math.BigDecimal;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.Methods;
import com.github.elrol.dropparty.libs.PluginInfo.Permissions;
import com.github.elrol.dropparty.libs.TextLibs;



public class DropPartyStartExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player player = null;
		if(src instanceof Player)
			player = (Player)src;
		SetupConfiguration setup = SetupConfiguration.getInstance();
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			if(Main.getInstance().getEventManager().isRunning) { 
				TextLibs.sendError(src, "A Drop Party is already running, Please wait until it has finished.");
				return CommandResult.success();
			}
			boolean isAdmin = false;
			if(args.hasAny("admin") && args.<String>getOne("admin").get().equalsIgnoreCase("admin")) {
				if(src.hasPermission(Permissions.dropPartyOp)) {
					isAdmin = true;
				} else {
					TextLibs.sendError(src, "You do not have the permissions needed to start and Admin DropParty");
					return CommandResult.empty();
					
				}
			}
			
			if(player != null) {
				if(!src.hasPermission(Permissions.dropPartyStart)) {
					if(SetupConfiguration.getInstance().getPartyCost(name) >= 0 && Main.getInstance().getEconService() != null) {
						Optional<UniqueAccount> account = Main.getInstance().getEconService().getOrCreateAccount(player.getUniqueId());
						account.get().withdraw(Main.getInstance().getEconService().getDefaultCurrency(), BigDecimal.valueOf(SetupConfiguration.getInstance().getPartyCost(name)), Sponge.getCauseStackManager().getCurrentCause());
					}
				}
			}
			int delay = 0;
			int persec = 3;
			if(args.hasAny("delay"))
				delay = args.<Integer>getOne("delay").get();
			if(args.hasAny("persec")){
				persec = args.<Integer>getOne("persec").get();
				if(persec > 20){
					TextLibs.sendError(src, "ItemsPerSec cannot exceed 20");
					return CommandResult.empty();
				} else if(persec < 1) {
					TextLibs.sendError(src, "ItemsPerSec must be at least 1");
					return CommandResult.empty();
				}
			}
			Title title = Title.builder().title(Text.of(TextColors.BLUE, "DropParty '" + name + "'"))
					.subtitle(Text.of(TextColors.AQUA, "Starting in " + delay + " min")).stay(100).build();
			if(setup.doesPartyExist(name))
				if(!setup.getChests(name).isEmpty())
					if(!setup.getDrops(name).isEmpty())
						if(setup.getPartyItemQty(name) > 0) {
							Main.getInstance().getEventManager().scheduleEvent(player, name, null, delay, persec, isAdmin);
							Methods.broadcastTitle(title);
						} else TextLibs.sendError(src, "There are no items in the chests for the party.");
					else TextLibs.sendError(src, "There are no DropPoints specified for the party. Use /DropParty drop add " + name);
				else TextLibs.sendError(src, "There are no Chests specified for the party. Use /DropParty chest add " + name);
			else TextLibs.sendError(src, "There is no party by the name of: " + name);
			
		}
		return CommandResult.success();
	}

}
