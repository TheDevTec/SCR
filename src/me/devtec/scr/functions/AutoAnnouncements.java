package me.devtec.scr.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class AutoAnnouncements {

	private static int task = -1;
	private static List<String> list = new ArrayList<>();
	public static int line;

	public static void loadtask() {
		if (task != -1)
			unloadTask();

		if (!Loader.config.getBoolean("autoAnnouncements.enabled"))
			return;

		long time = StringUtils.timeFromString(Loader.config.getString("autoAnnouncements.interval"));
		list = Loader.config.getStringList("autoAnnouncements.messages");
		line = 0;

		task = new Tasker() {

			@Override
			public void run() {
				if (BukkitLoader.getOnlinePlayers().size() >= Loader.config.getInt("autoAnnouncements.minimalPlayers")) {
					if (Loader.config.getBoolean("autoAnnouncements.random"))
						line = new Random().nextInt(list.size());

					MessageUtils.sendAnnoucment(list.get(line), BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
					line = list.size() - 1 >= line + 1 ? line + 1 : 0;
				}

			}
		}.runRepeating(0, time * 20);
	}

	public static void unloadTask() {
		if (task != -1)
			Scheduler.cancelTask(task);
		task = -1;
	}
}
