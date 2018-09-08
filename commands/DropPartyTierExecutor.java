package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.text.Text;

import com.github.elrol.dropparty.config.TierConfiguration;
import com.github.elrol.dropparty.libs.TextLibs;

public class DropPartyTierExecutor implements CommandExecutor {

	private int mode;
	
	public DropPartyTierExecutor(int mode) {
		this.mode = mode;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		int tier = args.<Integer>getOne("tier").get();
		if(mode == 0) {
			List<ItemType> items = TierConfiguration.getInstance().getTier(tier);
			if(items.isEmpty()) {
				TextLibs.sendMessage(src, "No items found for Tier " + tier);
				return CommandResult.builder().successCount(0).build();
			}
			TextLibs.sendMessage(src, "Tier " + tier + " Items:");
			for(ItemType item : items) {
				TextLibs.sendMessage(src, Text.of(TextLibs.headerSpacing + item.getTranslation()));
			}
		} else if(mode == 1) {
			if(src instanceof Player) {
				Player player = (Player)src;
				ItemStack item = player.getEquipped(EquipmentTypes.MAIN_HAND).get();
				TierConfiguration.getInstance().addItem(src, tier, item.getType());
				
			}
		} else if(mode == 2){
			if(src instanceof Player) {
				Player player = (Player)src;
				if(player.getEquipped(EquipmentTypes.MAIN_HAND) != null) {
					ItemType item = player.getEquipped(EquipmentTypes.MAIN_HAND).get().getType();
					TierConfiguration.getInstance().removeItem(src, tier, item);
				} else {
					TextLibs.sendError(src, "You must be holding the item you want to remove from the list");
				}
			}
		} else {
			
		}
		return CommandResult.success();
	}

}
