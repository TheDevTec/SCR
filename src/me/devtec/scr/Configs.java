package me.devtec.scr;

import java.io.File;

import me.devtec.scr.functions.AutoAnnoucments;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.dataholder.DataType;
import me.devtec.shared.utility.StreamUtils;

public class Configs {
	private static Config temp_data = new Config();
	
	public static void reloadConfig(String config) {

		if(config.equalsIgnoreCase("config")) {
			Loader.config = loadAndMerge("config.yml", "config.yml");
			AutoAnnoucments.loadtask();
		}
		if(config.equalsIgnoreCase("commands"))
			Loader.commands = loadAndMerge("commands.yml", "commands.yml");
		
		if(config.equalsIgnoreCase("translation")) {
			loadAndMerge("translations/Translation-en.yml", "translations/Translation-en.yml");
			loadAndMerge("translations/Translation-cz.yml", "translations/Translation-cz.yml");
			String type = "en";
			if (Loader.config.exists("Options.Language"))
				type = Loader.config.getString("Options.Language");
			if (!new File("plugins/SCR/translations/Translation-" + type + ".yml").exists())
				type = "en";
			Loader.translations = loadAndMerge("translations/Translation-" + type + ".yml", "translations/Translation-" + type + ".yml");
		}
		
		if(config.equalsIgnoreCase("economy"))
			Loader.economyConfig = loadAndMerge("economy.yml", "economy.yml");
		if(config.equalsIgnoreCase("tablist"))
			Loader.tablistConfig = loadAndMerge("tablist.yml", "tablist.yml");
		if(config.equalsIgnoreCase("scoreboard"))
			Loader.scoreboardConfig = loadAndMerge("scoreboard.yml", "scoreboard.yml");
		if(config.equalsIgnoreCase("joinConfig"))
			Loader.joinListenerConfig = loadAndMerge("events/join-listener.yml", "events/join-listener.yml");
		if(config.equalsIgnoreCase("quitConfig"))
			Loader.quitListenerConfig = loadAndMerge("events/quit-listener.yml", "events/quit-listener.yml");

		temp_data.clear();
		temp_data = null; // clear cache
	}
	
	public static void loadConfigs() {
		
		Loader.config = loadAndMerge("config.yml", "config.yml");
		AutoAnnoucments.loadtask(); // in config.yml
		Loader.commands = loadAndMerge("commands.yml", "commands.yml");
		Loader.economyConfig = loadAndMerge("economy.yml", "economy.yml");
		Loader.joinListenerConfig = loadAndMerge("events/join-listener.yml", "events/join-listener.yml");
		Loader.quitListenerConfig = loadAndMerge("events/quit-listener.yml", "events/quit-listener.yml");
		Loader.tablistConfig = loadAndMerge("tablist.yml", "tablist.yml");
		Loader.scoreboardConfig = loadAndMerge("scoreboard.yml", "scoreboard.yml");
		Loader.data = loadAndMerge("data.yml", "data.yml");

		loadAndMerge("translations/Translation-en.yml", "translations/Translation-en.yml");
		loadAndMerge("translations/Translation-cz.yml", "translations/Translation-cz.yml");
		String type = "en";
		if (Loader.config.exists("Options.Language"))
			type = Loader.config.getString("Options.Language");
		if (!new File("plugins/SCR/translations/Translation-" + type + ".yml").exists())
			type = "en";
		Loader.translations = loadAndMerge("translations/Translation-" + type + ".yml", "translations/Translation-" + type + ".yml");

		temp_data.clear();
		temp_data = null; // clear cache
	}

	private static Config loadAndMerge(String sourcePath, String filePath) {
		temp_data.reload(StreamUtils.fromStream(Loader.plugin.getResource("files/" + sourcePath)));

		Config result = new Config("plugins/SCR/" + filePath);
		if (result.merge(temp_data))
			result.save(DataType.YAML);
		return result;
	}
	
}
