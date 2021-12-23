package me.devtec.servercontrolreloaded.utils;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.datakeeper.Data;

public class Configs {
	public static void load(boolean settingMessage) {
		Loader.portals= new Config("ServerControlReloaded/Portals.yml");
		if(!new File("plugins/ServerControlReloaded/CustomCommands.yml").exists()) {
			Loader.customCmds=Config.loadConfig(Loader.getInstance, "Configs/CustomCommands.yml", "ServerControlReloaded/CustomCommands.yml");
		}else Loader.customCmds=new Config("ServerControlReloaded/CustomCommands.yml");
		copyDefauts();
		String lang = Loader.config.getString("Options.Language");
		if(lang!=null) {
			if(!new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml").exists())
			lang="en";
		}else lang="en";
		if(lang.equals("en")) {
			Loader.trans=null;
		}else {
			Config c = new Config("ServerControlReloaded/Translations/translation-"+lang+".yml");
    		Data dd = new Data();
    		dd.reload(new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml"));
	    	if(c.getData().merge(dd, false, false))c.save();
	    	Loader.trans=c;
		}
		setting.load(settingMessage);
		Tasks.aa.reload();
		TabList.aset.reload();
		DisplayManager.sb.reload();
		DisplayManager.ac.reload();
		DisplayManager.bb.reload();
		NameTagChanger.anim.reload();
		File file = new File("plugins/ServerControlReloaded/Guis");
		if(file.exists() && !file.isDirectory())file.delete();
		if(!file.exists()) {
			file.mkdirs();
			Config.loadConfig(Loader.getInstance, "Configs/Guis/shop.yml", "ServerControlReloaded/Guis/shop.yml");
			Config.loadConfig(Loader.getInstance, "Configs/Guis/item-buy.yml", "ServerControlReloaded/Guis/item-buy.yml");
		}
	}
	
	static final List<String> datas = Arrays.asList("Config.yml","Scoreboard.yml","Placeholders.yml","Tablist.yml","BossBar.yml","ActionBar.yml", "Animations.yml","Kits.yml","MultiWorlds.yml","Events.yml","Commands.yml","Translations/translation-en.yml","Translations/translation-cz.yml","Translations/translation-sk.yml", "Rewards.yml");
	
	private static void copyDefauts() {
		Data data = new Data();
		for(String s : datas) {
			data.reset();
			Config c = null;
	    	switch(s) {
	    	case "Kits.yml":
	    		c=Loader.kit;
	    		break;
	    	case "Config.yml":
	    		c=Loader.config;
	    		break;
	    	case "Placeholders.yml":
	    		c=Loader.plac;
	    		break;
	    	case "Tablist.yml":
	    		c=Loader.tab;
	    		break;
	    	case "Scoreboard.yml":
	    		c=Loader.sb;
	    		break;
	    	case "MultiWorlds.yml":
	    		c=Loader.mw;
	    		break;
	    	case "BossBar.yml":
	    		c=Loader.bb;
	    		break;
	    	case "ActionBar.yml":
	    		c=Loader.ac;
	    		break;
	    	case "Events.yml":
	    		c=Loader.events;
	    		break;
	    	case "Commands.yml":
	    		c=Loader.cmds;
	    		break;
	    	case "Animations.yml":
	    		c=Loader.anim;
	    		break;
	    	case "Rewards.yml":
	    		c=Loader.rewards;
	    		break;
	    	case "Translations/translation-en.yml":
	    		c=Loader.english;
	    		break;
	    	}
	    	if(s.endsWith(".txt"))continue;
	    	if(c!=null) {
	    		c.reload();
	    	}else c=new Config("ServerControlReloaded/"+s);
    		try {
    		URLConnection u = Loader.getInstance.getClass().getClassLoader().getResource("Configs/"+s).openConnection();
    		u.setUseCaches(false);
    		data.reload(StreamUtils.fromStream(u.getInputStream()));
    		}catch(Exception e) {}
	    	if(c.getData().merge(data, false, false))
	    		c.save();
	    	switch(s) {
	    	case "Kits.yml":
	    		Loader.kit=c;
	    		break;
	    	case "Placeholders.yml":
	    		Loader.plac=c;
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
	    	case "Rewards.yml":
	    		Loader.rewards=c;
	    		break;
	    	case "Translations/translation-en.yml":
	    		Loader.english=c;
	    		break;
	    	}
		}
	}
}
