package me.devtec.servercontrolreloaded.utils;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.theapi.apis.NameTagAPI;
import me.devtec.theapi.utils.StringUtils;

public class NameTagChanger {
	static final HashMap<Player, NameTagAPI> t = new HashMap<>();
	static final AnimationManager anim = new AnimationManager();

	public static void setNameTag(Player p, String prefix, String suffix) {
		Tasks.regPlayer(p);
		if (setting.tab_sort) {
			String sortGroup = TabList.sorting.get(API.getGroup(p))+Tasks.sss.get(p.getName());
			NameTagAPI n = t.get(p);
			if (n==null)
				t.put(p, n = new NameTagAPI(p, sortGroup));
			if (setting.tab_nametag)
				n.set(getColor(anim.replace(p,prefix)), anim.replace(p,prefix), anim.replace(p,suffix));
			else
				n.set(ChatColor.WHITE, "", "");
			n.send(API.getPlayersThatCanSee(p).toArray(new Player[0]));
		} else {
			if (setting.tab_nametag) {
				NameTagAPI n = t.get(p);
				if (n==null)
					t.put(p, n = new NameTagAPI(p, Tasks.sss.get(p.getName())));
				n.set(getColor(anim.replace(p,prefix)), anim.replace(p,prefix), anim.replace(p,suffix));
				n.send(API.getPlayersThatCanSee(p).toArray(new Player[0]));
			}
		}
	}
	
	public static void updateVisibility(Player to) {
		for(Entry<Player, NameTagAPI> e : t.entrySet()) {
			if(e.getKey()!=to) {
				e.getValue().send(to);
			}
		}
	}
	
	public static void removeVisibility(Player to) {
		for(Entry<Player, NameTagAPI> e : t.entrySet()) {
			e.getValue().reset(to);
		}
	}
	
	public static void update() {
		anim.update();
	}

	public static void remove(Player p) {
		if(p==null || p.getScoreboard()==null)return;
		for(Team t : p.getScoreboard().getTeams())t.unregister();
		NameTagAPI a = t.remove(p);
		if(a!=null)a.reset(API.getPlayersThatCanSee(p).toArray(new Player[0]));
	}
	
	private static ChatColor getColor(String lastColors) {
		if(lastColors==null||lastColors.isEmpty())return null;
		lastColors=StringUtils.getLastColors(lastColors);
		if(lastColors.isEmpty())return null;
		return ChatColor.getByChar(lastColors.charAt(0));
	}
}