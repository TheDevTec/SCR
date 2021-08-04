package me.devtec.servercontrolreloaded.commands.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.playtime.PlayTimeUtils;
import me.devtec.servercontrolreloaded.utils.playtime.PlayTimeUtils.PlayTimeType;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.sortedmap.RankingAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class PlayTime implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		
		if(Loader.has(s, "PlayTime", "Time")) {
		if(!CommandsManager.canUse("Time.PlayTime", s)) {
			Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.Ping", s))));
			return true;
		}
			if(args.length==0) {
				Loader.sendMessages(s, "PlayTime.Time.Global", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( (Player) s) )));
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
								if ( (TheAPI.getCooldownAPI("ServerControlReloaded").expired("scr_playtop") || PlayTimeUtils.playtop == null || PlayTimeUtils.playtop.size()==0) && PlayTimeUtils.task!=true) {
									TheAPI.getCooldownAPI("ServerControlReloaded").createCooldown("scr_playtop", 300*20); 
									for (UUID sa : TheAPI.getUsers()) {
										String n = LoaderClass.cache.lookupNameById(sa);
										if(n!=null) {
											int time = PlayTimeUtils.playtime(n);
											if(time>0)
												PlayTimeUtils.playtop.put(sa, time);
										}
									}
								}

								int pages = (int) Math.ceil((double) PlayTimeUtils.playtop.size() / 10);
								int page =args.length>1?StringUtils.getInt(args[1]):1;
								if(page<=0)page=1;
								if(pages<page)page=pages;
								--page;
								Loader.sendMessages(s, "PlayTime.PlayTop.Header", Placeholder.c().replace("%page%",(page+1)+"")
										.replace("%pages%", pages+""));
								
								RankingAPI<UUID, Integer> tops = new RankingAPI<>(PlayTimeUtils.playtop);
								
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
											.replace("%playtime%", StringUtils.timeToString(PlayTimeUtils.playtime(player))) );
								}
								
								Loader.sendMessages(s, "PlayTime.PlayTop.Footer", Placeholder.c().replace("%page%",(page+1)+"")
										.replace("%pages%", pages+""));
						}}.runTask();
						return true;
					}
					Loader.noPerms(s, "PlayTime", "Time", "Top");
					return true;
				}
				/*
				 * OTHER SUBCOMMANDS
				 */
				
				GameMode mode = null;
				try {
					mode = GameMode.valueOf(args[0].toUpperCase());
				} catch (Exception e) {	}
				
				if(mode!=null ) {
					if(args.length==1) {
						if(Loader.has(s, "PlayTime", "Time", "Gamemode")) {
							Loader.sendMessages(s, "PlayTime.Time.Gamemode", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( (Player) s, PlayTimeType.GAMEMODE, mode, null) ))
									.add("%player%", s.getName()).add("%gamemode%", mode.name()));
							return true;
						}
						Loader.noPerms(s, "PlayTime", "Time", "Gamemode");
						return true;
					}
					if(args.length==2) {
						World w = null;
						try {
							w = Bukkit.getWorld(args[1]);
						} catch (Exception e) {	}
						
						if(w!=null) {
							if(Loader.has(s, "PlayTime", "Time", "WorldGamemode")) {
								Loader.sendMessages(s, "PlayTime.Time.WorldGamemode", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( (Player) s, PlayTimeType.WORLDGAMEMODE, mode, w) ))
										.add("%player%", s.getName()).add("%gamemode%", mode.name()).add("%world%", w.getName()));
								return true;
							}
							Loader.noPerms(s, "PlayTime", "Time", "WorldGamemode");
							return true;
						}
						Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", args[1]));
						return true;
					}
				}
				
				World w = null;
				try {
					w = Bukkit.getWorld(args[0]);
				} catch (Exception e) {	}
				
				if(w!=null) {
					if(Loader.has(s, "PlayTime", "Time", "World")) {
						Loader.sendMessages(s, "PlayTime.Time.World", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( (Player) s, PlayTimeType.WORLD, null, w) ))
								.add("%player%", s.getName()).add("%world%", w.getName()));
						return true;
					}
					Loader.noPerms(s, "PlayTime", "Time", "World");
					return true;
				}
				
				
				/*
				 *  TARGET
				 */
				String player = args[0];
				if(TheAPI.existsUser(player)) {
					if(Loader.has(s, "PlayTime", "Time", "Other")) {
						if(args.length==1) {
							Loader.sendMessages(s, "PlayTime.Other.Global", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime(player) ))
									.add("%player%", player).add("%target%", player));
							return true;
						}
						GameMode m = null;
						try {
							m = GameMode.valueOf(args[1].toUpperCase());
						} catch (Exception e) {	}
						
						if(m!=null) {
							if(args.length==2) {
								if(Loader.has(s, "PlayTime", "Time", "Gamemode")) {
									Loader.sendMessages(s, "PlayTime.Other.Gamemode", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( player, PlayTimeType.GAMEMODE, m, null) ))
											.add("%player%", player).add("%target%", player).add("%gamemode%", m.name()));
									return true;
								}
								Loader.noPerms(s, "PlayTime", "Time", "Gamemode");
								return true;
							}
							if(args.length==3) {
								if(!Loader.has(s, "PlayTime", "Time", "WorldGamemode")) {
									Loader.noPerms(s, "PlayTime", "Time", "WorldGamemode");
									return true;
								}
								World ww = null;
								try {
									ww =  Bukkit.getWorld(args[2]);
								} catch (Exception e) {	}
								
								if(ww!=null) {
									Loader.sendMessages(s, "PlayTime.Other.WorldGamemode", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( player, PlayTimeType.WORLDGAMEMODE, m, ww) ))
											.add("%player%", player).add("%target%", player).add("%gamemode%", m.name()).add("%world%", ww.getName()));
									return true;
								}
								Loader.sendMessages(s, "Missing.World", Placeholder.c().add("%world%", args[2]));
								return true;
							}
						}
						World ww = null;
						try {
							ww =  Bukkit.getWorld(args[1]);
						} catch (Exception e) {	}
						
						if(ww!=null) {
							Loader.sendMessages(s, "PlayTime.Other.World", Placeholder.c().add("%playtime%", StringUtils.timeToString( PlayTimeUtils.playtime( player, PlayTimeType.WORLD, null, ww) ))
									.add("%player%", player).add("%target%", player).add("%world%", ww.getName()));
							return true;
						}
						Loader.Help(s, "PlayTime", "Time");
						return true;
					}
					Loader.noPerms(s, "PlayTime", "Time", "Other");
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

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String arg2, String[] args) {
		if (Loader.has(s, "PlayTime", "Time")) {
			if(args.length==1) {
				if(args[0].isEmpty()) {
					List<String> a = new ArrayList<>();
					if(Loader.has(s, "PlayTime", "Time", "Other")) {
						for(Player p : TheAPI.getOnlinePlayers())
							a.add(p.getName());
					}
					a.add("Help");
					if(Loader.has(s, "PlayTime", "Time", "Top")) a.add("Top");
					if(Loader.has(s, "PlayTime", "Time", "Gamemode")) a.add("<GAMEMODE>");
					if(Loader.has(s, "PlayTime", "Time", "World")) a.add("<world>");
					
					return StringUtils.copyPartialMatches(args[0], a);
				}else {
					List<String> a = new ArrayList<>();
					for(GameMode m : GameMode.values()) {
						if(m.name().startsWith(args[0]) ||m.name().toLowerCase().startsWith(args[0]))
							a.add(m.name());
					}
					for(World w : Bukkit.getWorlds()) {
						if(w.getName().startsWith(args[0]) ||w.getName().toLowerCase().startsWith(args[0]))
							a.add(w.getName());
					}
					
					if(Loader.has(s, "PlayTime", "Time", "Other")) {
						for(Player p : TheAPI.getOnlinePlayers())
							if(p.getName().startsWith(args[0])||p.getName().toLowerCase().startsWith(args[0]) ) a.add(p.getName());
					}
					if("Help".startsWith(args[0]) || "Help".toLowerCase().startsWith(args[0]) ) a.add("Help");
					if(Loader.has(s, "PlayTime", "Time", "Top") && ("Top".startsWith(args[0]) || "Top".toLowerCase().startsWith(args[0]) )) a.add("Top");

					return StringUtils.copyPartialMatches(args[0], a);
				}
			}
			/*
			 *  /playtime <GAMEMODE> <world>
			 *  /playtime <GAMEMODE>
			 *  /playtime <world>
			 *  
			 *  /playtime <player>
			 *  /playtime <player> <GAMEMODE>
			 *  /playtime <player> <world>
			 *  /playtime <player> <GAMEMODE> <world>
			 */
			if(args.length==2) {
				List<String> a = new ArrayList<>();
				
				GameMode mode = null;
				try {
					mode = GameMode.valueOf(args[0].toUpperCase());
				} catch (Exception e) {	}
				
				if(mode!=null) {
					if(Loader.has(s, "PlayTime", "Time", "WorldGamemode")) {
						for(World w : Bukkit.getWorlds())
							a.add(w.getName());
					}
					return StringUtils.copyPartialMatches(args[1], a);
				}
				
				Player p = TheAPI.getPlayerOrNull(args[0]);
				if(p!=null) {
					if(args[1].isEmpty()) {
						if(Loader.has(s, "PlayTime", "Time", "Gamemode")) a.add("<GAMEMODE>");
						if(Loader.has(s, "PlayTime", "Time", "World")) a.add("<world>");
						return StringUtils.copyPartialMatches(args[1], a);
					}else {
						for(GameMode m : GameMode.values()) {
							if(m.name().startsWith(args[1]) ||m.name().toLowerCase().startsWith(args[1]))
								a.add(m.name());
						}
						for(World w : Bukkit.getWorlds()) {
							if(w.getName().startsWith(args[1]) ||w.getName().toLowerCase().startsWith(args[1]))
								a.add(w.getName());
						}
						return StringUtils.copyPartialMatches(args[1], a);
					}
				}
			}
			if(args.length==3) {
				List<String> a = new ArrayList<>();

				Player p = TheAPI.getPlayerOrNull(args[0]);
				if(p!=null) {
					GameMode mode = null;
					try {
						mode = GameMode.valueOf(args[1].toUpperCase());
					} catch (Exception e) {	}
					
					if(mode!=null) {
						if(Loader.has(s, "PlayTime", "Time", "WorldGamemode")) {
							for(World w : Bukkit.getWorlds())
								a.add(w.getName());
						}
						return StringUtils.copyPartialMatches(args[2], a);
					}
				}
			}
		}
		return Arrays.asList();
	}
	
}
