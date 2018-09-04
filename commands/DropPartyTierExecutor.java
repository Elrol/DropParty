package com.github.elrol.dropparty.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
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
				src.sendMessage(TextLibs.pluginError("No items found for Tier " + tier));
				return CommandResult.builder().successCount(0).build();
			}
			src.sendMessage(TextLibs.pluginMessage("Tier " + tier + " Items:"));
			for(ItemType item : items) {
				src.sendMessage(Text.of(item.getTranslation()));
			}
		} else if(mode == 1) {
			if(src instanceof Player) {
				Player player = (Player)src;
				ItemType item = player.getEquipped(EquipmentTypes.MAIN_HAND).get().getType();
				TierConfiguration.getInstance().addItem(src, tier, item);
				
			}
		} else {
			if(src instanceof Player) {
				Player player = (Player)src;
				ItemType item = player.getEquipped(EquipmentTypes.MAIN_HAND).get().getType();
				TierConfiguration.getInstance().removeItem(src, tier, item);
			}
		}
		return CommandResult.success();
	}

}
