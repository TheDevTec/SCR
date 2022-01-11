package me.devtec.scr.commands.other.control;

import java.util.List;

import org.bukkit.Bukkit;

import me.devtec.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;

public class ControlPanel {
	public enum StopType {
		STOP, RESTART, RELOAD
	}
	
	public static StopType process;
	private static int task;
	
	public static void start(StopType type, int time) {
		if(process!=null)return;
		process=type;
		for(String cmd : Loader.config.getStringList("control-panel."+type.name().toLowerCase()+".start.commands"))
			TheAPI.sudoConsole(cmd);
		for(String msg : Loader.config.getStringList("control-panel."+type.name().toLowerCase()+".start.broadcast"))
			TheAPI.bcMsg(msg);
		int wait = (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(null, Loader.config.getString("control-panel."+type.name().toLowerCase()+".start.wait")));
		task=new Tasker() {
			int schedule = time;
			public void run() {
				if(--schedule <= 0) {
					TheAPI.getNmsProvider().postToMainThread(() -> process(type, (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(null, Loader.config.getString("control-panel."+type.name().toLowerCase()+".end.wait")))));
					cancel();
					return;
				}
				List<String> cmds = Loader.config.getStringList("control-panel."+type.name().toLowerCase()+".process."+schedule+".commands");
				if(!cmds.isEmpty())TheAPI.getNmsProvider().postToMainThread(() -> {
					for(String cmd : cmds)
						TheAPI.sudoConsole(cmd);
				});
				for(String msg : Loader.config.getStringList("control-panel."+type.name().toLowerCase()+".process."+schedule+".broadcast"))
					TheAPI.bcMsg(msg);
			}
		}.runRepeating(wait<0?0:wait*20, 20);
	}
	
	public static void cancel() {
		Scheduler.cancelTask(task);
		for(String cmd : Loader.config.getStringList("control-panel."+process.name().toLowerCase()+".cancel.commands"))
			TheAPI.sudoConsole(cmd);
		for(String msg : Loader.config.getStringList("control-panel."+process.name().toLowerCase()+".cancel.broadcast"))
			TheAPI.bcMsg(msg);
		process=null;
	}
	
	public static void process(StopType type, int waitTime) {
		for(String cmd : Loader.config.getStringList("control-panel."+type.name().toLowerCase()+".end.commands"))
			TheAPI.sudoConsole(cmd);
		for(String msg : Loader.config.getStringList("control-panel."+type.name().toLowerCase()+".end.broadcast"))
			TheAPI.bcMsg(msg);
		if(waitTime>0)
			new Tasker() {
				public void run() {
					switch(type) {
					case RELOAD:
						Bukkit.reload();
						break;
					case RESTART:
						try {
							Bukkit.spigot().restart();
						}catch(Exception | NoClassDefFoundError err) {
							Bukkit.shutdown();
						}
						break;
					case STOP:
						Bukkit.shutdown();
						break;
					}
				}
			}.runLaterSync(waitTime*20);
			else {
				switch(type) {
				case RELOAD:
					Bukkit.reload();
					break;
				case RESTART:
					try {
						Bukkit.spigot().restart();
					}catch(Exception | NoClassDefFoundError err) {
						Bukkit.shutdown();
					}
					break;
				case STOP:
					Bukkit.shutdown();
					break;
				}
			}
	}

	public static void skipTime() {
		if(process==null)return;
		Scheduler.cancelTask(task);
		process(process, (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(null, Loader.config.getString("control-panel."+process.name().toLowerCase()+".end.wait"))));
	}
}
