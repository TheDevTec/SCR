package me.devtec.scr;

import me.devtec.theapi.configapi.Config;

public class ConfigManager {
	public static Config commands = new Config("plugins/SCR/commands.yml"), data = new Config("plugins/SCR/data.yml");

	public static void load() {
		Loader.config=Config.loadConfig(Loader.plugin, "files/config.yml", "SCR/config.yml");
		Loader.translation=Config.loadConfig(Loader.plugin, "files/translation/en.yml", "SCR/translation/en.yml");
		Config.loadConfig(Loader.plugin, "files/translation/cz.yml", "SCR/translation/cz.yml");
		Config.loadConfig(Loader.plugin, "files/translation/ru.yml", "SCR/translation/ru.yml");
	}

	public static void unload() {
		
	}
}
