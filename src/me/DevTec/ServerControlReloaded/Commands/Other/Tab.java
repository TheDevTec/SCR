package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Tab implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String awd, String[] args) {
		if(args.length==0) {
			//tablist prefix <player/group> <name> <tablist/nametag> get
			//tablist prefix <player/group> <name> <tablist/nametag> set <value>

			//tablist suffix <player/group> <name> <tablist/nametag> get
			//tablist suffix <player/group> <name> <tablist/nametag> set <value>

			//tablist format <player/group> <name> <tablist/nametag> get
			//tablist format <player/group> <name> <tablist/nametag> set <value>
			
			//tablist header <group/player> add <value>
			//tablist header <group/player> remove <line>
			//tablist header <group/player> set <line> <value>
			//tablist header <group/player> list
			
			//tablist footer <group/player> add <value>
			//tablist footer <group/player> remove <line>
			//tablist footer <group/player> set <line> <value>
			//tablist footer <group/player> list
			return true;
		}
		if(args[0].equalsIgnoreCase("prefix")) {
			if(args.length<6) {
				//tablist prefix <player/group> <name> <tablist/nametag> get
				//tablist prefix <player/group> <name> <tablist/nametag> set <value>
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("suffix")) {
			if(args.length<6) {
				//tablist suffix <player/group> <name> <tablist/nametag> get
				//tablist suffix <player/group> <name> <tablist/nametag> set <value>
				return true;
			}
			if(args[4].contentEquals("get")) {
				
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("format")) {
			if(args.length<6) {
				//tablist format <player/group> <name> <tablist/nametag> get
				//tablist format <player/group> <name> <tablist/nametag> set <value>
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("header")) {
			if(args.length<3) {
				//tablist header <group/player> add <value>
				//tablist header <group/player> remove <line>
				//tablist header <group/player> set <line> <value>
				//tablist header <group/player> list
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("footer")) {
			if(args.length<3) {
				//tablist footer <group/player> add <value>
				//tablist footer <group/player> remove <line>
				//tablist footer <group/player> set <line> <value>
				//tablist footer <group/player> list
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		//must be rewrited
		return c;
	}
}
