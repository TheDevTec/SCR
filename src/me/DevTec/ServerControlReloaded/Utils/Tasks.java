package me.DevTec.ServerControlReloaded.Utils;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.Collections.UnsortedSet;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.UnsortedMap;
import me.DevTec.TheAPI.Utils.Listener.EventHandler;
import me.DevTec.TheAPI.Utils.Listener.Listener;
import me.DevTec.TheAPI.Utils.Listener.Events.ServerListPingEvent;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI.ChatType;
import me.DevTec.TheAPI.Utils.Reflections.Ref;

public class Tasks {
	
	public static Set<String> players = new UnsortedSet<>();
	static Set<Integer> tasks = new UnsortedSet<>();
	static Map<String, String> sss = new UnsortedMap<>();
	static Loader a;
	static int tests;
	static Listener l = new Listener() {
		@EventHandler
		public void onTag(ServerListPingEvent e) {
			if (setting.motd) {
				e.setMotd(TheAPI.colorize(PlaceholderAPI.setPlaceholders(null,Loader.config.getString((!setting.lock_server || setting.lock_server && !setting.motd_maintenance)?"Options.ServerList.MOTD.Text.Normal":"Options.ServerList.MOTD.Text.Maintenance")
						.replace("%next%", "\n").replace("%line%", "\n"))));
			}
		}
	};

	public static void unload() {
		for (Integer t : tasks)
			Scheduler.cancelTask(t);
		tests = 0;
		l.unregister();
		tasks.clear();
	}

	public static void load() {
		a = Loader.getInstance;
		sss.clear();
		players.clear();
		if(setting.motd)l.register();
		if (setting.am)
			automessage();
		if (setting.vip)
			vipslot();

		if (setting.tab)
			tab();

		if (setting.save)
			savetask();

		if (setting.sb)
			scoreboard();

		other();
		tempfly();
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
								TheAPI.sendActionBar(s, "&cTempFly ended");
								API.getSPlayer(s).disableFly();
								TheAPI.getUser(s).remove("TempFly");
								TheAPI.getUser(s).save();
							}
						}
						if (timeout == 5 || timeout == 4 || timeout == 3 || timeout == 2 || timeout == 1
								|| timeout == 15 || timeout == 10 || timeout == 30) {
							if (s != null)
								TheAPI.sendActionBar(s,"&6TempFly ends in &c" + StringUtils.setTimeToString(timeout));
						}
					}}
		}.runRepeating(0, 20));
	}

	private static void scoreboard() {
		int r = Loader.sb.getInt("Options.RefleshTick");
		if (r <= 0)
			r = 1;
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					ScoreboardStats.createScoreboard(p);

			}
		}.runRepeating(0, r));
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
		}.runRepeatingSync(0, 20 * Loader.mw.getInt("SavingTask.Delay")));
	}

	public static void regPlayer(Player p) {
		if (!sss.containsKey(p.getName())) {
			String uuid = p.getUniqueId().toString();
			uuid = uuid.substring(0, 5);
			String pname = p.getName();
			if (pname.length() > 5) {
				pname = pname.substring(0, 5);
			}
			sss.put(p.getName(), uuid + pname);
		}
	}

	private static void other() {
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					Loader.setupChatFormat(p);
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
				for (Player p : TheAPI.getOnlinePlayers()) {
					TabList.setFooterHeader(p);
				}
			}
		}.runRepeating(0, Loader.tab.getInt("Options.RefleshTick.Tablist")));
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers()) {
					TabList.setName(p);
				}
			}
		}.runRepeating(0, r));
	}

	private static void vipslot() {
		if (setting.vip_add)
			TheAPI.setMaxPlayers(Bukkit.getMaxPlayers() + Loader.config.getInt("Options.VIPSlots.SlotsToAdd"));
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player online : TheAPI.getOnlinePlayers()) {
					if (!players.contains(online.getName())) {
						if (!online.hasPermission("ServerControl.JoinFullServer"))
							players.add(online.getName());
					} else if (online.hasPermission("ServerControl.JoinFullServer")) {
						players.remove(online.getName());
					}
				}
			}
		}.runRepeating(0, 200));
	}
	
	private static void automessage() {
		tasks.add(new Tasker() {
			@Override
			public void run() {
				if (TheAPI.getOnlinePlayers().size() < Loader.config.getInt("Options.AutoMessage.MinimalPlayers"))
					return;
				List<String> l = Loader.config.getStringList("Options.AutoMessage.Messages");
				if (setting.am_random) {
					for(Player p : TheAPI.getOnlinePlayers()) {
						if(Loader.config.getBoolean("Options.AutoMessage.UseJson")) {
							String json = StringUtils.colorizeJson(TabList.replace(TheAPI.getRandomFromList(l), p, false));
							Ref.sendPacket(p, NMSAPI.getPacketPlayOutChat(ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(json)));
						}else {
							TheAPI.msg(TabList.replace(TheAPI.getRandomFromList(l), p, true), p);
						}
					}
				} else {
					if (l.size() <= tests)
						tests = 0;
					for(Player p : TheAPI.getOnlinePlayers()) {
						if(Loader.config.getBoolean("Options.AutoMessage.UseJson")) {
							Ref.sendPacket(p, NMSAPI.getPacketPlayOutChat(ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(StringUtils.colorizeJson(TabList.replace(l.get(tests), p, false)))));
						}else {
							TheAPI.msg(TabList.replace(l.get(tests), p, true), p);
						}
					}
					++tests;
				}
			}

		}.runRepeating(0, 20* StringUtils.getTimeFromString(Loader.config.getString("Options.AutoMessage.Interval"))));
	}
}
