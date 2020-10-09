package Utils;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Scheduler;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.StringUtils;

public class AFK {
	private static int task;
	private static long time, rkick;

	public static void start() {
		time = StringUtils.getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK"));
		rkick = StringUtils.getTimeFromString(Loader.config.getString("Options.AFK.TimeToKick"));
		task=new Tasker() {
			@Override
			public void run() {
				for(SPlayer s : API.getSPlayers()) {
				boolean is = getTime(s) <= 0;
				if (setting.afk_auto) {
					if (is) {
						if (!s.bc && !s.mp) {
							s.bc = true;
							s.mp = true;
							if (!s.hasVanish())
								Loader.sendBroadcasts(s.getPlayer(), "AFK.Start");
						}
						if (setting.afk_kick && is) {
							if (s.kick >= rkick) {
								if (!s.hasPermission("servercontrol.afk.bypass"))
									if(s.getPlayer()!=null && s.getPlayer().isOnline())
									s.getPlayer().kickPlayer(TheAPI.colorize(Loader.config.getString("Options.AFK.KickMessage")));
								cancel();
								return;
							} else
								++s.kick;
						}
					}
					++s.afk;
				}
				}
			}
		}.runRepeating(0, 20);
	}

	public static void setAFK(SPlayer s) {
		save(s);
		s.mp = true;
		s.manual = true;
			if (!s.hasVanish())
				Loader.sendBroadcasts(s.getPlayer(), "AFK.Start");
	}

	public static long getTime(SPlayer s) {
		return time - s.afk;
	}

	public static void save(SPlayer s) {
		s.afk = 0;
		s.kick = 0;
		s.manual = false;
		s.mp = false;
		s.bc = false;
	}

	public static boolean isManualAfk(SPlayer s) {
		return s.manual;
	}

	public static boolean isAfk(SPlayer s) {
		return getTime(s) <= 0;
	}

	public static void stop() {
		Scheduler.cancelTask(task);
	}
}
