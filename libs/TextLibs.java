package com.github.elrol.dropparty.libs;


import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class TextLibs {

	public static Text pluginHeader() {
		Text header = Text.builder()
				.append(Text.of(TextColors.DARK_GRAY, "["))
				.append(Text.of(TextColors.BLUE, "Drop"))
				.append(Text.of(TextColors.AQUA, "Party"))
				.append(Text.of(TextColors.DARK_GRAY, "]"))
				.build();
		return header;
	}
	
	public static Text pluginError(String message) {
		Text error = Text.builder()
				.append(pluginHeader())
				.append(Text.of(TextColors.RED, " " + message))
				.build();
		return error;
	}
	
	public static Text pluginMessage(String message) {
		Text error = Text.builder()
				.append(pluginHeader())
				.append(Text.of(TextColors.WHITE, " " + message))
				.build();
		return error;
	}
}
