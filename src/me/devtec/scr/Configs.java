package me.devtec.scr;

import java.io.File;

import me.devtec.shared.dataholder.Config;
import me.devtec.shared.dataholder.DataType;
import me.devtec.shared.utility.StreamUtils;

public class Configs {

	private static Config data = new Config();
	
	public static void loadConfigs() {
		Loader.config = loadAndMerge("Config.yml", "Config.yml");
		Loader.commands = loadAndMerge("Commands.yml", "Commands.yml");
		Loader.economyConfig = loadAndMerge("economy.yml", "economy.yml");
		/*joinListenerConfig = loadAndMerge("events/join-listener.yml", "events/join-listener.yml");
		quitListenerConfig = loadAndMerge("events/quit-listener.yml", "events/quit-listener.yml");
		tablistConfig = loadAndMerge("tablist.yml", "tablist.yml");*/
		
			// TRANSLATIONS
		loadAndMerge("Translations/Translation-en.yml", "translations/Translation-en.yml");
		loadAndMerge("Translations/Translation-cz.yml", "translations/Translation-cz.yml");
		
		String type = "en";
		if(Loader.config.exists("Options.Language"))
			type = Loader.config.getString("Options.Language");
		if(!new File("plugins/SCR/Translations/Translation-"+type+".yml").exists())
			type = "en";
		Loader.translation = loadAndMerge("Translations/Translation-"+type+".yml", "translations/Translation-"+type+".yml");
		
		data.clear();
		data = null; //clear cache
	}
	
	public static Config loadAndMerge(String sourcePath, String filePath) {
		data.reload(StreamUtils.fromStream(Loader.plugin.getResource("Files/"+sourcePath)));
		
		Config result = new Config("plugins/SCR/"+filePath);
		if(result.merge(data, true, true))
			result.save(DataType.YAML);
		return result;
	}
}
