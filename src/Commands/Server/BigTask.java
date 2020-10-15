package Commands.Server;

import org.bukkit.Bukkit;

import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.Reflections.Reflections;

public class BigTask {
	public static int r = -1;

	public static enum TaskType {
		STOP, RESTART, RELOAD
	}

	private static TaskType s;

	public static boolean start(TaskType t, long h) {
		s = t;
		if ((t == TaskType.STOP ? setting.warn_stop
				: (t == TaskType.RELOAD ? setting.warn_reload : setting.warn_restart))) {
			if (r == -1) {
				r = new Tasker() {
					long f = h;

					@Override
					public void run() {
						if (f <= 0) {
							end();
							return;
						} else if (f == h % 75 && h > 15 || f == h % 50 && h > 10 || f == h % 25 && h > 5 || f == 5
								|| f == 4 || f == 3 || f == 2 || f == 1) {
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
			}
			return false;
		} else {
			end();
			return true;
		}
	}

	public static void cancel() {
		if (r != -1) {
			Scheduler.cancelTask(r);
			r = -1;
			TheAPI.broadcastMessage("&eCancelled "
					+ (s == TaskType.STOP ? "stopping" : (s == TaskType.RELOAD ? "reloading" : "restarting"))
					+ " of server!");
		}
	}

	public static void end() {
		if (r != -1) {
			Scheduler.cancelTask(r);
			r = -1;
			TheAPI.broadcastMessage(
					"&c" + (s == TaskType.STOP ? "Stopping" : (s == TaskType.RELOAD ? "Reloading" : "Restarting"))
							+ " of server..");
			switch (s) {
			case RELOAD:
				Bukkit.reload();
				break;
			case RESTART:
				if (Reflections.existsClass("net.md_5.bungee.api.ChatColor"))
					Bukkit.spigot().restart();
				else
					Bukkit.shutdown();
				break;
			case STOP:
				Bukkit.shutdown();
				break;
			}
	}}
}
