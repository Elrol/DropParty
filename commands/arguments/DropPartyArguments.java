package com.github.elrol.dropparty.commands.arguments;

import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.config.DropConfiguration;
import com.github.elrol.dropparty.config.SetupConfiguration;

public class DropPartyArguments {

	public static CommandElement partyName(Text text) {
		return new PartyName(text);
	}
	
	public static CommandElement droplistName(Text text) {
		return new DropList(text);
	}
	
	public static class PartyName extends CommandElement{

		public PartyName(Text key) {
			super(key);
		}

		@Override
		protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
			return args.next();
		}

		@Override
		public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
			return SetupConfiguration.getInstance().getParties();
		}
		
		public Text getUsage(CommandSource src) {
			return Text.of("<partyName>");
		}
	}
	
	public static class DropList extends CommandElement{

		public DropList(Text key) {
			super(key);
		}

		@Override
		protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
			return args.next();
		}

		@Override
		public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
			return DropConfiguration.getInstance().getDrops();
		}
		
		public Text getUsage(CommandSource src) {
			return Text.of("<dropList>");
		}
	}
}
