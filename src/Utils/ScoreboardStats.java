package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import ServerControl.Loader;
import me.DevTec.ConfigAPI;
import me.DevTec.ScoreboardAPI;
import me.DevTec.TheAPI;
import me.DevTec.Other.ScoreboardType;

public class ScoreboardStats {
	public ScoreboardStats() {
		f = Loader.sb;
	}

	static ConfigAPI f;
	static HashMap<Player, ScoreboardAPI> setup = Maps.newHashMap(); //prevent recreating 1000x scoreboard

	public static void createScoreboard(Player p) {
		if (!setup.containsKey(p))
			setup.put(p, TheAPI.getScoreboardAPI(p, ScoreboardType.PACKETS));
		ScoreboardAPI a = setup.get(p);
		String getName = "SCR";
		List<String> getLine = new ArrayList<String>();
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
			a.setLine(line,TheAPI.getPlaceholderAPI().setPlaceholders(p, TabList.replace(ss, p)));
			--line;
		}
	}
	
	public static void removeScoreboard() {
		for (Player p : setup.keySet()) {
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