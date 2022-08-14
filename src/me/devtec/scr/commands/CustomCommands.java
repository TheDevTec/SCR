package me.devtec.scr.commands;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.server_managment.SCR_Command;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class CustomCommands {

	public static HashMap<String, CCommand> custom_commands = new HashMap<>(); //name | CCommand
	
	public static void load() {
		//Utility
		File file = new File("plugins/SCR/custom commands");
		if(file.exists() && file.isDirectory()) {
			for(File f : file.listFiles()) {
				Config c = new Config(f);
				if(c.getBoolean("enabled")) {
					CCommand cmd = new CCommand(c);
					cmd.createCommand();
					custom_commands.put(f.getName(), cmd);
					Loader.plugin.getLogger().info("[CustomCommands] Loaded "+c.getString("name"));
				}
			}
		}
	}
	public static void reload() {	
		//Utility
		File file = new File("plugins/SCR/custom commands");
		if(file.exists() && file.isDirectory()) {
			for(File f : file.listFiles()) {
				Config c = new Config(f);
				CCommand cmd = custom_commands.get(f.getName());
				cmd.newConfig(c);
			}
		}
	}
	
	public static class CCommand {

		public CCommand(Config config) {
			this.c = config;
		}
		private Config c;
		private CommandStructure<CommandSender> cmd;
		
		public void newConfig(Config config) {
			this.c = config;
		}
		
		@SuppressWarnings("unused")
		public void createCommand() {
			List<String> cmds = c.getStringList("cmds");
			
			cmd = CommandStructure.create(CommandSender.class, SCR_Command.PERMS_CHECKER, (s, structure, args) -> {
				actions(s, "cmd", "default", args);
			}).permission(mainPermission());
			
			for(String subcmd : c.getKeys("command")) {
				if(!subcmd.equalsIgnoreCase("cmd")) {
					Bukkit.broadcastMessage("|||| LOADING SubCmd: "+subcmd);
					loadSubCommands(subcmd, "");
					Bukkit.broadcastMessage("|||| LOADED SubCmd: "+subcmd);
					cmd = cmd.first();
				}
			}
			/*for(int i = 0; c.exists("command."+i) ; i++) {
				Bukkit.broadcastMessage("i:"+i);
				int subcom = 0;
				for(String subcommand : c.getKeys("command."+i) ) {
					Bukkit.broadcastMessage("SubCmd: "+i+" ; "+subcommand);
					
					Selector selector = null;
					for(Selector sel : Selector.values()) {
						if(sel.toString().equals(subcommand))
							selector = Selector.valueOf(subcommand);
					}
					
					if(selector != null) {
						cmd.selector(selector, (s, structure, args) -> {
							if(Selector.valueOf(subcommand) == Selector.ENTITY_SELECTOR)
								for(Player p : playerSelectors(s, args[(args.length-1)] ))
									actions(s, (args.length-1)+"."+subcommand, "default", args);
							actions(s, (args.length-1)+"."+subcommand, "default", args);
						});
					}else {
						cmd.argument(subcommand, (s, structure, args) -> {
							actions(s, (args.length-1)+"."+subcommand, "default", args);
						});
					}
					Bukkit.broadcastMessage("SIZE: "+(c.getKeys("command."+i).size()-1)+" ; "+subcom);
					if( (c.getKeys("command."+i).size()-1)>subcom) {
						cmd.parent();
						Bukkit.broadcastMessage("parenting");
						
					}
					subcom++;
				}
			}*/
			
			
			cmd.build().register(cmds.remove(0), cmds.toArray(new String[0]));
		}
		// command.<subcommand>
		// path always needs to end with '.'
		@SuppressWarnings("unused")
		private void loadSubCommands(String subcommand, String path) {
			int subcom = 1;
			
			for(String thing : c.getKeys("command."+path+""+subcommand)) {
				//thing -> actions, next subcommand
				if(thing.equalsIgnoreCase("actions")) {
					
					Selector selector = null; //Selectors
					for(Selector sel : Selector.values()) {
						if(sel.toString().equals(subcommand))
							selector = Selector.valueOf(subcommand);
					}
					if(selector != null) { //Selector
						cmd = cmd.selector(selector, (s, structure, args) -> {
							if(Selector.valueOf(subcommand) == Selector.ENTITY_SELECTOR)
								for(Player p : playerSelectors(s, args[(args.length-1)] ))
									actions(s, path+""+subcommand, "default", args);
							actions(s, path+""+subcommand, "default", args);
						});
					}else { //Argument
						cmd = cmd.argument(subcommand, (s, structure, args) -> {
							actions(s, path+""+subcommand, "default", args);
						});
					}
				}else
					loadSubCommands(thing, path+""+subcommand+".");
				
				if( (c.getKeys("command."+path+""+subcommand).size()) > subcom && !thing.equalsIgnoreCase("actions")) {
					cmd = cmd.parent();
				}
				subcom++;
			}
			
		}
		// command.+'path'+.actions.+'actionp'
		private void actions(CommandSender s, String path, String actionp, String[] args) {
			Bukkit.broadcastMessage(path+" ; "+actionp);
			for(String action : c.getStringList("command."+path+".actions."+actionp)) {
				if(action.startsWith("con:")) {
					String[] conline = action.split(" ");
					String condition = conline[0].replace("con:", "");
					Bukkit.broadcastMessage("CheckingCondition");
					if(ConditionPositive(s, condition, args))
						continue;
					else {
						if(conline[1].startsWith("help:"))
							help(s, conline[1].replace("help:", ""), args);
						else
							actions(s, path, conline[1], args);
						break;
					}
				}
				if(action.startsWith("help:")) {
					help(s, action.replace("help:", ""), args);
				}
				if(action.startsWith("cmd ")) {
					cmd(s, action.replaceFirst("cmd ", ""), getPlaceholders(args).addPlayer("player", s));
				}
				if(action.startsWith("msg ")) {
					msg(s, action.replaceFirst("msg ", ""), getPlaceholders(args).addPlayer("player", s));
				}
			}
			
		}
		//Conditions check
		private boolean ConditionPositive(CommandSender s, String condition, String[] args) {
			//InBuild CONDITIONS
			if(condition.startsWith("havePerm:")) { //just for sender
				String perm = condition.replace("havePerm:", "");
				return s.hasPermission(perm);
			}
			if(condition.equalsIgnoreCase("mustBePlayer")) { //just for sender
				if(s instanceof Player)
					return true;
				return false;
			}
			if(condition.equalsIgnoreCase("isPlayer")) { //just for sender
				if(s instanceof Player)
					return true;
				return false;
			}
			if(condition.equalsIgnoreCase("mustBeConsole")) { //just for sender
				if(s instanceof Player)
					return false;
				return true;
			}
			if(condition.equalsIgnoreCase("isConsole")) { //just for sender
				if(s instanceof Player)
					return false;
				return true;
			}
			if(condition.startsWith("isOnline")) {
				int a = (args.length-1);
				if(!condition.equalsIgnoreCase("isOnline")&& condition.startsWith("isOnline:"))
					a = StringUtils.getInt(condition.replace("isOnline:", ""));
				if(Bukkit.getPlayer( args[a] ) != null)
					return true;
				return false;
			}
			if(condition.startsWith("isWorld")) {
				int a = (args.length-1);
				if(!condition.equalsIgnoreCase("isWorld") && condition.startsWith("isWorld:"))
					a = StringUtils.getInt(condition.replace("isWorld:", ""));
				if(Bukkit.getWorld( args[a] )!=null)
					return true;
				return false;
			}
			if(condition.startsWith("isNumber")) {
				int a = (args.length-1);
				if(!condition.equalsIgnoreCase("isNumber") && condition.startsWith("isNumber:"))
					a = StringUtils.getInt(condition.replace("isNumber:", ""));
				return StringUtils.isNumber( args[a] );
			}
			if(condition.startsWith("isMaterial")) {
				int a = (args.length-1);
				if(!condition.equalsIgnoreCase("isMaterial") && condition.startsWith("isMaterial:"))
					a = StringUtils.getInt(condition.replace("isMaterial:", ""));
				return XMaterial.valueOf(args[a])!=null?true:false;
			}
			if(condition.startsWith("isBoolean")) {
				int a = (args.length-1);
				if(!condition.equalsIgnoreCase("isBoolean") && condition.startsWith("isBoolean:"))
					a = StringUtils.getInt(condition.replace("isBoolean:", ""));
				if(args[a].equalsIgnoreCase("true") || args[a].equalsIgnoreCase("on")
						|| args[a].equalsIgnoreCase("false") || args[a].equalsIgnoreCase("off"))
					return true;
				return false;
			}
			//TODO - 260 chyba
			//CUSTOM CONDITION
			String[] con = condition.split(":");
			if(c.exists("conditions."+con[0])) {
				String value = PlaceholderAPISupport.replace(c.getString("conditions."+condition+".value"), s, true);
				if(con.length==1 &&con[1]!=null && !con[1].isEmpty())
					value = args[ StringUtils.getInt(con[1]) ];
				
				for(String positive : c.getStringList("conditions."+condition+".positive")) {
					if( positive.startsWith(">=") &&
							StringUtils.getDouble(value) >= StringUtils.getDouble(positive.replace(">=", "")) )
						return true;
					if( positive.startsWith("<=") &&
							StringUtils.getDouble(value) <= StringUtils.getDouble(positive.replace("<=", "")) )
						return true;
					if( positive.startsWith(">") && !positive.startsWith(">=")&&
							StringUtils.getDouble(value) > StringUtils.getDouble(positive.replace(">", "")) )
						return true;
					if( positive.startsWith("<") && !positive.startsWith("<=") &&
							StringUtils.getDouble(value) < StringUtils.getDouble(positive.replace("<", "")) )
						return true;
					if( positive.startsWith("=") &&
							StringUtils.getDouble(value) == StringUtils.getDouble(positive.replace("=", "")) )
						return true;
					if( positive.startsWith("==") &&
							StringUtils.getDouble(value) == StringUtils.getDouble(positive.replace("==", "")) )
						return true;
					if(value.equalsIgnoreCase(positive))
						return true;
				}
			}
			
			Loader.plugin.getLogger().warning("Condition " + condition + " not found in Custom Command " + c.getFile().getName() + "");
			
			return true;
		}
		/*
		 * InBuildConditions:
		 * 	mustBePlayer/isPlayer - sender must be Player
		 * 	mustBeConsole/isConsole - sender must be Console
		 *  isOnline - argument must be a online player
		 *  havePerm:scr.exampleperm - sender must have subpermission
		 *  isWorld - argument must be a world
		 *  isNumber - argument must be a number
		 *  isMaterial - argument must be a valid material
		 *  isBoolean - argument must be true/false
		 * 
		 *  isOnline:0 - if args[0] is online
		 */
		/*
		 * CustomConditions
		 * >=50
		 * >50
		 * <=50
		 * <50
		 * ==50 (or =50)
		 * <value> -> equalsIgnoreCase
		 */
		
		
		//help.+path
		private void help(CommandSender s, String path, String[] args) {
			MessageUtils.msgConfig(s, "help."+path, c, getPlaceholders(args));
		}
		
		@SuppressWarnings({ "static-access" })
		private Placeholders getPlaceholders(String[] args) {
			Placeholders p = new Placeholders().c();
			for(int i = 0; i<=(args.length-1); i++)
				p.add("args["+i+"]", args[i]);
			return p;
		}
		
		//Aditionals
		
		private static void msg(CommandSender s, String message, Placeholders placeholders) {
			String text = message;
			text = StringUtils.colorize( MessageUtils.placeholder(s, text, placeholders) );
			String lastcolor = null;
			for(String line : text.replace("\\n", "\n").split("\n")) {
				if(lastcolor!=null && lastcolor.length()==1)
					lastcolor = "&"+lastcolor;
				if(lastcolor!=null && lastcolor.length()==7) {
					lastcolor = "&"+lastcolor;
					lastcolor = lastcolor.replace("&x", "#");
				}
				s.sendMessage(StringUtils.colorize(PlaceholderAPISupport.replace((lastcolor==null?line:lastcolor+""+line), s, true)));
				lastcolor = StringUtils.getLastColors(StringUtils.colorize(line));
			}
		}
		private static void cmd(CommandSender s, String command, Placeholders placeholders) {
			Bukkit.getConsoleSender().getServer().dispatchCommand(Bukkit.getConsoleSender(), 
					PlaceholderAPISupport.replace(MessageUtils.placeholder(s, command, placeholders), s, true));
		}
		
		private String mainPermission() {
			if(c.exists("permission"))
				return c.getString("permission");
			return null;
		}
		private Collection<? extends Player> playerSelectors(CommandSender sender, String selector) {
			char lowerCase = selector.equals("*") ? '*' : Character.toLowerCase(selector.charAt(1));
			if (lowerCase == '*' || selector.charAt(0) == '@')
				switch (lowerCase) {
				case 'a':
				case 'e':
				case '*':
					return BukkitLoader.getOnlinePlayers();
				case 'r':
					return Arrays.asList(StringUtils.getRandomFromCollection(BukkitLoader.getOnlinePlayers()));
				case 's':
				case 'p':
					Location pos = null;
					if (sender instanceof Player)
						pos = ((Player) sender).getLocation();
					else if (sender instanceof BlockCommandSender)
						pos = ((BlockCommandSender) sender).getBlock().getLocation();
					else
						pos = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
					double distance = -1;
					Player nearestPlayer = null;
					for (Player sameWorld : pos.getWorld().getPlayers())
						if (distance == -1 || distance < sameWorld.getLocation().distance(pos)) {
							distance = sameWorld.getLocation().distance(pos);
							nearestPlayer = sameWorld;
						}
					return Arrays.asList(nearestPlayer == null ? BukkitLoader.getOnlinePlayers().iterator().next() : nearestPlayer);
				}
			return Arrays.asList(API.getPlayer(selector));
		}
	}
	
}
