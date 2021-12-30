package me.devtec.scr;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaceholderBuilder {
	private Map<String, String> placeholders = new HashMap<>();
	private Map<String, CommandSender> commandsenders = new HashMap<>();
	
	public PlaceholderBuilder player(CommandSender sender, String prefix) {
		commandsenders.put(prefix.toLowerCase(), sender);
		return this;
	}
	
	public PlaceholderBuilder add(String placeholder, Object replacement) {
		placeholders.put(placeholder.toLowerCase(), replacement+"");
		return this;
	}
	
	public String apply(String text) {
		for(Entry<String, CommandSender> e : commandsenders.entrySet()) {
			String s = "%"+e.getKey();
			CommandSender d = e.getValue();
			text=text.replace(s+"_name%", d.getName());
			if(d instanceof Player) {
				Player f = (Player)d;
				text=text.replace(s+"_displayname%", f.getDisplayName());
				text=text.replace(s+"_customname%", f.getCustomName());
				text=text.replace(s+"_health%", Formatters.formatDouble(f.getHealth()));
				text=text.replace(s+"_food%", f.getFoodLevel()+"");
				text=text.replace(s+"_air%", f.getRemainingAir()+"");
				
				
				//POSITION
				text=text.replace(s+"_world%", f.getWorld().getName());
				text=text.replace(s+"_pos_fixed_x%", Formatters.formatDouble(f.getLocation().getX()));
				text=text.replace(s+"_pos_fixed_y%", Formatters.formatDouble(f.getLocation().getY()));
				text=text.replace(s+"_pos_fixed_z%", Formatters.formatDouble(f.getLocation().getZ()));
				text=text.replace(s+"_pos_fixed_yaw%", Formatters.formatDouble(f.getLocation().getYaw()));
				text=text.replace(s+"_pos_fixed_pitch%", Formatters.formatDouble(f.getLocation().getPitch()));

				text=text.replace(s+"_pos_x%", f.getLocation().getX()+"");
				text=text.replace(s+"_pos_y%", f.getLocation().getY()+"");
				text=text.replace(s+"_pos_z%", f.getLocation().getZ()+"");
				text=text.replace(s+"_pos_yaw%", f.getLocation().getYaw()+"");
				text=text.replace(s+"_pos_pitch%", f.getLocation().getPitch()+"");
			}else {
				text=text.replace(s+"_displayname%", d.getName());
				text=text.replace(s+"_customname%", d.getName());
				text=text.replace(s+"_health%", "0");
				text=text.replace(s+"_food%", "0");
				text=text.replace(s+"_air%", "0");
				if(d instanceof BlockCommandSender) {
					text=text.replace(s+"_world%", ((BlockCommandSender)d).getBlock().getWorld().getName());

					text=text.replace(s+"_pos_fixed_x%", ((BlockCommandSender)d).getBlock().getX()+"");
					text=text.replace(s+"_pos_fixed_y%", ((BlockCommandSender)d).getBlock().getY()+"");
					text=text.replace(s+"_pos_fixed_z%", ((BlockCommandSender)d).getBlock().getZ()+"");
					text=text.replace(s+"_pos_fixed_yaw%", "0");
					text=text.replace(s+"_pos_fixed_pitch%", "0");

					text=text.replace(s+"_pos_x%", ((BlockCommandSender)d).getBlock().getX()+"");
					text=text.replace(s+"_pos_y%", ((BlockCommandSender)d).getBlock().getY()+"");
					text=text.replace(s+"_pos_z%", ((BlockCommandSender)d).getBlock().getZ()+"");
					text=text.replace(s+"_pos_yaw%", "0");
					text=text.replace(s+"_pos_pitch%", "0");
				}else {
					text=text.replace(s+"_pos_fixed_x%", "0");
					text=text.replace(s+"_pos_fixed_y%", "0");
					text=text.replace(s+"_pos_fixed_z%", "0");
					text=text.replace(s+"_pos_fixed_yaw%", "0");
					text=text.replace(s+"_pos_fixed_pitch%", "0");
					
					text=text.replace(s+"_pos_x%", "0");
					text=text.replace(s+"_pos_y%", "0");
					text=text.replace(s+"_pos_z%", "0");
					text=text.replace(s+"_pos_yaw%", "0");
					text=text.replace(s+"_pos_pitch%", "0");
				}
			}
		}
		for(Entry<String, String> e : placeholders.entrySet())text=text.replace("%"+e.getKey()+"%", e.getValue());
		return text;
	}

	public static PlaceholderBuilder make(CommandSender s, String prefix) {
		return new PlaceholderBuilder().player(s, prefix);
	}
}
