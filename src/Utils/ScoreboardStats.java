package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import ServerControl.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.ScoreboardAPI.ScoreboardAPI;
import me.DevTec.TheAPI.ScoreboardAPI.ScoreboardType;
import me.DevTec.TheAPI.TheAPI;

public class ScoreboardStats {
	
	static HashMap<Player, ScoreboardAPI> setup = new HashMap<>(); //prevent recreating 1000x scoreboard

	public static void createScoreboard(Player p) {
		if (!setup.containsKey(p))
			setup.put(p, TheAPI.getScoreboardAPI(p, ScoreboardType.PACKETS));
		ScoreboardAPI a = setup.get(p);
		String getName = "SCR";
		List<String> getLine = new ArrayList<String>();
		Config f = Loader.sb;
		if (setting.sb_world) {
			if (f.getString("PerWorld." + p.getWorld().getName() + ".Name") != null)
				getName = "PerWorld." + p.getWorld().getName() + ".Name";
			else
				getName = f.getString("Name");
			if (f.getString("PerWorld." + p.getWorld().getName() + ".Lines") != null)
				getLine = f.getStringList("PerWorld." + p.getWorld().getName() + ".Lines");
			else
				getName = f.getString("Lines");
		} else {
			getName = f.getString("Name");
			getLine = f.getStringList("Lines");
		}
		a.setDisplayName(getName);
		int line = getLine.size();
		for (String ss : getLine) {
			a.setLine(line,PlaceholderAPI.setPlaceholders(p, TabList.replace(ss, p)));
			--line;
		}
	}
	
	public static void removeScoreboard() {
		for (Player p : setup.keySet()) {
			if(p.isOnline())
			setup.get(p).destroy();
		}
		setup.clear();
	}

	public static void removeScoreboard(Player p) {
		if(setup.containsKey(p))
		setup.get(p).destroy();
		setup.remove(p);
	}
}