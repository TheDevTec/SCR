package me.devtec.scr.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;

public abstract class CommandHolder implements CommandExecutor, TabCompleter {

	public static int[] placeholder_FIRST = new int[]{1};
	public static int[] placeholder_TWO = new int[]{2};
	public static int[] placeholder_FIRST_TWO = new int[]{1,2};
	
	protected String command;
	int startArg;
	
	public CommandHolder(String command, int startArg) {
		this.command=command;
		this.startArg=startArg;
	}
	
	public boolean check(CommandSender s) {
		if (hasPerms(s, null)) {
			if(!canUse()) { //Cooldown
				//Cooldown msg
				return false;
			}
			return true;
		}
		noPerms(s);
		return false;
	}
	
	public void noPerms(CommandSender s) {
		Loader.send(s, "missing.perms", PlaceholderBuilder.make(s, "sender"));
	}

	private boolean canUse() {
		return true;
	}

	public Player requireOnline(CommandSender s, String playerName) {
		Player player = null;
		for(Player online : Loader.onlinePlayers(s)) {
			if(online.getName().equalsIgnoreCase(playerName)) {
				player=online;
				break;
			}
		}
		if(player==null)
			Loader.send(s, "missing.player", PlaceholderBuilder.make(s, "sender").add("player", playerName));
		return player;
	}
	
	public boolean hasPerms(CommandSender s, String section) {
		return s.hasPermission(ConfigManager.commands.getString(command+(section==null?".perm":".perm."+section)));
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String alias, String[] args) {
		if(check(s)) {
			if(startArg <= args.length)help(s, args.length);
			boolean silent = false;
			if(args.length>0 && args[args.length-1].endsWith("-s")) {
				args=args.length==1?new String[0]:Arrays.copyOfRange(args, 0, args.length-1);
				silent=true;
			}
			command(s, args, replace(s, args, playerPlaceholders(s, args)),silent);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (hasPerms(s, null)) {
			List<String> tab = new ArrayList<>();
			
			return tab;
		}
		return Collections.emptyList();
	}
	
	public void help(CommandSender s, int length) {
		for(String msg : ConfigManager.commands.getStringList(command+".help."+length))
			TheAPI.msg(PlaceholderAPI.setPlaceholders(s instanceof Player ? (Player)s:null, msg), s);
	}
	
	public abstract void command(CommandSender s, String[] args, boolean loop, boolean silent);

	
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
