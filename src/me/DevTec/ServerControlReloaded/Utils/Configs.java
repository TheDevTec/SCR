package me.DevTec.ServerControlReloaded.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.DataKeeper.Data;
import me.DevTec.TheAPI.Utils.Reflections.Ref;

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
			JarFile file = new JarFile(new File("plugins/"+new File(Ref.getClass("me.DevTec.ServerControlReloaded.SCR.Loader").getProtectionDomain()
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
		    	BufferedReader is = new BufferedReader(new InputStreamReader(file.getInputStream(entry), StandardCharsets.UTF_8));
		    	StringBuffer s = new StringBuffer();
		    	String readBytes;
		    	while ((readBytes = is.readLine()) != null)
		    		s.append(readBytes+System.lineSeparator());
		    	data.reload(s.toString());
		    	Config c = new Config("ServerControlReloaded/"+entry.getName().replaceFirst("Configs/", ""));
		    	boolean add = false;
		    	for(String sr : data.getKeys(true)) {
		    		if(!c.exists(sr)) {
		    			add=true;
		    			c.set(sr, data.get(sr));
		    			c.setComments(sr, data.getLines(sr));
		    		}
		    	}
		    	if(add)
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
