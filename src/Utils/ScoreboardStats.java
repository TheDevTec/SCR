package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import me.Straiker123.ScoreboardAPIV2;
import me.Straiker123.TheAPI;

public class ScoreboardStats {
	public ScoreboardStats() {
		f=Loader.scFile;
	}
	static FileConfiguration f;
	 static HashMap<Player, ScoreboardAPIV2> setup = new HashMap<Player, ScoreboardAPIV2>();
	 public static void createScoreboard(Player p) {
		 if(!setup.containsKey(p))setup.put(p, TheAPI.getScoreboardAPIV2(p));
		 ScoreboardAPIV2 a=setup.get(p);
			String getName = null;
			 List<String> getLine = new ArrayList<String>();
			 if(setting.sb_world) {
				 if(f.getString("PerWorld."+p.getWorld().getName()+".Name")!=null)
					 getName="PerWorld."+p.getWorld().getName()+".Name";
				 else
					 getName=f.getString("Name");
				 if(f.getString("PerWorld."+p.getWorld().getName()+".Lines")!=null)
					 getLine=f.getStringList("PerWorld."+p.getWorld().getName()+".Lines");
				 else
					 getName=f.getString("Lines");
			 }else {
				 getName=f.getString("Name");
				 getLine=f.getStringList("Lines");
			 }
			a.setTitle(getName);
			for(String ss:getLine) {
				a.addLine(TheAPI.getPlaceholderAPI().setPlaceholders(p,TabList.replace(ss, p)));
			}
			 a.create();
		 }
	 	
		public static void removeScoreboard() {
			for(Player p:TheAPI.getOnlinePlayers()) {
				 p.setScoreboard(p.getServer().getScoreboardManager().getNewScoreboard());
		}
			setup.clear();
		}
			public static void removeScoreboard(Player p) {
					setup.remove(p);
					 p.setScoreboard(p.getServer().getScoreboardManager().getNewScoreboard());
}}