package com.github.elrol.dropparty.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.Main;
import com.github.elrol.dropparty.commands.arguments.DropPartyArguments;
import com.github.elrol.dropparty.libs.PluginInfo.Descriptions;
import com.github.elrol.dropparty.libs.PluginInfo.Permissions;

public class CommandRegistry {

	public static void setup(Main main) {
		//DropParty help
		CommandSpec dropPartyHelp = CommandSpec.builder()
				.description(Descriptions.dropPartyHelp)
				.permission(Permissions.dropPartyHelp)
				.executor(new DropPartyHelpExecutor())
				.build();
		
		//DropParty start [name]
		CommandSpec dropPartyStart = CommandSpec.builder()
				.description(Descriptions.dropPartyStart)
				.permission(Permissions.dropPartyStart)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("delay"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("persec"))),
						GenericArguments.optional(GenericArguments.bool(Text.of("admin"))))
				.executor(new DropPartyStartExecutor())
				.build();
		//DropParty stop [name]
		CommandSpec dropPartyStop = CommandSpec.builder()
				.description(Descriptions.dropPartyStop)
				.permission(Permissions.dropPartyStop)
				.executor(new DropPartyStopExecutor())
				.build();
		
		//DropParty droplist create [name]
		CommandSpec dropPartyDropListCreate = CommandSpec.builder()
				.description(Descriptions.dropPartyDropListCreate)
				.permission(Permissions.dropPartyDropListCreate)
				.arguments(
						GenericArguments.string(Text.of("name")))
				.executor(new DropPartyDropListExecutor(5))
				.build();
		
		//DropParty droplist add [name]
		CommandSpec dropPartyDropListAddAll = CommandSpec.builder()
				.description(Descriptions.dropPartyDropListAddAll)
				.permission(Permissions.dropPartyDropListAddAll)
				.arguments(
						DropPartyArguments.droplistName(Text.of("name")),
						DropPartyArguments.partyName(Text.of("party")))
				.executor(new DropPartyDropListExecutor(4))
				.build();
		
		
		//DropParty droplist clear [name]
		CommandSpec dropPartyDropListClear = CommandSpec.builder()
				.description(Descriptions.dropPartyDropListClear)
				.permission(Permissions.dropPartyDropListClear)
				.arguments(
						DropPartyArguments.droplistName(Text.of("name")))
				.executor(new DropPartyDropListExecutor(3))
				.build();
		
		
		//DropParty droplist add [name]
		CommandSpec dropPartyDropListRemove = CommandSpec.builder()
				.description(Descriptions.dropPartyDropListRemove)
				.permission(Permissions.dropPartyDropListRemove)
				.arguments(
						DropPartyArguments.droplistName(Text.of("name")))
				.executor(new DropPartyDropListExecutor(2))
				.build();
				
		//DropParty droplist add [name]
		CommandSpec dropPartyDropListAdd = CommandSpec.builder()
				.description(Descriptions.dropPartyDropListAdd)
				.permission(Permissions.dropPartyDropListAdd)
				.arguments(
						DropPartyArguments.droplistName(Text.of("name")))
				.executor(new DropPartyDropListExecutor(1))
				.build();
		
		//DropParty droplist [name]
		CommandSpec dropPartyDropList = CommandSpec.builder()
				.description(Descriptions.dropPartyDropList)
				.permission(Permissions.dropPartyDropList)
				.arguments(
						DropPartyArguments.droplistName(Text.of("name")))
				.child(dropPartyDropListAdd, "add")
				.child(dropPartyDropListRemove, "remove")
				.child(dropPartyDropListClear, "clear")
				.child(dropPartyDropListAddAll, "addall")
				.child(dropPartyDropListCreate, "create")
				.executor(new DropPartyDropListExecutor(0))
				.build();
		//DropParty clear [name]
		CommandSpec dropPartyClear = CommandSpec.builder()
				.description(Descriptions.dropPartyClear)
				.permission(Permissions.dropPartyClear)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")))
				.executor(new DropPartyClearExecutor())
				.build();
		//DropParty tp [name] {player}
		CommandSpec dropPartyTP = CommandSpec.builder()
				.description(Descriptions.dropPartyTP)
				.permission(Permissions.dropPartyTP)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.player(Text.of("player"))))
				.executor(new DropPartyTpExecutor())
				.build();
		
		//DropParty tier remove [tier] {item:meta}
		CommandSpec dropPartyTierRemove = CommandSpec.builder()
				.description(Descriptions.dropPartyTierRemove)
				.permission(Permissions.dropPartyTierRemove)
				.arguments(
						GenericArguments.integer(Text.of("tier")),
						GenericArguments.optional(GenericArguments.string(Text.of("item"))))
				.executor(new DropPartyTierExecutor(2))
				.build();
		//DropParty tier add [tier] {item:meta}
		CommandSpec dropPartyTierAdd = CommandSpec.builder()
				.description(Descriptions.dropPartyTierAdd)
				.permission(Permissions.dropPartyTierAdd)
				.arguments(
						GenericArguments.integer(Text.of("tier")))
				.executor(new DropPartyTierExecutor(1))
				.build();
		
		//DropParty tier [tier]
		CommandSpec dropPartyTier = CommandSpec.builder()
				.description(Descriptions.dropPartyTier)
				.permission(Permissions.dropPartyTier)
				.arguments(
						GenericArguments.integer(Text.of("tier")))
				.child(dropPartyTierAdd, "add", "a")
				.child(dropPartyTierRemove, "remove", "r")
				.executor(new DropPartyTierExecutor(0))
				.build();
		
		//DropParty rename [name] [newname]
		CommandSpec dropPartyRename = CommandSpec.builder()
				.description(Descriptions.dropPartyRename)
				.permission(Permissions.dropPartyRename)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.string(Text.of("newname")))
				.executor(new DropPartyRenameExecutor())
				.build();
		
		//DropParty remove [name]
		CommandSpec dropPartyRemove = CommandSpec.builder()
				.description(Descriptions.dropPartyRemove)
				.permission(Permissions.dropPartyRemove)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")))
				.executor(new DropPartyRemoveExecutor())
				.build();
		
		//DropParty drop remove [name] [id]
		CommandSpec dropPartyDropRemove = CommandSpec.builder()
				.description(Descriptions.dropPartyDropRemove)
				.permission(Permissions.dropPartyDropRemove)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.integer(Text.of("id")))
				.executor(new DropPartyDropExecutor(2))
				.build();
				
		//DropParty drop add [name] {[x] [y] [z] {dim}}
		CommandSpec dropPartyDropAdd = CommandSpec.builder()
				.description(Descriptions.dropPartyDropAdd)
				.permission(Permissions.dropPartyDropAdd)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))),
						GenericArguments.optional(GenericArguments.string(Text.of("dim"))))
				.executor(new DropPartyDropExecutor(1))
				.build();
		
		//DropParty drop [name]
		CommandSpec dropPartyDrop = CommandSpec.builder()
				.description(Descriptions.dropPartyDrop)
				.permission(Permissions.dropPartyDrop)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")))
				.child(dropPartyDropAdd, "add", "a")
				.child(dropPartyDropRemove, "remove", "r")
				.executor(new DropPartyDropExecutor(0))
				.build();
		
		//DropParty chest remove [name] [id]
		CommandSpec dropPartyChestRemove = CommandSpec.builder()
				.description(Descriptions.dropPartyChestRemove)
				.permission(Permissions.dropPartyChestRemove)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.integer(Text.of("id")))
				.executor(new DropPartyChestExecutor(2))
				.build();
				
		//DropParty chest add [name] {[x] [y] [z] {dim}}
		CommandSpec dropPartyChestAdd = CommandSpec.builder()
				.description(Descriptions.dropPartyChestAdd)
				.permission(Permissions.dropPartyChestAdd)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))),
						GenericArguments.optional(GenericArguments.string(Text.of("dim"))))
				.executor(new DropPartyChestExecutor(1))
				.build();
		//DropParty chest [name]
		CommandSpec dropPartyChest = CommandSpec.builder()
				.description(Descriptions.dropPartyChest)
				.permission(Permissions.dropPartyChest)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")))
				.child(dropPartyChestAdd, "add", "a")
				.child(dropPartyChestRemove, "remove", "r")
				.executor(new DropPartyChestExecutor(0))
				.build();
		
		//DropParty create [name] {[x] [y] [z] {dim}}
		CommandSpec dropPartyCreate = CommandSpec.builder()
				.description(Descriptions.dropPartyCreate)
				.permission(Permissions.dropPartyCreate)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("x"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("y"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))),
						GenericArguments.optional(GenericArguments.string(Text.of("dim"))))
				.executor(new DropPartyCreateExecutor())
				.build();
		
		//DropParty
		CommandSpec dropParty = CommandSpec.builder()
			    .description(Descriptions.dropParty)
			    .permission(Permissions.dropParty)
			    .child(dropPartyCreate, "create")
			    .child(dropPartyChest, "chest")
			    .child(dropPartyDrop, "drop")
			    .child(dropPartyRemove, "remove")
			    .child(dropPartyRename, "rename")
			    .child(dropPartyTier, "tier")
			    .child(dropPartyTP, "teleport", "tp")
			    .child(dropPartyClear, "clear")
			    .child(dropPartyDropList, "droplist")
			    .child(dropPartyStart, "start")
			    .child(dropPartyStop, "stop")
			    .child(dropPartyHelp, "help")
			    .executor(new DropPartyExecutor())
			    .build();

		Sponge.getCommandManager().register(main, dropParty, "dropparty", "dp");
	}	
}
