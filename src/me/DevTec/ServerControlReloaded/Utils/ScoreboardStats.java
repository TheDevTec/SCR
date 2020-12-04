package me.DevTec.ServerControlReloaded.Utils;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.ScoreboardAPI.ScoreboardAPI;
import me.DevTec.TheAPI.ScoreboardAPI.ScoreboardType;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedSet;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.UnsortedMap;

public class ScoreboardStats {
	public static Set<String> toggled = new UnsortedSet<>();
	static UnsortedMap<String, ScoreboardAPI> setup = new UnsortedMap<>(); //prevent of recreating unused scoreboards

	public static void createScoreboard(Player p) {
		if (!setup.containsKey(p.getName()))
			setup.put(p.getName(), new ScoreboardAPI(p, ScoreboardType.PACKETS, 0));
		ScoreboardAPI a = setup.get(p.getName());
		if(toggled.contains(p.getName()) && isToggleable(p)) {
			a.destroy();
			return;
		}
		Config f = Loader.sb;
		String s = f.getString("PerWorld." + p.getWorld().getName() + ".Name");
		if(s==null)
			s = f.getString("PerPlayer." + p.getName() + ".Name");
		if(s==null)
			s = f.getString("Name");
		List<String> b = f.getStringList("PerWorld." + p.getWorld().getName() + ".Lines");
		if(b.isEmpty())
			b = f.getStringList("PerPlayer." + p.getName() + ".Lines");
		if(b.isEmpty())
			b = f.getStringList("Lines");
		a.setDisplayName(AnimationManager.replace(p,s));
		if(a.getLines().size()>b.size())
		a.removeUpperLines(b.size()-1);
		for (int i = 0; i < b.size(); ++i)
			a.setLine(i, AnimationManager.replace(p, b.get(i)));
	}
	
	public static void removeScoreboard() {
		for (Entry<String, ScoreboardAPI> p : setup.entrySet())
			p.getValue().destroy();
		setup.clear();
	}

	public static void removeScoreboard(Player p) {
		if(setup.containsKey(p.getName()))
		setup.get(p.getName()).destroy();
		setup.remove(p.getName());
	}

	public static boolean isToggleable(Player s) {
		boolean d = Loader.sb.getBoolean("PerWorld." + s.getWorld().getName() + ".Toggleable");
		if(!Loader.sb.exists("PerWorld." + s.getWorld().getName() + ".Toggleable"))
			d = Loader.sb.getBoolean("PerPlayer." + s.getName() + ".Toggleable");
		if(!Loader.sb.exists("PerWorld." + s.getWorld().getName() + ".Toggleable"))
			d = Loader.sb.getBoolean("Options.Toggleable");
		return d;
	}
}