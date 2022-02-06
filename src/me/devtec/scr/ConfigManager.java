package me.devtec.scr;

import java.io.File;

import me.devtec.theapi.configapi.Config;

public class ConfigManager {
	public static Config commands = new Config("plugins/SCR/commands.yml"), data = new Config("plugins/SCR/data.yml"), 
			tablist, scoreboard, bossbar, actionbar, listeners, economy, punishment;

	public static void load() {
		Loader.config=Config.loadConfig(Loader.plugin, "files/config.yml", "SCR/config.yml");
		tablist=Config.loadConfig(Loader.plugin, "files/modules/tablist.yml", "SCR/modules/tablist.yml");
		scoreboard=Config.loadConfig(Loader.plugin, "files/modules/scoreboard.yml", "SCR/modules/scoreboard.yml");
		actionbar=Config.loadConfig(Loader.plugin, "files/modules/actionbar.yml", "SCR/modules/actionbar.yml");
		bossbar=Config.loadConfig(Loader.plugin, "files/modules/bossbar.yml", "SCR/modules/bossbar.yml");
		punishment=Config.loadConfig(Loader.plugin, "files/modules/punishment.yml", "SCR/modules/punishment.yml");
		Loader.defaultTranslation=Config.loadConfig(Loader.plugin, "files/translation/en.yml", "SCR/translation/en.yml");
		Config.loadConfig(Loader.plugin, "files/translation/cz.yml", "SCR/translation/cz.yml");
		Config.loadConfig(Loader.plugin, "files/translation/ru.yml", "SCR/translation/ru.yml");
		if(new File("plugins/SCR/translation/"+Loader.config.getString("translation")+".yml").exists())
			Loader.translation=new Config("SCR/translation/"+Loader.config.getString("translation")+".yml");
		else Loader.translation=Loader.defaultTranslation;
	}

	public static void unload() {
		
	}
}
