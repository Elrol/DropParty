package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.config.SetupConfiguration;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyStartExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		SetupConfiguration setup = SetupConfiguration.getInstance();
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			int delay = 0;
			int persec = 3;
			if(args.hasAny("delay"))
				delay = args.<Integer>getOne("delay").get();
			if(args.hasAny("persec")){
				persec = args.<Integer>getOne("persec").get();
				if(persec > 20){
					persec = 20;
					TextLibs.sendError(src, "ItemsPerSec cannot exceed 20");
				}
			}
			boolean isAdmin = false;
			if(args.hasAny("admin") && args.<Boolean>getOne("admin").get())
				isAdmin = true;
			Title title = Title.builder().title(Text.of(TextColors.BLUE, "DropParty '" + name + "'"))
					.subtitle(Text.of(TextColors.AQUA, "Starting in " + delay + " min")).stay(100).build();
			if(setup.doesPartyExist(name))
				if(!setup.getChests(name).isEmpty())
					if(!setup.getDrops(name).isEmpty())
						if(setup.getPartyItemQty(name) > 0)
							Main.getInstance().getEventManager().scheduleEvent(name, title, delay, persec, isAdmin);
						else TextLibs.sendError(src, "There are no items in the chests for the party.");
					else TextLibs.sendError(src, "There are no DropPoints specified for the party. Use /DropParty drop add " + name);
				else TextLibs.sendError(src, "There are no Chests specified for the party. Use /DropParty chest add " + name);
			else TextLibs.sendError(src, "There is no party by the name of: " + name);
			
		}
		return CommandResult.success();
	}

}
