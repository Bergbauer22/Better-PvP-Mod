package net.bergbauer.better_pvp;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterPvP implements ModInitializer {
	public static final String MOD_ID = "better_pvp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("BetterPvP loaded succesfully");
		PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
	}
}