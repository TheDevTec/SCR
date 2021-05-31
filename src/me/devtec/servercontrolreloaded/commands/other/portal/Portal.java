package me.devtec.servercontrolreloaded.commands.other.portal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.particlesapi.Particle;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.reflections.Ref;

public class Portal implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unchecked")
	public Portal() {
		new Particle("HEART"); //inicialize class
		part = ((Map<String,Object>)Ref.getStatic(Particle.class, "identifier")).keySet();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Portal","Other")) {
			if(!CommandsManager.canUse("Other.Portal", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Portal", s))));
				return true;
			}
			if(args.length==0) {
				Loader.Help(s, "Portal", "Other");
				return true;
			}
			if(args[0].equalsIgnoreCase("create")) {
				if(!Loader.portals.exists(args[1])) {
					Loader.portals.addDefault(args[1]+".cmds", Arrays.asList());
					Loader.portals.save();
					Loader.sendMessages(s, "Portals.Create", Placeholder.c().add("%name%", args[1]));
					return true;
				}
				Loader.sendMessages(s, "Portals.Exists", Placeholder.c().add("%name%", args[1]));
				return true;
			}
			if(args[0].equalsIgnoreCase("delete")) {
				if(Loader.portals.exists(args[1])) {
					Loader.portals.remove(args[1]);
					Loader.portals.save();
					me.devtec.servercontrolreloaded.utils.Portal.reload();
					Loader.sendMessages(s, "Portals.Delete", Placeholder.c().add("%name%", args[1]));
					return true;
				}
				Loader.sendMessages(s, "Portals.NotExist", Placeholder.c().add("%name%", args[1]));
				return true;
			}
			if(args[0].equalsIgnoreCase("list")) {
				Loader.sendMessages(s, "Portals.List", Placeholder.c().add("%list%", StringUtils.join(Loader.portals.getKeys(), ", ")));
				return true;
			}
			if(args[0].equalsIgnoreCase("set")) {
				if(args.length<=2) {
					Loader.advancedHelp(s, "Portal", "Other","Set");
					return true;
				}
				if(!Loader.portals.exists(args[2])) {
					Loader.sendMessages(s, "Portals.NotExist", Placeholder.c().add("%name%", args[2]));
					return true;
				}
				if(args[1].equalsIgnoreCase("pos1")) {
					Position f = new Position(((Player)s).getLocation().getBlock().getLocation());
					Loader.portals.set(args[2]+".pos.1", f.toString());
					Loader.portals.save();
					Loader.sendMessages(s, "Portals.PosSet", Placeholder.c().add("%x%", f.getBlockX()+"")
							.add("%name%", args[2])
							.add("%position%","1")
							.add("%world%", f.getWorldName()+"")
							.add("%y%", f.getBlockY()+"").add("zx%", f.getBlockZ()+""));
					me.devtec.servercontrolreloaded.utils.Portal.reload();
					return true;
				}
				if(args[1].equalsIgnoreCase("pos2")) {
					Position f = new Position(((Player)s).getLocation().getBlock().getLocation());
					Loader.portals.set(args[2]+".pos.2", f.toString());
					Loader.portals.save();
					Loader.sendMessages(s, "Portals.PosSet", Placeholder.c().add("%x%", f.getBlockX()+"")
							.add("%name%", args[2])
							.add("%position%","2")
							.add("%world%", f.getWorldName()+"")
							.add("%y%", f.getBlockY()+"").add("zx%", f.getBlockZ()+""));
					me.devtec.servercontrolreloaded.utils.Portal.reload();
					return true;
				}
				if(args[1].equalsIgnoreCase("particle")) {
					Loader.portals.set(args[2]+".particle", args[3]);
					Loader.portals.save();
					Loader.sendMessages(s, "Portals.Particle", Placeholder.c().add("%value%", args[3])
							.add("%name%", args[2]));
					me.devtec.servercontrolreloaded.utils.Portal.reload();
					return true;
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("server")) {
				if(args.length<=2) {
					Loader.advancedHelp(s, "Portal", "Other","Server");
					return true;
				}
				if(!Loader.portals.exists(args[1])) {
					Loader.sendMessages(s, "Portals.NotExist", Placeholder.c().add("%name%", args[1]));
					return true;
				}
				Loader.sendMessages(s, "Portals.Server", Placeholder.c().add("%name%", args[1]).add("%server%", args[2]));
				return true;
			}
			if(args[0].equalsIgnoreCase("cmds")||args[0].equalsIgnoreCase("bcmds")) {
				String path = args[0].equalsIgnoreCase("cmds")?".cmds":".bcmds";
				if(args.length<=2) {
					Loader.advancedHelp(s, "Portal", "Other",args[0].equalsIgnoreCase("cmds")?"Cmds":"BCmds");
					return true;
				}
				if(!Loader.portals.exists(args[2])) {
					Loader.sendMessages(s, "Portals.NotExist", Placeholder.c().add("%name%", args[2]));
					return true;
				}
				if(args[1].equalsIgnoreCase("add")) {
					if(args.length==3) {
						Loader.advancedHelp(s, "Portal", "Other",args[0].equalsIgnoreCase("cmds")?"Cmds":"BCmds");
						return true;
					}
					String id = StringUtils.buildString(4,args);
					List<String> cmds = Loader.portals.getStringList(args[2]+path);
					cmds.add(id);
					Loader.portals.set(args[2]+path,cmds);
					Loader.portals.save();
					Loader.sendMessages(s, "Portals.Cmds.Add", Placeholder.c().add("%name%", args[2]).add("%value%", id));
					me.devtec.servercontrolreloaded.utils.Portal.reload();
					return true;
				}
				if(args[1].equalsIgnoreCase("remove")) {
					if(args.length==3) {
						Loader.advancedHelp(s, "Portal", "Other","Cmds");
						return true;
					}
					int id = StringUtils.getInt(args[3]);
					List<String> cmds = Loader.portals.getStringList(args[2]+path);
					try {
						cmds.remove(id);
					}catch(Exception bound) {}
					Loader.portals.set(args[2]+path,cmds);
					Loader.portals.save();
					Loader.sendMessages(s, "Portals.Cmds.Remove", Placeholder.c().add("%name%", args[2]).add("%position%", id+""));
					me.devtec.servercontrolreloaded.utils.Portal.reload();
					return true;
				}
				if(args[1].equalsIgnoreCase("list")) {
					Loader.sendMessages(s, "Portals.Cmds.List", Placeholder.c().add("%name%", args[2]).add("%list%", StringUtils.join(Loader.portals.getStringList(args[2]+path), "\n")));
					return true;
				}
				return true;
			}
			Loader.Help(s, "Portal", "Other");
			return true;
		}
		Loader.noPerms(s, "Portal", "Other");
		return true;
	}
	
	private Set<String> part;

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Portal","Other")) {
			if(args.length==1) {
				return StringUtils.copyPartialMatches(args[0], Arrays.asList("Create","Delete","Set", "List","Cmds","BCmds","Server"));
			}
			if(args.length==2) {
				if(args[0].equalsIgnoreCase("create"))
					return Arrays.asList("?");
				if(args[0].equalsIgnoreCase("delete")||args[0].equalsIgnoreCase("server"))
				return StringUtils.copyPartialMatches(args[1], Loader.portals.getKeys());
				if(args[0].equalsIgnoreCase("cmds")||args[0].equalsIgnoreCase("bcmds"))
					return StringUtils.copyPartialMatches(args[1], Arrays.asList("Add","Remove","List"));
				if(args[0].equalsIgnoreCase("set"))
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("Pos1","Pos2","Particle"));
			}
			if(args.length==3) {
				if(args[0].equalsIgnoreCase("server"))
					return Arrays.asList("?");
				if(args[0].equalsIgnoreCase("cmds")||args[0].equalsIgnoreCase("bcmds"))
					return StringUtils.copyPartialMatches(args[2], Loader.portals.getKeys());
				if(args[0].equalsIgnoreCase("set")) {
					if(args[1].equalsIgnoreCase("pos1")||args[1].equalsIgnoreCase("pos2")||args[1].equalsIgnoreCase("particle"))
					return StringUtils.copyPartialMatches(args[2], Loader.portals.getKeys());
				}
			}
			if(args.length==4) {
				if(args[0].equalsIgnoreCase("cmds")||args[0].equalsIgnoreCase("bcmds")) {
					if(args[1].equalsIgnoreCase("add"))
						return Arrays.asList("?");
					if(args[1].equalsIgnoreCase("remove")) {
						List<String> c = new ArrayList<>();
						for(int i = 0; i < Loader.portals.getStringList(args[2]+"."+args[0].toLowerCase()).size(); ++i)
							c.add(i+"");
						return StringUtils.copyPartialMatches(args[3], c);
					}
				}
				if(args[0].equalsIgnoreCase("set")) {
					if(args[1].equalsIgnoreCase("particle"))
					return StringUtils.copyPartialMatches(args[3], part);
				}
			}
		}
		return Arrays.asList();
	}

}
