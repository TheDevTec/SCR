package me.DevTec.ServerControlReloaded.Utils;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.datakeeper.loader.YamlLoader;

public class Configs {
	public static void load(boolean settingMessage) {
		copyDefauts();
		String lang = Loader.config.getString("Options.Language");
		if(lang!=null) {
			if(!new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml").exists())
			lang="en";
		}else lang="en";
		Loader.trans = new Config("Translations/translation-"+lang+".yml");
		setting.load(settingMessage);
		AnimationManager.reload();
	}
	
	static List<String> datas = Arrays.asList("Config.yml","Scoreboard.yml","Tablist.yml","BossBar.yml","ActionBar.yml", "Animations.yml","Kits.yml","MultiWorlds.yml","Events.yml","Commands.yml","Translations/translation-en.yml","Translations/translation-cz.yml","Translations/translation-sk.yml");
	
	@SuppressWarnings("unchecked")
	private static void copyDefauts() {
		for(String s : datas) {
			Config c = new Config("ServerControlReloaded/"+s);
    		YamlLoader data = new YamlLoader();
    		try {
    		URLConnection u = Loader.getInstance.getClass().getClassLoader().getResource("Configs/"+s).openConnection();
    		u.setUseCaches(false);
    		data.load(StreamUtils.fromStream(u.getInputStream()));
    		}catch(Exception e) {}
	    	boolean change = false;
	    	for(String sr : data.getKeys()) {
	    		if(c.get(sr)==null && data.get().get(sr)[0]!=null) {
	    			c.set(sr, data.get().get(sr)[0]);
	    			change = true;
	    		}
	    		if(c.getComments(sr)!=null && c.getComments(sr).isEmpty() && (data.get().get(sr)[1]==null||!((List<String>) data.get().get(sr)[1]).isEmpty())) {
	    			c.setComments(sr, (List<String>) data.get().get(sr)[1]);
	    			change = true;
	    		}
	    	}
	    	try {
    		if(data.getHeader()!=null)
    			if(!c.getHeader().equals(data.getHeader()))
    				c.setHeader(data.getHeader());
    		if(data.getFooter()!=null)
    			if(!c.getFooter().equals(data.getFooter()))
    				c.setFooter(data.getFooter());
	    	}catch(Exception unsuported) {}
	    	if(change)
	    	c.save();
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
	    	case "BossBar.yml":
	    		Loader.bb=c;
	    		break;
	    	case "ActionBar.yml":
	    		Loader.ac=c;
	    		break;
	    	case "Events.yml":
	    		Loader.events=c;
	    		break;
	    	case "Commands.yml":
	    		Loader.cmds=c;
	    		break;
	    	case "Animations.yml":
	    		Loader.anim=c;
	    		break;
	    	case "translation-en.yml":
	    		Loader.english=c;
	    		break;
	    	}
		}
	}
}
