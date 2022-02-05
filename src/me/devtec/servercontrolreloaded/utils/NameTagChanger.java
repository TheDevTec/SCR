package me.devtec.servercontrolreloaded.utils;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.theapi.apis.NameTagAPI;

public class NameTagChanger {
	static final HashMap<Player, NameTagAPI> t = new HashMap<>();
	static final AnimationManager anim = new AnimationManager();

	public static void setNameTag(Player p, String prefix, String suffix) {
		Tasks.regPlayer(p);
		if (setting.tab_sort) {
			String sortGroup = TabList.sorting.get(API.getGroup(p))+Tasks.sss.get(p.getName());
			if (setting.tab_nametag) {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n = new NameTagAPI(p, sortGroup));
					//t.put(p, n = TheAPI.getNameTagAPI(p, anim.replace(p,prefix), anim.replace(p,suffix)));
				/*n.setPrefix(anim.replace(p,prefix));
				n.setSuffix(anim.replace(p,suffix));
				n.setNameTag(sortGroup);*/
				n.set(null, anim.replace(p,prefix), anim.replace(p,suffix));
			} else {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n = new NameTagAPI(p, sortGroup));
				/*	t.put(p, n=TheAPI.getNameTagAPI(p, null, null));
				n.setPrefix(null);
				n.setSuffix(null);
				n.setNameTag(sortGroup);*/
				n.set(null, null, null);
			}
		} else {
			if (setting.tab_nametag) {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n = new NameTagAPI(p, null));
				n.set(null, anim.replace(p,prefix), anim.replace(p,suffix));
				n.setName(Tasks.sss.get(p.getName()));
				/*	t.put(p, n=TheAPI.getNameTagAPI(p, anim.replace(p,prefix), anim.replace(p,suffix)));
				n.setPrefix(anim.replace(p,prefix));
				n.setSuffix(anim.replace(p,suffix));
				n.setNameTag(Tasks.sss.get(p.getName()));*/
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
		//if(a!=null)a.resetNameTag();
		if(a!=null)a.reset();
	}
}