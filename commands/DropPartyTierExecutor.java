package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.config.TierConfiguration;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyTierExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		int tier = args.<Integer>getOne("tier").get();
		int mode = 0;
		if(args.hasAny("sub"))
			mode = args.<Integer>getOne("sub").get();
		if(mode == 0) {
			List<ItemStack> items = TierConfiguration.getInstance().getTier(tier);
			if(items.isEmpty()) {
				TextLibs.sendMessage(src, "No items found for Tier " + tier);
				return CommandResult.builder().successCount(0).build();
			}
			TextLibs.sendMessage(src, "Tier " + tier + " Items:");
			for(ItemStack item : items) {
				TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + item.getTranslation().get()));
			}
		} else if(mode == 1) {
			if(src instanceof Player) {
				Player player = (Player)src;
				ItemStack item = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
				if(!item.getType().equals(ItemTypes.AIR))
					TierConfiguration.getInstance().addItem(src, tier, item);
				else
					TextLibs.sendMessage(src, "You cant add an empty hand to any tier");
				
			}
		} else if(mode == 2) {
			if(src instanceof Player) {
				Player player = (Player)src;
				if(player.getEquipped(EquipmentTypes.MAIN_HAND) != null) {
					ItemStack item = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
					TierConfiguration.getInstance().removeItem(src, tier, item);
				} else {
					TextLibs.sendError(src, "You must be holding the item you want to remove from the list");
				}
			}
		} else if(mode == 3) {
			if(tier > 5 || tier < 1) {
				TextLibs.sendError(src, "Tier can only be 1-5");
				return CommandResult.empty();
			}
			TierConfiguration.getInstance().clearTier(src, tier);
		}
		return CommandResult.success();
	}

}
