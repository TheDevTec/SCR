package Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if(args.length==0) {
		if(API.hasPerm(s, "ServerControl.Spawn")) {
			float x_head;
			float z_head;
			World world = null;
			Location loc = null;
			if(Loader.config.getString("Spawn")!=null) {
		 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
		 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
		 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
		 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
			}
			if(Loader.config.getString("Spawn")==null) {
				world = Bukkit.getWorlds().get(0);
				 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
			}
		if(s instanceof Player) {
			API.setBack((Player)s);
		((Player)s).teleport(loc);
		Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
					.replace("%world%", world.getName())
					.replace("%player%", s.getName())
					.replace("%playername%", ((Player)s).getDisplayName())
					, s);
			return true;
		}else {
			Loader.Help(s, "/Spawn <player>", "Spawn");
			return true;
		}}return true;}
		if(args.length==1) {
			Player p = Bukkit.getPlayer(args[0]);
			if(p==null) {
				Loader.msg(Loader.PlayerNotEx(args[0]), s);
				return true;
			}
			float x_head;
			float z_head;
			World world = null;
			Location loc = null;
			if(p==s) {
				if(API.hasPerm(s, "ServerControl.Spawn")) {
					if(Loader.config.getString("Spawn")!=null) {
						 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
						 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
						 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
						 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
							}
							if(Loader.config.getString("Spawn")==null) {
								world = Bukkit.getWorlds().get(0);
								 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
							}
							API.setBack(p);
							p.teleport(loc);
								Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
										.replace("%world%", world.getName())
										.replace("%player%", p.getName())
										.replace("%playername%", p.getDisplayName())
										, p);
			return true;
			}return true;}
			if(p!=s) {
				if(API.hasPerm(s, "ServerControl.Spawn.Other")) {
					if(Loader.config.getString("Spawn")!=null) {
						 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
						 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
						 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
						 loc = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
							}
							if(Loader.config.getString("Spawn")==null) {
								world = Bukkit.getWorlds().get(0);
								 loc = Bukkit.getWorlds().get(0).getSpawnLocation();
							}
							API.setBack(p);
							p.teleport(loc);
							Loader.msg(Loader.s("Spawn.TeleportedToSpawn")
									.replace("%world%", world.getName())
									.replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									, p);
							Loader.msg(Loader.s("Spawn.PlayerTeleportedToSpawn")
									.replace("%world%", world.getName())
									.replace("%player%", p.getName())
									.replace("%playername%", p.getDisplayName())
									, s);
			return true;
			}return true;}
			return true;
			}return true;}}
