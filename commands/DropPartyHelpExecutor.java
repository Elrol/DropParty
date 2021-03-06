package com.github.elrol.dropparty.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.github.elrol.dropparty.libs.PluginInfo.Descriptions;
import com.github.elrol.dropparty.libs.PluginInfo.Permissions;

public class DropPartyHelpExecutor implements CommandExecutor {

	private static Text create = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty create [name] {[x] [y] [z] [dimName]}",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty create "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text remove = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty remove [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty remove "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text chest = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty chest [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty chest "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text chestAdd = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty chest add [name] {[[x] [y] [z] [dimName]}",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty chest add "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text chestRemove = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty chest remove [name] {[[x] [y] [z] [dimName]}",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty chest remove "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text drop = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty drop [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty chest add "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text dropAdd = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty drop add [name] {[[x] [y] [z] [dimName]}",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty drop add "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text dropRemove = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty drop remove [name] {[[x] [y] [z] [dimName]}",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty drop remove "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text rename = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty drop rename [name] [newName]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty rename "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text tier = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty tier [tier]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty tier "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text tierAdd = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty tier add [tier]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty add tier "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text tierRemove = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty tier remove [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty remove tier "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text teleport = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty teleport [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty teleport "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text clear = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty clear [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty clear "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text droplist = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty droplist [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty droplist "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text droplistCreate = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty droplist create [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty droplist create "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text droplistAdd = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty droplist add [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty droplist add "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text droplistRemove = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty droplist remove [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty droplist remove "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text droplistAddAll = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty droplist addall [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty droplist addall "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text droplistClear = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty droplist clear [name]",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty droplist clear "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text start = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty start [name] {delay} {itemsPerSec} {adminMode}",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty start "))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	private static Text stop = Text.builder().append(Text.of(TextColors.BLUE, "[", TextColors.AQUA, "<", TextColors.GRAY, "/DropParty stop",  TextColors.AQUA, ">", TextColors.BLUE, "]"))
			.onClick(TextActions.suggestCommand("/DropParty stop"))
			.onHover(TextActions.showText(Text.of(TextColors.AQUA, "Click to insert command")))
			.build();
	
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Builder help = Text.builder();
		if(src.hasPermission(Permissions.dropPartyCreate))
			help.append(create, Descriptions.dropPartyCreate, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyRemove))
			help.append(remove, Descriptions.dropPartyRemove, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyChest))
			help.append(chest, Descriptions.dropPartyChest, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyChestAdd))
			help.append(chestAdd, Descriptions.dropPartyChestAdd, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyChestRemove))
			help.append(chestRemove, Descriptions.dropPartyChestRemove, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDrop))
			help.append(drop, Descriptions.dropPartyDrop, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropAdd))
			help.append(dropAdd, Descriptions.dropPartyDropAdd, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyRemove))
			help.append(dropRemove, Descriptions.dropPartyRemove, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyRename))
			help.append(rename, Descriptions.dropPartyRename, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyTier))
			help.append(tier, Descriptions.dropPartyTier, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyTierAdd))
			help.append(tierAdd, Descriptions.dropPartyTierAdd, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyTierRemove))
			help.append(tierRemove, Descriptions.dropPartyTierRemove, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyTP))
			help.append(teleport, Descriptions.dropPartyTP, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyClear))
			help.append(clear, Descriptions.dropPartyClear, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropList))
			help.append(droplist, Descriptions.dropPartyDropList, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropListCreate))
			help.append(droplistCreate, Descriptions.dropPartyDropListCreate, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropListAdd))
			help.append(droplistAdd, Descriptions.dropPartyDropListAdd, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropListRemove))
			help.append(droplistRemove, Descriptions.dropPartyDropListRemove, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropListAddAll))
			help.append(droplistAddAll, Descriptions.dropPartyDropListAddAll, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyDropListClear))
			help.append(droplistClear, Descriptions.dropPartyDropListClear, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyStart))
			help.append(start, Descriptions.dropPartyStart, Text.NEW_LINE);
		if(src.hasPermission(Permissions.dropPartyStop))
			help.append(stop, Descriptions.dropPartyStop, Text.NEW_LINE);
		src.sendMessage(help.build());
		return CommandResult.success();
	}

}
