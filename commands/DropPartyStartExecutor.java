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

public class DropPartyStartExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("name")) {
			String name = args.<String>getOne("name").get();
			int delay = 0;
			int persec = 3;
			if(args.hasAny("delay"))
				delay = args.<Integer>getOne("delay").get();
			if(args.hasAny("persec"))
				persec = args.<Integer>getOne("persec").get();
			Title title = Title.builder().title(Text.of(TextColors.BLUE, "DropParty '" + name + "'"))
					.subtitle(Text.of(TextColors.AQUA, "Starting in " + delay + " min")).stay(100).build();
			Main.getInstance().getEventManager().scheduleEvent(name, title, delay, persec);
		}
		return CommandResult.success();
	}

}
