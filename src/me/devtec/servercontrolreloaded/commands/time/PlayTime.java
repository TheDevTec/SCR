package me.devtec.servercontrolreloaded.commands.time;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.Tasks;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.sortedmap.RankingAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class PlayTime implements CommandExecutor {

	public static HashMap<UUID, Long> playtop = new HashMap<>(); //Player UUID || PlayTime
	public static RankingAPI<UUID, Long> ranks = new RankingAPI<>(playtop);

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		
		if(Loader.has(s, "PlayTime", "Time")) {
		if(!CommandsManager.canUse("Time.PlayTime", s)) {
			Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.Ping", s))));
			return true;
		}
			if(args.length==0) {
				Loader.sendMessages(s, "PlayTime.Time", Placeholder.c().add("%playtime%", StringUtils.timeToString( TabList.playtime( (Player) s) )));
				return true;
			}
			if(args.length>0) {
				if(args[0].equalsIgnoreCase("Help")) {
					Loader.Help(s, "PlayTime", "Time");
					return true;
				}
				if(args[0].equalsIgnoreCase("Top")) {
					if(Loader.has(s, "PlayTime", "Time", "Top")) {
						Loader.sendMessages(s, "PlayTime.PlayTop.Loading");
						new Tasker() {
							public void run() {
								if ( (TheAPI.getCooldownAPI("ServerControlReloaded").expired("scr_playtop") || playtop == null || playtop.size()==0) && task!=true) {
									TheAPI.getCooldownAPI("ServerControlReloaded").createCooldown("scr_playtop", 300*20); 
									for (UUID sa : TheAPI.getUsers()) {
										String n = LoaderClass.cache.lookupNameById(sa);
										if(n!=null) {
											long time = TabList.playtime(n);
											if(time>0)
												playtop.put(sa, time);
										}
									}
								}

								int pages = (int) Math.ceil((double) playtop.size() / 10);
								int page =args.length>1?StringUtils.getInt(args[1]):1;
								if(page<=0)page=1;
								if(pages<page)page=pages;
								--page;
								Loader.sendMessages(s, "PlayTime.PlayTop.Header", Placeholder.c().replace("%page%",(page+1)+"")
										.replace("%pages%", pages+""));
								
								RankingAPI<UUID, Long> tops = new RankingAPI<>(playtop);
								
								int min = page * 10;
								int max = ((page * 10) + 10);

								if (max > tops.size())
									max = tops.size();
								min=+1;
								for (int i = min; i<=max; i++) {
									UUID uuid = tops.getObject(i);
									String player = LoaderClass.cache.lookupNameById(uuid);
									Loader.sendMessages(s, "PlayTime.PlayTop.Top", Placeholder.c().replace("%position%", (i+(10*(page+1))-10) + "")
											.replace("%player%", player).replace("%playername%", player(s, player))
											.replace("%playtime%", StringUtils.timeToString(TabList.playtime(player))) );
								}
								
								Loader.sendMessages(s, "PlayTime.PlayTop.Footer", Placeholder.c().replace("%page%",(page+1)+"")
										.replace("%pages%", pages+""));
						}}.runTask();
						return true;
					}
					Loader.noPerms(s, "PlayTime", "Time", "Top");
					return true;
				}
				String player = args[0];
				if(TheAPI.existsUser(player)) {
					Loader.sendMessages(s, "PlayTime.Other", Placeholder.c().add("%playtime%", StringUtils.timeToString( TabList.playtime(player) ))
							.add("%player%", player).add("%target%", player));
					return true;
				}
				Loader.notExist(s, player);
				return true;
			}
		}
		Loader.noPerms(s, "PlayTime", "Time");
		return true;
	}
	
	public String player(CommandSender d, String s) {
		if (TheAPI.getPlayerOrNull(s) != null)
			return API.getPlayers(d).contains(TheAPI.getPlayerOrNull(s)) ? TheAPI.getPlayerOrNull(s).getDisplayName() : s;
		return s;
	}
	
	public static boolean task = false;
	
	public static String getTop(int pos) {
		if(playtop == null || playtop.size()==0) {
			Tasks.playTop();
		}
		if(ranks.getObject(pos)==null)
			return "-";
		UUID uuid = ranks.getObject(pos);
		String player = LoaderClass.cache.lookupNameById(uuid);
		
		if(player==null)
			return "-";
		return Loader.trans.getString("PlayTime.PlayTop.Top").replace("%position%", pos+ "")
				.replace("%player%", player).replace("%playername%", player)
				.replace("%playtime%", StringUtils.timeToString(TabList.playtime(player)));
	}
}
