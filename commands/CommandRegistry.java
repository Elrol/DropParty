package com.github.elrol.dropparty.commands;

import java.util.HashMap;
import java.util.Map;

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
		//DropParty load [party] [list]
		CommandSpec dropPartyLoad = CommandSpec.builder()
				.description(Descriptions.dropPartyLoad)
				.permission(Permissions.dropPartyLoad)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						DropPartyArguments.droplistName(Text.of("list")))
				.executor(new DropPartyLoadExecutor())
				.build();
		
		//DropParty cost [name] [cost]
		CommandSpec dropPartyCost = CommandSpec.builder()
				.description(Descriptions.dropPartyCost)
				.permission(Permissions.dropPartyCost)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("cost"))))
				.executor(new DropPartyCostExecutor())
				.build();
		//DropParty help
		CommandSpec dropPartyHelp = CommandSpec.builder()
				.description(Descriptions.dropPartyHelp)
				.permission(Permissions.dropPartyHelp)
				.executor(new DropPartyHelpExecutor())
				.build();
		
		//DropParty start [name]
		CommandSpec dropPartyStart = CommandSpec.builder()
				.description(Descriptions.dropPartyStart)
				.permission(Permissions.dropCostPartyStart)
				.arguments(
						DropPartyArguments.partyName(Text.of("name")),
						GenericArguments.optional(GenericArguments.integer(Text.of("delay"))),
						GenericArguments.optional(GenericArguments.integer(Text.of("persec"))),
						GenericArguments.optional(GenericArguments.string(Text.of("admin"))))
				.executor(new DropPartyStartExecutor())
				.build();
		
		//DropParty stop
		CommandSpec dropPartyStop = CommandSpec.builder()
				.description(Descriptions.dropPartyStop)
				.permission(Permissions.dropPartyStop)
				.executor(new DropPartyStopExecutor())
				.build();
		
		Map<String, Integer> droplistChoices = new HashMap<String, Integer>();
		droplistChoices.put("add", 1);
		droplistChoices.put("remove", 2);
		droplistChoices.put("clear", 3);
		droplistChoices.put("addall", 4);
		droplistChoices.put("create", 5);
		
		//DropParty droplist [name] {subcmd}
		CommandSpec dropPartyDropList = CommandSpec.builder()
				.description(Descriptions.dropPartyDropList)
				.permission(Permissions.dropPartyDropList)
				.arguments(
						DropPartyArguments.droplistName(Text.of("name")),
						GenericArguments.optional(GenericArguments.choices(Text.of("sub"), droplistChoices)),
						GenericArguments.optional(DropPartyArguments.droplistName(Text.of("party"))))
				.executor(new DropPartyDropListExecutor())
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
		
		Map<String, Integer> tierChoices = new HashMap<String, Integer>();
		tierChoices.put("add", 1);
		tierChoices.put("remove", 2);
		tierChoices.put("clear", 3);
		
		//DropParty tier [tier]
		CommandSpec dropPartyTier = CommandSpec.builder()
				.description(Descriptions.dropPartyTier)
				.permission(Permissions.dropPartyTier)
				.arguments(
						GenericArguments.integer(Text.of("tier")),
						GenericArguments.optional(GenericArguments.choices(Text.of("sub"), tierChoices)))
				.executor(new DropPartyTierExecutor())
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
						GenericArguments.optional(GenericArguments.integer(Text.of("z"))))
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
			    .child(dropPartyCost, "cost")
			    .child(dropPartyLoad, "load")
			    .executor(new DropPartyExecutor())
			    .build();

		Sponge.getCommandManager().register(main, dropParty, "dropparty", "dp");
	}	
}
