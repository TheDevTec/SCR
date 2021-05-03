package me.devtec.servercontrolreloaded.commands.warps;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;

public class SetSpawn implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			if (s instanceof Player) {
				if (Loader.has(s, "SetSpawn", "Warps")) {
				Player p = (Player) s;
				Position local = new Position(p.getLocation());
				try {
					local.getWorld().setSpawnLocation(local.toLocation());
				}catch(NoSuchMethodError err) {
					local.getWorld().setSpawnLocation(local.getBlockX(), local.getBlockY(), local.getBlockZ());
				}
				Loader.config.set("Spawn", local.toString());
				Loader.config.save();
				Loader.sendMessages(s, "Spawn.Set", Placeholder.c()
						.add("%x%", StringUtils.fixedFormatDouble(local.getX()))
						.add("%y%", StringUtils.fixedFormatDouble(local.getY()))
						.add("%z%", StringUtils.fixedFormatDouble(local.getZ()))
						.add("%world%", local.getWorldName()));
				return true;
			}
			Loader.noPerms(s, "SetSpawn", "Warps");
			return true;
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Arrays.asList();
	}
}