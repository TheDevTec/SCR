package me.DevTec.ServerControlReloaded.Utils;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.DataKeeper.Data;
import me.DevTec.TheAPI.Utils.Decompression.Decompression;

public class Configs {
	
	public static void load() {
		copyDefauts();
		String lang = Loader.config.getString("Options.Language");
		if(lang!=null) {
			if(!new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml").exists())
			lang="en";
		}else lang="en";
		Loader.trans = new Config("ServerControlReloaded/Translations/translation-"+lang+".yml");
	}

	private static void copyDefauts() {
		try {
			JarFile file = new JarFile(new File("plugins/"+new File(Loader.class.getProtectionDomain()
					  .getCodeSource().getLocation().getPath()).getName()));
			boolean found = false;
	    	Data data = new Data();
	    	Enumeration<? extends JarEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
		    	JarEntry entry = entries.nextElement();
		    	if(!entry.getName().startsWith("Configs/"))
		    		if(found)break;
		    		else continue;
		    	if(entry.getName().endsWith("/"))continue;
		    	found=true;
		    	data.reload(Decompression.getText(file.getInputStream(entry)));
		    	Config c = new Config("ServerControlReloaded/"+entry.getName().replaceFirst("Configs/", ""));
		    	c.setHeader(data.getHeader());
		    	c.setFooter(data.getFooter());
		    	for(String sr : data.getKeys(true)) {
		    		if(c.get(sr)==null) {
		    			c.set(sr, data.get(sr));
		    			c.setComments(sr, data.getComments(sr));
		    		}
		    	}
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
		    	case "Events.yml":
		    		Loader.events=c;
		    		break;
		    	case "Commands.yml":
		    		Loader.cmds=c;
		    		break;
		    	}
			}
			data.clear();
			file.close();
	}catch(Exception erer) {}
	}
}
