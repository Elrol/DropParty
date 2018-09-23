package com.github.elrol.dropparty.libs;

import org.spongepowered.api.text.Text;

public class PluginInfo {

	public static final String NAME = "DropParty";
	public static final String ID = "dropparty";
	public static final String VERSION = "Alpha v0.6";
	public static final String DESC = "A Drop Party plugin commissioned by the PokeFreaks network";
	
	//Permissions
	public class Permissions{
		public static final String dropParty = "dropparty.command.help";
		public static final String dropPartyCreate = "dropparty.command.create";
		public static final String dropPartyChest = "dropparty.command.chest.info";
		public static final String dropPartyChestAdd = "dropparty.command.chest.add";
		public static final String dropPartyChestRemove = "dropparty.command.chest.remove";
		public static final String dropPartyDrop = "dropparty.command.drop.info";
		public static final String dropPartyDropAdd = "dropparty.command.drop.add";
		public static final String dropPartyDropRemove = "dropparty.command.drop.remove";
		public static final String dropPartyRemove = "dropparty.command.remove";
		public static final String dropPartyRename = "dropparty.command.rename";
		public static final String dropPartyTier = "dropparty.command.tier.help";
		public static final String dropPartyTierAdd = "dropparty.command.tier.add";
		public static final String dropPartyTierRemove = "dropparty.command.tier.remove";
		public static final String dropPartyTP = "dropparty.command.tp";
		public static final String dropPartyClear = "dropparty.command.clear";
		public static final String dropPartyDropList = "dropparty.command.droplist.info";
		public static final String dropPartyDropListAdd = "dropparty.command.droplist.add";
		public static final String dropPartyDropListRemove = "dropparty.command.droplist.remove";
		public static final String dropPartyDropListClear = "dropparty.command.droplist.clear";
		public static final String dropPartyDropListAddAll = "dropparty.command.droplist.addall";
		public static final String dropPartyDropListCreate = "dropparty.command.droplist.create";
		public static final String dropPartyStart = "dropparty.command.start";
		public static final String dropPartyStop = "dropparty.command.stop";
		public static final String dropPartyHelp = "dropparty.command.help";
		
	}
	
	//Descriptions
	public static class Descriptions{
		public static final Text dropParty = Text.of("Lists current DropParty setups");
		public static final Text dropPartyCreate = Text.of("Drop Party Create help command");
		public static final Text dropPartyChest = Text.of("Drop Party Chest help command");
		public static final Text dropPartyChestAdd = Text.of("Adds a chest to the specified Party setup");
		public static final Text dropPartyChestRemove = Text.of("Removes a chest from the specified Party setup");
		public static final Text dropPartyDrop = Text.of("Drop Party Drop point help command");
		public static final Text dropPartyDropAdd = Text.of("Adds a drop point to the specified Party setup");
		public static final Text dropPartyDropRemove = Text.of("Removes a drop point from the specified Party setup");
		public static final Text dropPartyRemove = Text.of("Removes a DropParty setup");
		public static final Text dropPartyRename = Text.of("Renames the Party specified to the new name");
		public static final Text dropPartyTier = Text.of("Lists items for the specified Tier");
		public static final Text dropPartyTierAdd = Text.of("Adds an item for the specified Tier");
		public static final Text dropPartyTierRemove = Text.of("Removes an item for the specified Tier");
		public static final Text dropPartyTP = Text.of("Teleports the player to one of the drop points of the Party setup");
		public static final Text dropPartyClear = Text.of("Clears the chests of the specified Party setup");
		public static final Text dropPartyDropList = Text.of("Lists all of the Items in the specified DropList");
		public static final Text dropPartyDropListAdd = Text.of("Adds an Item to the specified DropList");
		public static final Text dropPartyDropListRemove = Text.of("Removes an Item from the specified DropList");
		public static final Text dropPartyDropListClear = Text.of("Removes all Items from the specified DropList");
		public static final Text dropPartyDropListAddAll = Text.of("Adds all Items from all chests in a DropParty Setup");
		public static final Text dropPartyDropListCreate = Text.of("Creates a new DropList");
		public static final Text dropPartyStart = Text.of("Starts the specified DropParty");
		public static final Text dropPartyStop = Text.of("Stops the specified DropParty");
		public static final Text dropPartyHelp = Text.of("Displays the help message for DropParty");
	}
}
