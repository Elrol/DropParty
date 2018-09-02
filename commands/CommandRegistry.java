package com.github.elrol.dropparty.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.Main;

public class CommandRegistry {

	public static void setup(Main main) {
		CommandSpec dropPartyCreate = CommandSpec.builder()
				.description(Text.of("Drop Party Create help command"))
				.permission("dropparty.command.create")
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))))
				.executor(new DropPartyCreateExecutor())
				.build();
		CommandSpec dropParty = CommandSpec.builder()
			    .description(Text.of("Drop Party help command"))
			    .permission("dropparty.command.dropparty")
			    .child(dropPartyCreate, "create", "c")
			    .executor(new DropPartyExecutor())
			    .build();

		Sponge.getCommandManager().register(main, dropParty, "dropparty", "dp");
	}
	
}
