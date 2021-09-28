package me.devtec.servercontrolreloaded.utils;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.datakeeper.loader.YamlLoader;

public class Configs {
	@SuppressWarnings("unchecked")
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
    		YamlLoader data = new YamlLoader();
    		try {
    		data.load(new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml"));
    		}catch(Exception e) {}
	    	boolean change = false;
				try {
					if(c.getHeader()==null || c.getHeader()!=null && !c.getHeader().isEmpty() && (data.getHeader().isEmpty()||!c.getHeader().containsAll(data.getHeader()))) {
						c.getHeader().clear();
						c.getHeader().addAll(data.getHeader());
						change = true;
					}
					if(c.getFooter()==null || c.getFooter()!=null && !c.getFooter().isEmpty() && (data.getFooter().isEmpty()||!c.getFooter().containsAll(data.getFooter()))) {
						c.getFooter().clear();
						c.getFooter().addAll(data.getFooter());
						change = true;
					}
				}catch(Exception nope) {}
				try {
				for(Entry<String, Object[]> s : c.getData().getDataLoader().get().entrySet()) {
					Object[] o = c.getData().getOrCreateData(s.getKey());
					if(o[0]==null && s.getValue()[0]!=null) {
						o[0]=s.getValue()[0];
						try {
						o[2]=s.getValue()[2];
						}catch(Exception outOfBoud) {}
						change = true;
					}
					if(s.getValue()[1]!=null && !((List<String>) s.getValue()[1]).isEmpty()) {
						List<String> cc = (List<String>)o[1];
			    		if(cc==null || cc.isEmpty()) {
			    			if(c.getHeader()!=null && !c.getHeader().isEmpty() && ((List<String>)s.getValue()[1]).containsAll(c.getHeader())
		        					|| c.getFooter()!=null && !c.getFooter().isEmpty() && ((List<String>) s.getValue()[1]).containsAll(c.getFooter()))continue;
		        			o[1]= s.getValue()[1];
			    			change = true;
			    		}
					}
				}
				}catch(Exception err) {}
		    	data.reset();
				if(change)
			    	c.save();
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
			Config.loadConfig(Loader.getInstance, "Configs/Guis/shop.yml", "Guis/shop.yml");
			Config.loadConfig(Loader.getInstance, "Configs/Guis/item-buy.yml", "Guis/item-buy.yml");
		}
	}
	
	static final List<String> datas = Arrays.asList("Config.yml","Scoreboard.yml","Placeholders.yml","Tablist.yml","BossBar.yml","ActionBar.yml", "Animations.yml","Kits.yml","MultiWorlds.yml","Events.yml","Commands.yml","Translations/translation-en.yml","Translations/translation-cz.yml","Translations/translation-sk.yml","GUICreator.yml", "Rewards.yml");
	
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
	    	if(c.getData().merge(data, true, true))
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
