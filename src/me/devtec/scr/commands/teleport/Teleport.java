package me.devtec.scr.commands.teleport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.utils.Formatters;
import me.devtec.scr.utils.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Teleport extends CommandHolder {
	public Teleport(String command) { 
		//tp [player] {player}
		//tp {player} x y z {yaw} {pitch} {WORLD}
		super(command, 0);
	}
	
	@Override
	public void command(CommandSender sender, String[] args, boolean loop, boolean silent) {
		Player target, target2;
		double x, y, z, yaw, pitch;
		World world = sender instanceof Player ? ((Player)sender).getWorld() : null;
		
		switch(args.length) {
		case 1:
			if(!(sender instanceof Player)) {
				help(sender, 1);
				return;
			}
			if((target=requireOnline(sender, args[0]))==null)return;
			((Player)sender).teleport(target);
			if(!silent)
				Loader.send(sender, "teleport.tp.player.self", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
			break;
		case 2:
			if((target2=requireOnline(sender, args[1]))==null)return;
			if(loop) {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(player==target2)continue;
					player.teleport(target2);
					if(!silent) {
						if(sender==target2 || sender == player) {
							Loader.send(sender, "teleport.tp.player.self", PlaceholderBuilder.make(sender, "sender").player(player, "target"));
						}else {
							Loader.send(sender, "teleport.tp.player.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").player(target2, "sec_target"));
							Loader.send(player, "teleport.tp.player.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").player(target2, "sec_target"));
							Loader.send(target2, "teleport.tp.player.other.sec_target", PlaceholderBuilder.make(sender, "sender").player(player, "target").player(target2, "sec_target"));
						}
					}
				}
			}
			if((target=requireOnline(sender, args[0]))==null)return;
			target.teleport(target2);
			if(!silent) {
				if(sender==target2 || sender == target) {
					Loader.send(sender, "teleport.tp.player.self", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
				}else {
					Loader.send(sender, "teleport.tp.player.other.sender", PlaceholderBuilder.make(sender, "sender").player(target, "target").player(target2, "sec_target"));
					Loader.send(target, "teleport.tp.player.other.target", PlaceholderBuilder.make(sender, "sender").player(target, "target").player(target2, "sec_target"));
					Loader.send(target2, "teleport.tp.player.other.sec_target", PlaceholderBuilder.make(sender, "sender").player(target, "target").player(target2, "sec_target"));
				}
			}
			break;
		case 3:
			if(!(sender instanceof Player)) {
				help(sender, 3);
				return;
			}
			x=StringUtils.calculate(args[0].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
			y=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
			z=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
			yaw=((Player)sender).getLocation().getYaw();
			pitch=((Player)sender).getLocation().getPitch();
			((Player)sender).teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
			if(!silent) {
				Loader.send(sender, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			}
			break;
		case 4:
			target=TheAPI.getPlayer(args[0]);
			if(target==null) {
				if(loop) {
					for(Player player : TheAPI.getOnlinePlayers()) {
						yaw=player.getLocation().getYaw();
						pitch=player.getLocation().getPitch();
						x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
						y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
						z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
						player.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
						if(!silent) {
							if(player==sender) {
								Loader.send(sender, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}else {
								Loader.send(player, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
								Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}
						}
					}
					return;
				}
				if(!(sender instanceof Player)) {
					//player offline
					return;
				}
				yaw=((Player)sender).getLocation().getYaw();
				pitch=((Player)sender).getLocation().getPitch();
				x=StringUtils.calculate(args[0].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
				y=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
				z=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
				yaw=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
				((Player)sender).teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
				Loader.send(sender, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				return;
			}
			yaw=target.getLocation().getYaw();
			pitch=target.getLocation().getPitch();
			x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
			y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
			z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
			target.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
			if(target==sender) {
				Loader.send(target, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				return;
			}
			Loader.send(target, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
					.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
					.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
					.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
					.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			break;
		case 5:
			target=TheAPI.getPlayer(args[0]);
			if(target==null) {
				if(loop) {
					for(Player player : TheAPI.getOnlinePlayers()) {
						pitch=player.getLocation().getPitch();
						x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
						y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
						z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
						yaw=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
						player.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
						if(!silent) {
							if(player==sender) {
								Loader.send(sender, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}else {
								Loader.send(player, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
								Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}
						}
					}
					return;
				}
				if(!(sender instanceof Player)) {
					//player offline
					return;
				}
				if(Bukkit.getWorld(args[4])==null) {
					x=StringUtils.calculate(args[0].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
					y=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
					z=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
					yaw=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
					pitch=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getPitch() : 0)+""));
				}else {
					world=Bukkit.getWorld(args[4]);
					x=StringUtils.calculate(args[0].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
					y=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
					z=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
					yaw=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
					pitch=((Player)sender).getLocation().getPitch();
				}
				((Player)sender).teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
				Loader.send(target, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				return;
			}
			pitch=target.getLocation().getPitch();
			x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
			y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
			z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
			yaw=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
			target.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
			if(target==sender) {
				Loader.send(target, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				return;
			}
			Loader.send(target, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
					.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
					.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
					.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
					.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			break;
		case 6:
			target=TheAPI.getPlayer(args[0]);
			if(target==null) {
				if(loop) {
					for(Player player : TheAPI.getOnlinePlayers()) {
						x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
						y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
						z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
						yaw=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
						pitch=StringUtils.calculate(args[5].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getPitch() : 0)+""));
						player.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
						if(!silent) {
							if(player==sender) {
								Loader.send(sender, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}else {
								Loader.send(player, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
								Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}
						}
					}
					return;
				}
				if(!(sender instanceof Player)) {
					//player offline
					return;
				}
				if(Bukkit.getWorld(args[5])==null) {
					//msg - svět neexistuje
				}else {
					world=Bukkit.getWorld(args[5]);
					x=StringUtils.calculate(args[0].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
					y=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
					z=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
					yaw=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
					pitch=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getPitch() : 0)+""));
					((Player)sender).teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
					Loader.send(target, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
							.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
							.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				}
				return;
			}
			x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
			y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
			z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
			yaw=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
			pitch=StringUtils.calculate(args[5].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getPitch() : 0)+""));
			target.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
			if(target==sender) {
				Loader.send(target, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				return;
			}
			Loader.send(target, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
					.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
					.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
					.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
					.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
			break;
			default: //length 7 or more
				world=Bukkit.getWorld(args[6]);
				if(world==null) {
					//msg - svět neexistuje
					return;
				}
				x=StringUtils.calculate(args[1].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getX() : 0)+""));
				y=StringUtils.calculate(args[2].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getY() : 0)+""));
				z=StringUtils.calculate(args[3].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getZ() : 0)+""));
				yaw=StringUtils.calculate(args[4].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getYaw() : 0)+""));
				pitch=StringUtils.calculate(args[5].replace("~", (sender instanceof Player ? ((Player)sender).getLocation().getPitch() : 0)+""));
				if(loop) {
					for(Player player : TheAPI.getOnlinePlayers()) {
						player.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
						if(!silent) {
							if(player==sender) {
								Loader.send(sender, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}else {
								Loader.send(player, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
								Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(player, "target").add("x", Formatters.formatDouble(x))
										.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
										.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
							}
						}
					}
					return;
				}
				if((target=requireOnline(sender, args[0]))==null)return;
				target.teleport(new Location(world,x,y,z,(float)yaw,(float)pitch));
				if(target==sender) {
					Loader.send(target, "teleport.tp.location.self", PlaceholderBuilder.make(sender, "sender").add("x", Formatters.formatDouble(x))
							.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
							.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
					return;
				}
				Loader.send(target, "teleport.tp.location.other.target", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				Loader.send(sender, "teleport.tp.location.other.sender", PlaceholderBuilder.make(sender, "sender").player(target, "target").add("x", Formatters.formatDouble(x))
						.add("y", Formatters.formatDouble(y)).add("z", Formatters.formatDouble(z))
						.add("yaw", Formatters.formatDouble(yaw)).add("pitch", Formatters.formatDouble(pitch)).add("world", world.getName()));
				break;
		}
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return placeholder_FIRST;
	}
	
	@Override
	public List<String> tabValues(CommandSender sender, String[] args, String value) {
		if(value.equalsIgnoreCase("{worlds}")) {
			List<String> worlds = new ArrayList<>(Bukkit.getWorlds().size());
			for(World world : Bukkit.getWorlds())worlds.add(world.getName());
			return worlds;
		}
		if(value.equalsIgnoreCase("{world}")) {
			return sender instanceof Player ? Arrays.asList(((Player)sender).getWorld().getName()) : Arrays.asList("world");
		}
		if(value.equalsIgnoreCase("{x}")) {
			return sender instanceof Player ? Arrays.asList(Formatters.formatDouble(((Player)sender).getLocation().getX())) : Arrays.asList("x");
		}
		if(value.equalsIgnoreCase("{y}")) {
			return sender instanceof Player ? Arrays.asList(Formatters.formatDouble(((Player)sender).getLocation().getY())) : Arrays.asList("y");
		}
		if(value.equalsIgnoreCase("{z}")) {
			return sender instanceof Player ? Arrays.asList(Formatters.formatDouble(((Player)sender).getLocation().getZ())) : Arrays.asList("z");
		}
		if(value.equalsIgnoreCase("{yaw}")) {
			return sender instanceof Player ? Arrays.asList(Formatters.formatDouble(((Player)sender).getLocation().getYaw())) : Arrays.asList("yaw");
		}
		if(value.equalsIgnoreCase("{pitch}")) {
			return sender instanceof Player ? Arrays.asList(Formatters.formatDouble(((Player)sender).getLocation().getPitch())) : Arrays.asList("pitch");
		}
		return super.tabValues(sender, args, value);
	}
}