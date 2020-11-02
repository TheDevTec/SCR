package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ServerControl.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.DataKeeper.Data;
import me.DevTec.TheAPI.Utils.Reflections.Ref;

public class Configs {
	
	public static void load() {
		copyDefauts();
		Loader.config = new Config("ServerControlReloaded/Config.yml");
		String lang = Loader.config.getString("Options.Language");
		if(lang!=null) {
			if(!new File("plugins/ServerControlReloaded/Translations/translation-"+lang+".yml").exists())
			lang="en";
		}else lang="en";
		Loader.trans = new Config("ServerControlReloaded/Translations/translation-"+lang+".yml");
		Loader.cmds = new Config("ServerControlReloaded/Commands.yml");
		Loader.events = new Config("ServerControlReloaded/Events.yml");
		Loader.mw=new Config("ServerControlReloaded/MultiWorlds.yml");
		Loader.sb=new Config("ServerControlReloaded/Scoreboard.yml");
		Loader.tab=new Config("ServerControlReloaded/Tablist.yml");
		Loader.kit=new Config("ServerControlReloaded/Kits.yml");
	}

	private static void copyDefauts() {
		try {
			JarFile file = new JarFile(new File("plugins/"+new File(Ref.getClass(Loader.getInstance.getDescription().getMain()).getProtectionDomain()
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
		    	found= true;
		    	BufferedReader is = new BufferedReader(new InputStreamReader(file.getInputStream(entry), StandardCharsets.UTF_8));
		    	String s = "";
		    	String readBytes;
		    	while ((readBytes = is.readLine()) != null)
		    		s+=readBytes+System.lineSeparator();
		    	data.reload(s);
		    	Config c = new Config("ServerControlReloaded/"+entry.getName().replaceFirst("Configs/", ""));
		    	for(String sr : data.getKeys(true)) {
		    		if(!c.exists(sr)) {
		    			c.set(sr, data.get(sr));
		    			c.setComments(sr, data.getLines(sr));
		    		}
		    	}
		    	c.save();
			}
			file.close();
	}catch(Exception erer) {}
	}
}
