package me.devtec.servercontrolreloaded.utils;

import java.io.File;
import java.io.FileWriter;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.datakeeper.loader.YamlLoader;
import me.devtec.theapi.utils.reflections.Ref;

public class Configs {
	@SuppressWarnings("unchecked")
	public static void load(boolean settingMessage) {
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
    		data.load(StreamUtils.fromStream(new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml")));
    		}catch(Exception e) {}
	    	boolean change = false;
	    	try {
	    		for(Entry<String, Object[]> s : data.get().entrySet()) {
	    			if(c.get(s.getKey())==null && s.getValue()[0]!=null) {
	    				Object[] o = (Object[]) Ref.invoke(c.getData(),"getOrCreateData",s.getKey());
	    				o[0]=s.getValue()[0];
	    				try {
	    				o[2]=s.getValue()[2]==null?null:s.getValue()[2]+"";
	    				}catch(Exception outOfBoud) {
	    					try {
	    						o[2]=s.getValue()[0]==null?null:s.getValue()[0]+"";
	    					}catch(Exception outOfBoud2) {
	    						
	    					}
	    				}
	    				change = true;
	    			}
	    			try {
	    				if(data.getHeader()==null || data.getHeader()!=null && !data.getHeader().isEmpty() && (c.getHeader().isEmpty()||!data.getHeader().containsAll(c.getHeader()))) {
	    					c.setHeader(data.getHeader());
	    					change = true;
	    				}
	    				if(data.getFooter()==null ||data.getFooter()!=null && !data.getFooter().isEmpty() && (c.getFooter().isEmpty()||!data.getFooter().containsAll(c.getFooter()))) {
	    					c.setFooter(data.getFooter());
	    					change = true;
	    				}
	    			}catch(Exception nope) {}
	    			if(s.getValue()[1]!=null && !((List<String>) s.getValue()[1]).isEmpty())
	        		if(c.getComments(s.getKey())==null || c.getComments(s.getKey()).isEmpty()) {
	        			if(c.getHeader()!=null && !c.getHeader().isEmpty() && ((List<String>)s.getValue()[1]).containsAll(c.getHeader())
	        					|| c.getFooter()!=null && !c.getFooter().isEmpty() && ((List<String>) s.getValue()[1]).containsAll(c.getFooter()))continue;
	        			c.setComments(s.getKey(), (List<String>)s.getValue()[1]);
	        			change = true;
	        		}
	    		}
	    	}catch(Exception error) {}
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
	}
	
	static List<String> datas = Arrays.asList("Config.yml","Rules.txt","Colors.txt","Scoreboard.yml","Placeholders.yml","Tablist.yml","BossBar.yml","ActionBar.yml", "Animations.yml","Kits.yml","MultiWorlds.yml","Events.yml","Commands.yml","Translations/translation-en.yml","Translations/translation-cz.yml","Translations/translation-sk.yml","GUICreator.yml");
	
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
	    	case "GUICreator.yml":
	    		c=Loader.guicreator;
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
	    	case "Translations/translation-en.yml":
	    		c=Loader.english;
	    		break;
	    	case "Colors.txt":
    			try {
		    		File f = new File("plugins/ServerControlReloaded/Colors.txt");
		    		if(!f.exists()) {
		    			f.getParentFile().mkdirs();
						f.createNewFile();
						URLConnection u = Loader.getInstance.getClass().getClassLoader().getResource("Configs/"+s).openConnection();
			    		FileWriter writer = new FileWriter(f);
			    		String read = StreamUtils.fromStream(u.getInputStream());
			    		writer.write(read);
			    		Loader.colorsText=read.split(System.lineSeparator());
			    		writer.close();
		    		}else
			    		Loader.colorsText=StreamUtils.fromStream(f).split(System.lineSeparator());
		    	} catch (Exception e) {
				}
    			break;
	    	case "Rules.txt":
    			try {
		    		File f = new File("plugins/ServerControlReloaded/Rules.txt");
		    		if(!f.exists()) {
		    			f.getParentFile().mkdirs();
						f.createNewFile();
						URLConnection u = Loader.getInstance.getClass().getClassLoader().getResource("Configs/"+s).openConnection();
			    		FileWriter writer = new FileWriter(f);
			    		String read = StreamUtils.fromStream(u.getInputStream());
			    		writer.write(read);
			    		Loader.rulesText=read.split(System.lineSeparator());
			    		writer.close();
		    		}else
			    		Loader.rulesText=StreamUtils.fromStream(f).split(System.lineSeparator());
		    	} catch (Exception e) {
				}
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
	    	boolean change = c.getData().merge(data, true, true);
	    	if(change)
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
			case "GUICreator.yml":
				Loader.guicreator=c;
				break;
	    	case "Translations/translation-en.yml":
	    		Loader.english=c;
	    		break;
	    	}
		}
	}
}
