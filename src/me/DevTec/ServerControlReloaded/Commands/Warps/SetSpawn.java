package me.DevTec.ServerControlReloaded.Commands.Warps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.TheAPI.Utils.Position;

public class SetSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
			if (s instanceof Player) {
				if (Loader.has(s, "SetSpawn", "Warps")) {
				Player p = (Player) s;
				Position local = new Position(p.getLocation());
				try {
				p.getWorld().setSpawnLocation(local.toLocation());
				}catch(NoSuchMethodError err) {
				}
				Loader.config.set("Spawn", local.toString());
				Loader.config.save();
				Loader.sendMessages(s, "Spawn.Set", Placeholder.c()
						.add("%x%", String.format("%2.02f", local.getX()).replaceFirst("\\.00", ""))
						.add("%y%", String.format("%2.02f", local.getY()).replaceFirst("\\.00", ""))
						.add("%z%", String.format("%2.02f", local.getZ()).replaceFirst("\\.00", ""))
						.add("%world%", local.getWorldName()));
				return true;
			}
			Loader.noPerms(s, "SetSpawn", "Warps");
			return true;
		}
		return true;
	}
}