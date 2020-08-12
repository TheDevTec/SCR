package Utils;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import me.DevTec.TheAPI;
import me.DevTec.Scheduler.Tasker;

public class AFK {

	private static String afkMsg = Loader.s("Prefix") + Loader.s("AFK.IsAFK");
	private static long time = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToAFK")),
			rkick = TheAPI.getStringUtils().getTimeFromString(Loader.config.getString("Options.AFK.TimeToKick"));

	public static void start() {
		new Tasker() {
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
								TheAPI.broadcastMessage(
										afkMsg.replace("%player%", s.getName()).replace("%playername%", s.getDisplayName()));
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
		}.repeatingAsync(20, 20);
	}

	public static void setAFK(SPlayer s) {
		save(s);
		s.mp = true;
		s.manual = true;
			if (!s.hasVanish())
				TheAPI.broadcastMessage(afkMsg.replace("%player%", s.getName()).replace("%playername%", s.getDisplayName()));
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
}
