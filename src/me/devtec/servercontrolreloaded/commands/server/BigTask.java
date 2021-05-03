package me.devtec.servercontrolreloaded.commands.server;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class BigTask {
	public static int r = -1;

	public static enum TaskType {
		STOP, RESTART, RELOAD
	}

	private static TaskType s;

	public static boolean start(TaskType t, long h) {
		if (r != -1)cancel(false);
		s = t;
		if ((t == TaskType.STOP ? setting.warn_stop
				: (t == TaskType.RELOAD ? setting.warn_reload : setting.warn_restart))) {
			if(h > 15 && h!=30 && h!=45 && h!=60)
			for (String s : Loader.config.getStringList("Options.WarningSystem."
					+ (t == TaskType.STOP ? "Stop" : (t == TaskType.RELOAD ? "Reload" : "Restart"))
					+ ".Messages"))
				TheAPI.broadcastMessage(
						s.replace("%time%", "" + StringUtils.setTimeToString(h)));
				r = new Tasker() {
				long f = h;
				public void run() {
					if (f <= 0) {
						NMSAPI.postToMainThread(() ->end());
						return;
					} else if (f == 60 || f == 45 || f == 30 || f == 15 || f <= 5) {
						for (String s : Loader.config.getStringList("Options.WarningSystem."
								+ (t == TaskType.STOP ? "Stop" : (t == TaskType.RELOAD ? "Reload" : "Restart"))
								+ ".Messages"))
							TheAPI.broadcastMessage(
									s.replace("%time%", "" + StringUtils.setTimeToString(f)));
					}
					--f;
				}
			}.runRepeating(0, 20);
			return true;
		} else {
			end();
			return true;
		}
	}

	public static void cancel(boolean msg) {
		if (r != -1) {
			Scheduler.cancelTask(r);
			r = -1;
			if(msg)
				for (String s : Loader.config.getStringList("Options.WarningSystem."
						+ (s == TaskType.STOP ? "Stop.Cancelled" : (s == TaskType.RELOAD ? "Reload.Cancelled" : "Restart.Cancelled"))))
					TheAPI.broadcastMessage(s);
		}
	}

	public static void end() {
		if (r != -1) {
			Scheduler.cancelTask(r);
			r = -1;
			for (String s : Loader.config.getStringList("Options.WarningSystem."
					+ (s == TaskType.STOP ? "Stop.Process" : (s == TaskType.RELOAD ? "Reload.Process" : "Restart.Process"))))
				TheAPI.broadcastMessage(s);
			for (String s : Loader.config.getStringList("Options.WarningSystem."
					+ (s == TaskType.STOP ? "Stop.Commands" : (s == TaskType.RELOAD ? "Reload.Commands" : "Restart.Commands"))))
				TheAPI.sudoConsole(s);
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000+TheAPI.getOnlinePlayers().size()*50);
					} catch (Exception e1) {
					}
					NMSAPI.postToMainThread(new Runnable() {
						public void run() {
							switch (s) {
							case RELOAD:
								Bukkit.reload();
								break;
							case RESTART:
								for(Player s : TheAPI.getOnlinePlayers())
									s.kickPlayer(TabList.replace(Loader.config.getString("Options.WarningSystem.Restart.Kick"),s,true));
								if (Ref.getClass("net.md_5.bungee.api.ChatColor")!=null)
									try {
									Bukkit.spigot().restart();
									}catch(Exception | NoSuchMethodError e) {
										Bukkit.shutdown();
									}
								else
									Bukkit.shutdown();
								break;
							case STOP:
								for(Player s : TheAPI.getOnlinePlayers())
									s.kickPlayer(TabList.replace(Loader.config.getString("Options.WarningSystem.Stop.Kick"),s,true));
								Bukkit.shutdown();
								break;
							}
						}
					});
				}
			}).start();
		}
	}
}
