package me.DevTec.ServerControlReloaded.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.DataKeeper.Data;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.UnsortedMap;
import me.DevTec.TheAPI.Utils.Decompression.Decompression;

public class Configs {
	public static void load() {
		copyDefauts();
		String lang = Loader.config.getString("Options.Language");
		if(lang!=null) {
			if(!new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml").exists())
			lang="en";
		}else lang="en";
		Loader.trans = translations.get("Translations/translation-"+lang+".yml");
		setting.load();
	}
	
	static boolean a;
	static Map<String, Config> translations = new UnsortedMap<>();
	static List<String> datas = Arrays.asList("Config.yml","Scoreboard.yml","Tablist.yml","Kits.yml","MultiWorlds.yml","Events.yml","Commands.yml","Translations/translation-cz.yml","Translations/translation-en.yml","Translations/translation-sk.yml");
	
	private static void copyDefauts() {
		if(a)return;
		a=true;
		for(String s : datas) {
			Config c=new Config("ServerControlReloaded/"+s);
    		Data data=new Data();
    		data.reload(Decompression.getText(Loader.getInstance.getResource("Configs/"+s)));
	    	for(String sr : data.getKeys(true)) {
	    		if(!c.isSection(sr)) {
	    			c.setComments(sr, data.getComments(sr));
	    			c.set(sr, data.get(sr));
	    		}
	    	}
	    	c.save();
	    	if(s.startsWith("Translations/translation-"))
	    	translations.put(s, c);
	    	switch(c.getData().getFile().getName()) {
	    	case "Kits.yml":
	    		Loader.kit=c;
	    		break;
	    	case "Config.yml":
	    		Loader.config=c;
	    		break;
	    	case "Tablist.yml":
	    		Loader.tab=c;
	    		break;
	    	case "Scoreboard.yml":
	    		Loader.sb=c;
	    		break;
	    	case "MultiWorlds.yml":
	    		Loader.mw=c;
	    		break;
	    	case "Events.yml":
	    		Loader.events=c;
	    		break;
	    	case "Commands.yml":
	    		Loader.cmds=c;
	    		break;
	    	case "translation-en.yml":
	    		Loader.english=c;
	    		break;
	    	}
		}
	}
}
