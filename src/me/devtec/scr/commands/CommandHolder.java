package me.devtec.scr.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.scr.ConfigManager;
import me.devtec.scr.Loader;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;

public abstract class CommandHolder implements CommandExecutor, TabCompleter {

	public final static int[] placeholder_FIRST = new int[]{1};
	public final static int[] placeholder_TWO = new int[]{2};
	public final static int[] placeholder_FIRST_TWO = new int[]{1,2};
	private final static String[] EMPTY_ARRAY = new String[0];
	
	protected final String command;
	protected final Map<Integer, List<String>> tabbing = new HashMap<>();
	protected final int startArg;
	
	public CommandHolder(String command, int startArg) {
		this.command=command;
		this.startArg=startArg;
		for(String id : ConfigManager.commands.getKeys(command+".tab-completer"))
			tabbing.put(StringUtils.getInt(id), ConfigManager.commands.getStringList(command+".tab-completer."+id));
	}
	
	public final boolean check(CommandSender s) {
		if (hasPerms(s, null)) {
			if(!canUse()) { //Cooldown
				Loader.send(s, "cooldown", PlaceholderBuilder.make(s, "sender").add("cooldown", getCooldown(s)));
				return false;
			}
			return true;
		}
		noPerms(s);
		return false;
	}
	
	public final void noPerms(CommandSender s) {
		Loader.send(s, "missing.perms", PlaceholderBuilder.make(s, "sender"));
	}
	
	public final double getCooldown(CommandSender s) {
		return new CooldownAPI(s.getName()).getCooldown("scr-cmd."+command);
	}

	private final boolean canUse() {
		return true;
	}

	public final Player requireOnline(CommandSender s, String playerName) {
		for(Player online : Loader.onlinePlayers(s)) {
			if(online.getName().equalsIgnoreCase(playerName)) {
				return online;
			}
		}
		Loader.send(s, "missing.player", PlaceholderBuilder.make(s, "sender").add("player", playerName));
		return null;
	}
	
	public final boolean hasPerms(CommandSender s, String section) {
		return s.hasPermission(ConfigManager.commands.getString(command+(section==null?".perm":".perm."+section)));
	}

	@Override
	public final boolean onCommand(CommandSender s, Command cmd, String alias, String[] args) {
		if(check(s)) {
			if(startArg <= args.length)help(s, args.length);
			boolean silent = false;
			if(args.length>0 && args[args.length-1].endsWith("-s")) {
				args=args.length==1?EMPTY_ARRAY:Arrays.copyOfRange(args, 0, args.length-1);
				silent=true;
			}
			command(s, args, replace(s, args, playerPlaceholders(s, args)),silent);
		}
		return true;
	}

	@Override
	public final List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (!tabbing.isEmpty() && hasPerms(s, null)) {
			return tab(s,args);
		}
		return Collections.emptyList();
	}
	
	private List<String> tab(CommandSender sender, String[] args){
		List<String> s = new ArrayList<>();
		List<String> tab = tabbing.get(args.length);
		if(tab==null)
			tab=tabbing.get(tabbing.size()); //last tabbable
		for(String value : tab) {
			if(value.matches("\\{[0-9]+\\}")) {
				s.add(args[Integer.parseInt(value.substring(1,value.length()-1))]);
				continue;
			}
			if(value.equals("{online-players}")||value.equals("{seen-players}")||value.equals("{visible-players}")||value.equals("{players}")) {
				s.addAll(Loader.onlinePlayerNames(sender));
				continue;
			}
			if(value.equals("{all-players}")) {
				List<String> players = new ArrayList<>();
				for(Player player : TheAPI.getOnlinePlayers())players.add(player.getName());
				s.addAll(players);
				continue;
			}
			s.add(value);
		}
		return s;
	}
	
	public List<String> tabValues(CommandSender sender, String[] args, String value){
		return Collections.emptyList();
	}
	
	public final void help(CommandSender s, int length) {
		for(String msg : ConfigManager.commands.getStringList(command+".help."+length))
			TheAPI.msg(PlaceholderAPI.setPlaceholders(s instanceof Player ? (Player)s:null, msg), s);
	}
	
	public abstract void command(CommandSender s, String[] args, boolean loop, boolean silent);

	
	public abstract int[] playerPlaceholders(CommandSender s, String[] args);
	
	private static boolean replace(CommandSender s, String[] args, int... arg) {
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

	public static String findNearestPlayer(CommandSender s) {
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
