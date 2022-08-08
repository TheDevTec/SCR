package me.devtec.scr.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class AutoAnnoucments {

	private static int task = -1;
	private static List<String> list = new ArrayList<String>();
	public static int line;
	
	public static void loadtask() {
		if(task!=-1)
			unloadTask();
		
		long time = StringUtils.timeFromString(Loader.config.getString("autoAnnoucments.interval"));
		list = Loader.config.getStringList("autoAnnoucments.messages");
		line=0;
		
		task = new Tasker() {
			
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size()>=Loader.config.getInt("autoAnnoucments.minimalPlayers")) {
					if(Loader.config.getBoolean("autoAnnoucments.random"))
						line = new Random().nextInt(list.size());
					else
						line = (list.size()>=(line+1) ? (line+1) : 0);
					MessageUtils.sendAnnoucment(list.get(line), BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
				}
				
			}
		}.runRepeating(0, time*20);
	}
	
	public static void unloadTask() {
		if(task != -1)
			Scheduler.cancelTask(task);
	}
}
