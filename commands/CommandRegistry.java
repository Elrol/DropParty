package com.github.elrol.dropparty.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.libs.PluginPermissions;

public class CommandRegistry {

	public static void setup(Main main) {
		//DropParty rename [name] [newname]
		CommandSpec dropPartyRename = CommandSpec.builder()
				.description(Text.of("Renames the party specified to the new name"))
				.permission(PluginPermissions.dropPartyRename)
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.string(Text.of("newname")))
				.executor(new DropPartyRenameExecutor())
				.build();
		
		//DropParty remove [name]
		CommandSpec dropPartyRemove = CommandSpec.builder()
				.description(Text.of("removes a DropParty setup"))
				.permission(PluginPermissions.dropPartyRemove)
				.arguments(
						GenericArguments.string(Text.of("name")))
				.executor(new DropPartyRemoveExecutor())
				.build();
		
		//DropParty drop remove [name] [id]
		CommandSpec dropPartyDropRemove = CommandSpec.builder()
				.description(Text.of("Removes a drop point from the specified party setup"))
				.permission(PluginPermissions.dropPartyDropRemove)
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.integer(Text.of("id")))
				.executor(new DropPartyDropExecutor(2))
				.build();
				
		//DropParty drop add [name] {[x] [y] [z] {dim}}
		CommandSpec dropPartyDropAdd = CommandSpec.builder()
				.description(Text.of("Adds a drop point to the specified party setup"))
				.permission(PluginPermissions.dropPartyDropAdd)
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))),
						GenericArguments.optional(GenericArguments.string(Text.of("dim"))))
				.executor(new DropPartyDropExecutor(1))
				.build();
		
		//DropParty drop [name]
		CommandSpec dropPartyDrop = CommandSpec.builder()
				.description(Text.of("Drop Party Drop point help command"))
				.permission(PluginPermissions.dropPartyDrop)
				.arguments(
						GenericArguments.string(Text.of("name")))
				.child(dropPartyDropAdd, "add", "a")
				.child(dropPartyDropRemove, "remove", "r")
				.executor(new DropPartyDropExecutor(0))
				.build();
		
		//DropParty chest remove [name] [id]
		CommandSpec dropPartyChestRemove = CommandSpec.builder()
				.description(Text.of("Removes a chest from the specified party setup"))
				.permission(PluginPermissions.dropPartyChestRemove)
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.integer(Text.of("id")))
				.executor(new DropPartyChestExecutor(2))
				.build();
				
		//DropParty chest add [name] {[x] [y] [z] {dim}}
		CommandSpec dropPartyChestAdd = CommandSpec.builder()
				.description(Text.of("Adds a chest to the specified party setup"))
				.permission(PluginPermissions.dropPartyChestAdd)
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))),
						GenericArguments.optional(GenericArguments.string(Text.of("dim"))))
				.executor(new DropPartyChestExecutor(1))
				.build();
		//DropParty chest [name]
		CommandSpec dropPartyChest = CommandSpec.builder()
				.description(Text.of("Drop Party Chest help command"))
				.permission(PluginPermissions.dropPartyChest)
				.arguments(
						GenericArguments.string(Text.of("name")))
				.child(dropPartyChestAdd, "add", "a")
				.child(dropPartyChestRemove, "remove", "r")
				.executor(new DropPartyChestExecutor(0))
				.build();
		
		//DropParty create [name] {[x] [y] [z] {dim}}
		CommandSpec dropPartyCreate = CommandSpec.builder()
				.description(Text.of("Drop Party Create help command"))
				.permission(PluginPermissions.dropPartyCreate)
				.arguments(
						GenericArguments.string(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))),
						GenericArguments.optional(GenericArguments.string(Text.of("dim"))))
				.executor(new DropPartyCreateExecutor())
				.build();
		
		//DropParty
		CommandSpec dropParty = CommandSpec.builder()
			    .description(Text.of("Drop Party help command"))
			    .permission(PluginPermissions.dropParty)
			    .child(dropPartyCreate, "create")
			    .child(dropPartyChest, "chest")
			    .child(dropPartyDrop, "drop")
			    .child(dropPartyRemove, "remove")
			    .child(dropPartyRename, "rename")
			    .executor(new DropPartyExecutor())
			    .build();

		Sponge.getCommandManager().register(main, dropParty, "dropparty", "dp");
	}	
}
