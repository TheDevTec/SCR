package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Tasks {
	
	public static List<String> players = new ArrayList<String>();
	static List<Integer> tasks = new ArrayList<Integer>();
	static HashMap<String, String> ss = new HashMap<String, String>();
	static Loader a;
	static int tests;

	public static void unload() {
		for (Integer t : tasks)
			Scheduler.cancelTask(t);
		tests = 0;
		tasks.clear();
	}

	public static void load() {
		a = Loader.getInstance;
		ss.clear();
		players.clear();
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
								TheAPI.getUser(s).setAndSave("TempFly", null);
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
		}
		tasks.add(new Tasker() {
			int now = 0;

			@Override
			public void run() {
				List<World> w = Bukkit.getWorlds();
				if (w.size() - 1 == now)
					now = 0;
				try {
					if (!Loader.mw.getBoolean("WorldsSettings." + w.get(now).getName() + ".AutoSave"))
						w.get(now).save();
				} catch (Exception err) {
				}
				++now;
			}
		}.runRepeating(0, 20 * Loader.mw.getInt("SavingTask.Delay")));
	}

	public static void regPlayer(Player p) {
		if (!ss.containsKey(p.getName())) {
			String uuid = p.getUniqueId().toString();
			uuid = uuid.substring(0, 5);
			String pname = p.getName();
			if (pname.length() > 5) {
				pname = pname.substring(0, 5);
			}
			ss.put(p.getName(), uuid + pname);
		}
	}

	private static void other() {
		Config f = Loader.config;
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					Loader.setupChatFormat(p);
				if (setting.motd) {
					if (!setting.lock_server || setting.lock_server && !setting.motd_maintenance)
						TheAPI.setMotd(f.getString("Options.ServerList.MOTD.Text.Normal").replace("%next%", "\n")
								.replace("%line%", "\n"));
					else
						TheAPI.setMotd(f.getString("Options.ServerList.MOTD.Text.Maintenance")
								.replace("%next%", "\n").replace("%line%", "\n"));
				}
			}
		}.runRepeating(0, 100));
	}

	private static void tab() {
		if(setting.tab_sort)
		for (Player p : TheAPI.getOnlinePlayers()) {
			if (!ss.containsKey(p.getName()))
				regPlayer(p);
		}
		int r = Loader.tab.getInt("Options.RefleshTick.NameTag");
		if (r <= 0)
			r = 1;
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					TabList.setFooterHeader(p);
			}
		}.runRepeating(0, Loader.tab.getInt("Options.RefleshTick.Tablist")));
		tasks.add(new Tasker() {
			@Override
			public void run() {
				for (Player p : TheAPI.getOnlinePlayers())
					TabList.setName(p);
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
					TheAPI.broadcastMessage(TabList.replace(TheAPI.getRandomFromList(l).toString(), null));
				} else {
					if (l.size() <= tests) {
						tests = 0;
					}
					TheAPI.broadcastMessage(TabList.replace(l.get(tests).toString(), null));
					tests = tests + 1;
				}
			}

		}.runRepeating(0, 20* StringUtils.getTimeFromString(Loader.config.getString("Options.AutoMessage.Interval"))));
	}
}
