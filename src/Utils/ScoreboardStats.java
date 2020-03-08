package Utils;

import org.bukkit.entity.Player;

import me.Straiker123.TheAPI;

public class ScoreboardStats {
	  
	 public static void createScoreboard(Player p) {
		 if(Tasks.setup.containsKey(p) && Tasks.setup.get(p) != null)
		 Tasks.setup.get(p).create();
			 }
			 	
	public static void removeScoreboard() {
		for(Player p:TheAPI.getOnlinePlayers()) {
			Tasks.setup.remove(p);
			 p.setScoreboard(p.getServer().getScoreboardManager().getNewScoreboard());
	}
}}