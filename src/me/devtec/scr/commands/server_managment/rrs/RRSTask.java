package me.devtec.scr.commands.server_managment.rrs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.shared.Ref;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class RRSTask { // Reload Restart Stop task

	public static int time = -1;
	public static int task = -1;
	public static TaskType currentTask;

	public static List<Integer> broadcastTimes;

	public enum TaskType {
		RELOAD, RESTART, STOP;
	}

	public static void startTask(TaskType type, int time) {
		if (task != -1) // cancel running task
			cancelTask(false);

		// time=seconds
		currentTask = type;
		RRSTask.time = time;

		broadcastTimes = Loader.config.getIntegerList("rrs." + type.name().toLowerCase() + ".times");

		List<CommandSender> list = new ArrayList<>(API.getOnlinePlayers(true));
		// list.add(Bukkit.getConsoleSender()); // unused

		MessageUtils.msgConfig(Bukkit.getConsoleSender(), "rrs." + currentTask.name().toLowerCase() + ".start", Loader.config, Placeholders.c().add("time", StringUtils.timeToString(RRSTask.time)),
				list.toArray(new CommandSender[0]));

		task = new Tasker() {
			@Override
			public void run() {
				if (broadcastTimes.contains(RRSTask.time--))
					MessageUtils.msgConfig(Bukkit.getConsoleSender(), "rrs." + currentTask.name().toLowerCase() + ".running", Loader.config,
							Placeholders.c().add("time", StringUtils.timeToString(RRSTask.time + 1)), list.toArray(new CommandSender[0]));
			}
		}.runRepeatingTimes(0, 20, time, () -> {

			MessageUtils.msgConfig(Bukkit.getConsoleSender(), "rrs." + currentTask.name().toLowerCase() + ".process", Loader.config, null, list.toArray(new CommandSender[0]));

			BukkitLoader.getNmsProvider().postToMainThread(() -> {
				switch (currentTask) {
				case RELOAD: {
					for (String cmd : Loader.config.getStringList("rrs." + currentTask.name().toLowerCase() + ".commands"))
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
					int waitTime = Loader.config.getInt("rrs." + currentTask.name().toLowerCase() + ".waitTime");
					if (waitTime <= 0)
						Bukkit.reload();
					else
						new Tasker() {
							@Override
							public void run() {
								BukkitLoader.getNmsProvider().postToMainThread(Bukkit::reload);
							}
						}.runLater(waitTime * 20);
				}
					break;
				case RESTART:
					for (String cmd : Loader.config.getStringList("rrs." + currentTask.name().toLowerCase() + ".commands"))
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
					for (Player p : BukkitLoader.getOnlinePlayers())
						p.kickPlayer(StringUtils.colorize(Loader.config.getString("rrs." + currentTask.name().toLowerCase() + ".kick")));
					if (Ref.getClass("net.md_5.bungee.api.ChatColor") != null) // if spigot
						try {
							int waitTime = Loader.config.getInt("rrs." + currentTask.name().toLowerCase() + ".waitTime");
							if (waitTime <= 0)
								Bukkit.spigot().restart();
							else
								new Tasker() {
									@Override
									public void run() {
										BukkitLoader.getNmsProvider().postToMainThread(() -> Bukkit.spigot().restart());
									}
								}.runLater(waitTime * 20);
						} catch (Exception | NoSuchMethodError e) {
							int waitTime = Loader.config.getInt("rrs." + currentTask.name().toLowerCase() + ".waitTime");
							if (waitTime <= 0)
								Bukkit.shutdown();
							else
								new Tasker() {
									@Override
									public void run() {
										BukkitLoader.getNmsProvider().postToMainThread(Bukkit::shutdown);
									}
								}.runLater(waitTime * 20);
						}
					else {
						int waitTime = Loader.config.getInt("rrs." + currentTask.name().toLowerCase() + ".waitTime");
						if (waitTime <= 0)
							Bukkit.shutdown();
						else
							new Tasker() {
								@Override
								public void run() {
									BukkitLoader.getNmsProvider().postToMainThread(Bukkit::shutdown);
								}
							}.runLater(waitTime * 20);
					}
					break;
				case STOP:
					for (String cmd : Loader.config.getStringList("rrs." + currentTask.name().toLowerCase() + ".commands"))
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
					for (Player p : BukkitLoader.getOnlinePlayers())
						p.kickPlayer(StringUtils.colorize(Loader.config.getString("rrs." + currentTask.name().toLowerCase() + ".kick")));
					int waitTime = Loader.config.getInt("rrs." + currentTask.name().toLowerCase() + ".waitTime");
					if (waitTime <= 0)
						Bukkit.shutdown();
					else
						new Tasker() {
							@Override
							public void run() {
								BukkitLoader.getNmsProvider().postToMainThread(Bukkit::shutdown);
							}
						}.runLater(waitTime * 20);
					break;
				}
			});
		});
	}

	public static void cancelTask(boolean message) {
		if (task != -1)
			Scheduler.cancelTask(task);
		task = -1;
		time = -1;
		if (currentTask != null && message) {
			List<CommandSender> list = new ArrayList<>(BukkitLoader.getOnlinePlayers());
			list.add(Bukkit.getConsoleSender());
			MessageUtils.msgConfig(null, "rrs." + currentTask.name().toLowerCase() + ".cancel", Loader.config, null, list.toArray(new CommandSender[0]));
		}
	}

}
