package me.devtec.servercontrolreloaded.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.bansystem.Accounts;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.API.TeleportLocation;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.scr.events.BanlistUnbanEvent;
import me.devtec.servercontrolreloaded.scr.events.BanlistUnjailEvent;
import me.devtec.servercontrolreloaded.scr.events.BanlistUnmuteEvent;
import me.devtec.servercontrolreloaded.utils.setting.DeathTp;
import me.devtec.servercontrolreloaded.utils.bungeecord.BungeeListener;
import me.devtec.servercontrolreloaded.utils.playtime.PlayTimeUtils;
import me.devtec.servercontrolreloaded.utils.punishment.SPunishmentAPI;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.listener.Event;
import me.devtec.theapi.utils.listener.EventHandler;
import me.devtec.theapi.utils.listener.Listener;
import me.devtec.theapi.utils.listener.events.ServerListPingEvent;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.nms.NMSAPI.ChatType;
import me.devtec.theapi.utils.packetlistenerapi.PacketListener;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.serverlist.PlayerProfile;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class Tasks {
	
	public static final ArrayList<String> players = new ArrayList<>();
	static final ArrayList<Integer> tasks = new ArrayList<>();
	static final Map<String, String> sss = new HashMap<>();
	static Loader a;
	static int tests;
	static final Listener l = new Listener() {
		@EventHandler
		public void onTag(ServerListPingEvent e) {
			String path = "Options.serverlist."+(Loader.config.getBoolean("Options.serverlist.maintenance.use") && setting.lock_server ? "maintenance" : Loader.config.getString("Options.serverlist.selection"))+".";
			int vv=0;
			if(Loader.config.getBoolean(path+"players.hide-vanished")) {
				Iterator<PlayerProfile> p = e.getPlayersText().iterator();
				while(p.hasNext()) {
					PlayerProfile profile = p.next();
					Player player = Bukkit.getPlayer(profile.getUUID());
					if(player!=null && API.hasVanish(player)) {
						p.remove();
						++vv;
					}else
						profile.setName(TabList.replace(Loader.config.getString(path+"players.playername-format"), Bukkit.getPlayer(profile.getUUID()), true));
				}
			}
			int vanished = vv;
			int rOnline = TheAPI.getOnlineCount(), online = rOnline-vanished;
			
			if(Loader.config.exists(path+"protocol") && Loader.config.getInt(path+"protocol")!=-1)
				e.setProtocol(StringUtils.getInt(TabList.replace(Loader.config.getString(path+"protocol"), null, false)));
			if(Loader.config.exists(path+"players")) {
				if(Loader.config.exists(path+"players.online"))
					e.setOnlinePlayers(StringUtils.getInt(TabList.replace(Loader.config.getString(path+"players.online").replace("%real_online%", online+"").replace("%online%", online+"").replace("%vanished%", vanished+"").replace("%max_players%", TheAPI.getMaxPlayers()+""), null, true)));
				if(Loader.config.exists(path+"players.max"))
					e.setMaxPlayers(StringUtils.getInt(TabList.replace(Loader.config.getString(path+"players.max").replace("%real_online%", online+"").replace("%online%", online+"").replace("%vanished%", vanished+"").replace("%max_players%", TheAPI.getMaxPlayers()+""), null, true)));
				List<String> list = Loader.config.getStringList(path+"players.list");
				if(!list.isEmpty()) {
					list.replaceAll(a -> TabList.replace(a.replace("%real_online%", online+"").replace("%online%", online+"").replace("%vanished%", vanished+"").replace("%max_players%", TheAPI.getMaxPlayers()+""), null, true));
					List<PlayerProfile> profiles = new ArrayList<>(list.size());
					for(String s : list)
						profiles.add(new PlayerProfile(s));
					e.setPlayersText(profiles);
				}
			}
			if(Loader.config.exists(path+"motd")) {
				if(Loader.config.getString(path+"motd.0")!=null || Loader.config.getString(path+"motd.1")!=null)
				if(!(Loader.config.getString(path+"motd.0")+"").isEmpty() && !(Loader.config.getString(path+"motd.1")+"").isEmpty())
					e.setMotd(TabList.replace(((Loader.config.getString(path+"motd.0")==null?"":Loader.config.getString(path+"motd.0"))+"\n"+(Loader.config.getString(path+"motd.1")==null?"":Loader.config.getString(path+"motd.1"))).replace("%real_online%", online+"").replace("%online%", online+"").replace("%vanished%", vanished+"").replace("%max_players%", TheAPI.getMaxPlayers()+""), null, true));
			}
			if(Loader.config.exists(path+"icon") && !Loader.config.getString(path+"icon").isEmpty())
				e.setFalvicon(TabList.replace(Loader.config.getString(path+"icon").replace("%real_online%", online+"").replace("%online%", online+"").replace("%vanished%", vanished+"").replace("%max_players%", TheAPI.getMaxPlayers()+""), null, true));
			if(Loader.config.exists(path+"serverversion") && !Loader.config.getString(path+"serverversion").isEmpty())
				e.setVersion(TabList.replace(Loader.config.getString(path+"serverversion").replace("%real_online%", online+"").replace("%online%", online+"").replace("%vanished%", vanished+"").replace("%max_players%", TheAPI.getMaxPlayers()+""), null, true));
		}
	};
	static final PacketListener sleepSkipMessage = new PacketListener() {
		final Class<?> chat = Ref.nmsOrOld("network.protocol.game.PacketPlayOutChat", "PacketPlayOutChat");
		final Class<?> chatmessage = Ref.nmsOrOld("network.chat.ChatMessage", "ChatMessage");
		@Override
		public boolean PacketPlayOut(String player, Object packet, Object channel) {
			if(player==null)return false;
			if(packet.getClass()==chat) {
				Object nms = Ref.get(packet, "a");
				if(nms!=null)
				if(nms.getClass()==chatmessage) {
					String key = (String)Ref.invoke(nms, "getKey");
					return key.equals("sleep.skipping_night") || key.equals("sleep.players_sleeping");
				}
			}
			return false;
		}

		@Override
		public boolean PacketPlayIn(String player, Object packet, Object channel) {
			return false;
		}
	};

	public static void unload() {
		for (int t : tasks)
			Scheduler.cancelTask(t);
		tests = 0;
		sleepSkipMessage.unregister();
		l.unregister();
		tasks.clear();
		PlayTimeUtils.unloadRewards();
	}

	public static void load() {
		a = Loader.getInstance;
		sss.clear();
		if(TheAPI.isNewerThan(16) && setting.singeplayersleep)
			sleepSkipMessage.register();
		if(Loader.hasBungee)
			bungeecordPlayers();
		players.clear();
		if(Loader.config.getBoolean("Options.serverlist.enabled"))
			l.register();
		if (setting.am)
			automessage();
		if (setting.vip)
			vipslot();

		if (setting.tab) {
			for(Player p : TheAPI.getOnlinePlayers())regPlayer(p);
			tab();
		}

		if (setting.save)
			savetask();
		
		other();
		tempfly();
		tempGamemode();
		playTime();
		PlayTimeUtils.loadRewards();
		punishmentApi();
	}

	private static void punishmentApi() {
		tasks.add(new Tasker() {
			public void run() {
				for(String user : me.devtec.servercontrolreloaded.utils.punishment.SPunishmentAPI.data.getKeys("u")) {
					for(Punishment push : TheAPI.getPunishmentAPI().getPunishments(user)) {
						if(push.getExpire()<=0 && push.getDuration()!=0) {
							Event event =  null;
							switch(push.getType()) {
							case BAN:
								event=new BanlistUnbanEvent(push);
								break;
							case JAIL:
								event=new BanlistUnjailEvent(push);
								//teleport to player's "spawn"
								if(push.isIP()) {
									for(String nick : Accounts.findPlayersOnIP(push.getUser())) {
										Player p = TheAPI.getPlayerOrNull(nick);
										if(p!=null) {
											if (setting.deathspawnbol) {
												if (setting.deathspawn == DeathTp.HOME)
													API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
												else if (setting.deathspawn == DeathTp.BED)
													API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
												else if (setting.deathspawn == DeathTp.SPAWN) {
													API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
													Loader.sendMessages(p, "Spawn.Teleport.You");
												}
											}else
												API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
										}else {
											List<String> home = SPunishmentAPI.data.getStringList("tp-home");
											home.add(nick);
											SPunishmentAPI.data.set("tp-home", home);
											SPunishmentAPI.data.save();
										}
									}
									break;
								}
								Player p = TheAPI.getPlayerOrNull(push.getUser());
								if(p!=null) {
									if (setting.deathspawnbol) {
										if (setting.deathspawn == DeathTp.HOME)
											API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
										else if (setting.deathspawn == DeathTp.BED)
											API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
										else if (setting.deathspawn == DeathTp.SPAWN) {
											API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
											Loader.sendMessages(p, "Spawn.Teleport.You");
										}
									}else
										API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
								}else {
									List<String> home = SPunishmentAPI.data.getStringList("tp-home");
									SPunishmentAPI.data.set("tp-home", home);
									home.add(push.getUser());
									SPunishmentAPI.data.save();
								}
								break;
							case MUTE:
								event=new BanlistUnmuteEvent(push);
								break;
								default:
									break;
							}
							if(event!=null)TheAPI.callEvent(event);
							push.remove();
						}
					}
				}
				for(String ip : me.devtec.servercontrolreloaded.utils.punishment.SPunishmentAPI.data.getKeys("i")) {
					for(Punishment push : TheAPI.getPunishmentAPI().getPunishmentsIP(ip)) {
						if(push.getExpire()<=0 && push.getDuration()!=0) {
							Event event =  null;
							switch(push.getType()) {
							case BAN:
								event=new BanlistUnbanEvent(push);
								break;
							case JAIL:
								event=new BanlistUnjailEvent(push);
								//teleport to player's "spawn"
								if(push.isIP()) {
									for(String nick : Accounts.findPlayersOnIP(push.getUser())) {
										Player p = TheAPI.getPlayerOrNull(nick);
										if(p!=null) {
											if (setting.deathspawnbol) {
												if (setting.deathspawn == DeathTp.HOME)
													API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
												else if (setting.deathspawn == DeathTp.BED)
													API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
												else if (setting.deathspawn == DeathTp.SPAWN) {
													API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
													Loader.sendMessages(p, "Spawn.Teleport.You");
												}
											}else
												API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
										}else {
											List<String> home = SPunishmentAPI.data.getStringList("tp-home");
											home.add(nick);
											SPunishmentAPI.data.set("tp-home", home);
											SPunishmentAPI.data.save();
										}
									}
									break;
								}
								Player p = TheAPI.getPlayerOrNull(push.getUser());
								if(p!=null) {
									if (setting.deathspawnbol) {
										if (setting.deathspawn == DeathTp.HOME)
											API.teleport(p, API.getTeleportLocation(p, TeleportLocation.HOME));
										else if (setting.deathspawn == DeathTp.BED)
											API.teleport(p, API.getTeleportLocation(p, TeleportLocation.BED));
										else if (setting.deathspawn == DeathTp.SPAWN) {
											API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
											Loader.sendMessages(p, "Spawn.Teleport.You");
										}
									}else
										API.teleport(p, API.getTeleportLocation(p, TeleportLocation.SPAWN));
								}else {
									List<String> home = SPunishmentAPI.data.getStringList("tp-home");
									SPunishmentAPI.data.set("tp-home", home);
									home.add(push.getUser());
									SPunishmentAPI.data.save();
								}
								break;
							case MUTE:
								event=new BanlistUnmuteEvent(push);
								break;
								default:
									break;
							}
							if(event!=null)TheAPI.callEvent(event);
							push.remove();
						}
					}
				}
			}
		}.runRepeating(0, 40)); //every 2s update
	}

	private static void bungeecordPlayers() {
		tasks.add(new Tasker() {
			public void run() {
				BungeeListener.requestOnlinePlayers();
			}
		}.runRepeating(0, 100));
	}

	public static void reload() {
		unload();
		load();
	}

	private static void tempfly() {
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for(Player s : TheAPI.getOnlinePlayers()) {
					if(!TheAPI.getUser(s).getBoolean("TempFly.Use"))continue;
						long start = TheAPI.getUser(s).getLong("TempFly.Start");
						int end = TheAPI.getUser(s).getInt("TempFly.Time");
						long timeout = start / 1000 - System.currentTimeMillis() / 1000 + end;
						if (timeout <= 0) {
							if (s != null) {
								Loader.sendMessages(s, "Fly.Temp.End");
								SPlayer p = API.getSPlayer(s);
								p.disableFly();
								p.getUser().remove("TempFly");
								p.getUser().save();
							}
							return;
						}
						if (s != null)
						if (timeout <= 5 || timeout == 10 || timeout == 15 || timeout == 30 || timeout == 45 || timeout == 60)
								Loader.sendMessages(s, "Fly.Temp.EndIn", Placeholder.c().add("%time%", StringUtils.setTimeToString(timeout)));
					}
				}
		}.runRepeating(0, 20));
	}

	private static void tempGamemode(){
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for(Player s:TheAPI.getOnlinePlayers()) {
					if (!TheAPI.getUser(s).getBoolean("TempGamemode.Use")) continue;
					long start = TheAPI.getUser(s).getLong("TempGamemode.Start");
					int end = TheAPI.getUser(s).getInt("TempGamemode.Time");
					long timeout = start / 1000 - System.currentTimeMillis() / 1000 + end;
					if (timeout <= 0) {
						if (s != null) {
							Loader.sendMessages(s, "GameMode.Temp.End");
							SPlayer p = API.getSPlayer(s);
							if (!s.hasPermission("SCR.Other.GamemodeChangePrevent")) {
								if (Loader.mw.exists("WorldsSettings." + s.getWorld().getName() + ".GameMode"))
									try {
									s.setGameMode(GameMode.valueOf(Loader.mw.getString("WorldsSettings." + s.getWorld().getName() + ".GameMode").toUpperCase()));
									}catch(Exception | NoSuchFieldError err) {}
								}else
									s.setGameMode(GameMode.valueOf(p.getUser().getString("TempGamemode.Prev")));
							p.getUser().remove("TempGamemode");
							p.getUser().save();
						}
						return;
					}
					if (s != null)
						if (timeout <= 3 || timeout == 10 || timeout == 15 || timeout == 30 || timeout == 45 || timeout == 60)
							Loader.sendMessages(s, "GameMode.Temp.EndIn", Placeholder.c().add("%time%", StringUtils.setTimeToString(timeout)));
				}
			}
		}.runRepeatingSync(0,20));
	}

	private static void savetask() {
		if (Loader.mw.getInt("SavingTask.Delay") < 600) {
			Loader.mw.set("SavingTask.Delay", 600);
			Loader.mw.save();
		}
		tasks.add(new Tasker() {
			int now = 0;

			@Override
			public void run() {
				List<World> w = Bukkit.getWorlds();
				if (w.size() <= now)
					now = 0;
				try {
					if (!Loader.mw.getBoolean("WorldsSettings." + w.get(now).getName() + ".AutoSave"))
						w.get(now).save();
				} catch (Exception err) {
				}
				++now;
			}
		}.runRepeatingSync(0, 20 * Loader.mw.getLong("SavingTask.Delay")));
	}

	public static void regPlayer(Player p) {
		if (!sss.containsKey(p.getName())) {
			String uuid = p.getUniqueId().toString();
			uuid = uuid.substring(0, 5);
			String pname = p.getName();
			if (pname.length() > 5)
				pname = pname.substring(0, 5);
			sss.put(p.getName(), uuid + pname);
		}
	}

	private static void other() {
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					if(Loader.config.getBoolean("ChatFormat.enabled")) {
						ChatFormatter.setupName(p);
					}
			}
		}.runRepeating(0, 20));
	}

	private static void tab() {
		int r = Loader.tab.getInt("Options.RefleshTick.NameTag");
		if (r <= 0)
			r = 1;
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					TabList.setFooterHeader(p);
				TabList.update();
			}
		}.runRepeating(0, Loader.tab.getInt("Options.RefleshTick.Tablist")));
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers()) {
					TabList.setTabName(p);
					TabList.setNameTag(p);
				}
				TabList.update();
			}
		}.runRepeating(0, r));
	}

	private static void vipslot() {
		if (setting.vip_add)
			TheAPI.setMaxPlayers(Bukkit.getMaxPlayers() + Loader.config.getInt("Options.VIPSlots.SlotsToAdd"));
		if (setting.vip_kick)
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player online : TheAPI.getOnlinePlayers()) {
					if (!players.contains(online.getName())) {
						if (!online.hasPermission("SCR.Other.JoinFullServer"))
							players.add(online.getName());
					} else if (online.hasPermission("SCR.Other.JoinFullServer")) {
						players.remove(online.getName());
					}
				}
			}
		}.runRepeating(0, 200));
	}

	protected static final AnimationManager aa = new AnimationManager();
	
	private static void automessage() {
		tasks.add(new Tasker() {
			public void run() {
				if (TheAPI.getOnlinePlayers().size() < Loader.config.getInt("Options.AutoMessage.MinimalPlayers"))
					return;
				List<String> l = Loader.config.getStringList("Options.AutoMessage.Messages");
				if (setting.am_random) {
					for(Player p : TheAPI.getOnlinePlayers()) {
						if(Loader.config.getBoolean("Options.AutoMessage.UseJson")) {
							String json = StringUtils.colorizeJson(aa.replaceWithoutColors(p,TheAPI.getRandomFromList(l)));
							Ref.sendPacket(p, NMSAPI.getPacketPlayOutChat(ChatType.SYSTEM, NMSAPI.getIChatBaseComponentFromCraftBukkit(json)));
						}else {
							TheAPI.msg(aa.replace(p,TheAPI.getRandomFromList(l)), p);
						}
					}
				} else {
					if (l.size() <= tests)
						tests = 0;
					for(Player p : TheAPI.getOnlinePlayers()) {
						if(Loader.config.getBoolean("Options.AutoMessage.UseJson")) {
							Ref.sendPacket(p, NMSAPI.getPacketPlayOutChat(ChatType.SYSTEM, NMSAPI.getIChatBaseComponentFromCraftBukkit(StringUtils.colorizeJson(aa.replaceWithoutColors(p,l.get(tests))))));
						}else {
							TheAPI.msg(aa.replace(p,l.get(tests)), p);
						}
					}
					++tests;
				}
				aa.update();
			}

		}.runRepeating(0, 20* StringUtils.getTimeFromString(Loader.config.getString("Options.AutoMessage.Interval"))));
	}
	
	public static void playTime() {
		
		/*
		 * PlayTop task
		 */
		tasks.add(new Tasker() {
			public void run() {
				PlayTimeUtils.playtop.clear();
				for (UUID sa : TheAPI.getUsers()) {
					String n = LoaderClass.cache.lookupNameById(sa);
					if(n!=null) {
						if(PlayTimeUtils.playtop.containsKey(n))continue;
						int time = PlayTimeUtils.playtime(n);
						if(time>0)
							PlayTimeUtils.playtop.put(n, time);
					}
				}
				PlayTimeUtils.ranks.load(PlayTimeUtils.playtop);
			}
		}.runRepeating(1, 300*20));
		PlayTimeUtils.task=true;
		
		/*
		 * Custom PlayTime task
		 */
		if(Loader.config.getBoolean("Options.PlayTime.UseCustomPlayTime")) {
			if(Loader.config.getBoolean("Options.PlayTime.UseAfkTime")) {
				tasks.add(new Tasker() {
					public void run() {
						for(Player p : TheAPI.getOnlinePlayers()) {
							SPlayer player = API.getSPlayer(p);
							player.addPlayTime(1);
						}
						
					}
				}.runRepeating(20, 20));
			}else {
				tasks.add(new Tasker() {
					public void run() {
						for(Player p : TheAPI.getOnlinePlayers()) {
							if(!API.isAFK(p)) {
								SPlayer player = API.getSPlayer(p);
								player.addPlayTime(1);
							}
						}
						
					}
				}.runRepeating(20, 20));
			}
		}
	}
}
