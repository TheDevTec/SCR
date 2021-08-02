package me.devtec.servercontrolreloaded.utils;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.NameTagAPI;

public class NameTagChanger {
	static HashMap<Player, NameTagAPI> t = new HashMap<>();
	static AnimationManager anim = new AnimationManager();

	public static void setNameTag(Player p, String prefix, String suffix) {
		Tasks.regPlayer(p);
		if (setting.tab_sort) {
			String sortGroup = TabList.sorting.get(API.getGroup(p))+Tasks.sss.get(p.getName());
			if (setting.tab_nametag) {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n=TheAPI.getNameTagAPI(p, anim.replace(p,prefix), anim.replace(p,suffix)));
				n.setPrefix(anim.replace(p,prefix));
				n.setSuffix(anim.replace(p,suffix));
				n.setNameTag(sortGroup);
			} else {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n=TheAPI.getNameTagAPI(p, null, null));
				n.setPrefix(null);
				n.setSuffix(null);
				n.setNameTag(sortGroup);
			}
		} else {
			if (setting.tab_nametag) {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n=TheAPI.getNameTagAPI(p, anim.replace(p,prefix), anim.replace(p,suffix)));
				n.setPrefix(anim.replace(p,prefix));
				n.setSuffix(anim.replace(p,suffix));
				n.setNameTag(Tasks.sss.get(p.getName()));
			}
		}
	}
	
	public static void update() {
		anim.update();
	}

	public static void remove(Player p) {
		if(p==null || p.getScoreboard()==null)return;
		for(Team t : p.getScoreboard().getTeams())t.unregister();
		NameTagAPI a = t.remove(p);
		if(a!=null)a.resetNameTag();
	}
}