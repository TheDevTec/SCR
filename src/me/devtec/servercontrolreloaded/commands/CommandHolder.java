package me.devtec.servercontrolreloaded.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public abstract class CommandHolder implements CommandExecutor, TabCompleter {
	protected String section, name;
	public CommandHolder(String section, String name) {
		this.section=section;
		this.name=name;
	}
	
	public boolean check(CommandSender s) {
		if (Loader.has(s, name, section)) {
			if(!CommandsManager.canUse(section+"."+name, s)) { //Cooldown
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire(section+"."+name, s))));
				return false;
			}
			return true;
		}
		Loader.noPerms(s, name, section);
		return false;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String alias, String[] args) {
		if(check(s))command(s, args, replace(s, args, playerPlaceholders(s, args)));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (Loader.has(s, name, section)) {
			List<String> tab = tabCompleter(s, args);
			return tab==null?API.getPlayerNames(s):tab;
		}
		return Collections.emptyList();
	}
	
	public void help(CommandSender s) {
		Loader.Help(s, section, name);
	}
	
	public abstract List<String> tabCompleter(CommandSender s, String[] args);

	public abstract void command(CommandSender s, String[] args, boolean loop);

	@Nullable
	public abstract int[] playerPlaceholders(CommandSender s, String[] args);
	
	private boolean replace(CommandSender s, String[] args, int... arg) {
		if(arg==null||arg.length==0)return false;
		boolean loop = false;
		for(int i : arg) {
			args[i]=args[i].replace("@r", TheAPI.getRandomPlayer().getName())
					.replace("@s", s.getName()).replace("@p", s instanceof Player?s.getName():findNearestPlayer(s));
			if(args[i].contains("@a")||args[i].contains("@e")||args[i].contains("*"))
				loop=true;
		}
		return loop;
	}

	private String findNearestPlayer(CommandSender s) {
		if(s instanceof BlockCommandSender) {
			Block where = ((BlockCommandSender)s).getBlock();
			Player nearest = null;
			double range = -1;
			for(Player p : where.getWorld().getPlayers()) {
				if(range==-1) {
					nearest=p;
					range=where.getLocation().distance(p.getLocation());
				}else {
					double playerRange = where.getLocation().distance(p.getLocation());
					if(playerRange < range) {
						nearest=p;
						range=playerRange;
					}
				}
			}
			if(nearest==null)nearest=TheAPI.getPlayer(0);
			return nearest!=null?nearest.getName():"@p";
		}
		Player first = TheAPI.getPlayer(0);
		return first!=null?first.getName():"@p";
	}
}
