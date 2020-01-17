package Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class SetSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if(API.hasPerm(s, "ServerControl.SetSpawn")) {
			if(s instanceof Player) {
				Player p = (Player)s;
				Location local = p.getLocation();
		
		Loader.config.set("Spawn.World", p.getWorld().getName());
		Loader.config.set("Spawn.X", local.getX());
		Loader.config.set("Spawn.Y", local.getY());
		Loader.config.set("Spawn.Z", local.getZ());
		Loader.config.set("Spawn.X_Pos_Head", local.getYaw());
		Loader.config.set("Spawn.Z_Pos_Head", local.getPitch());
		Configs.config.save();
		Loader.msg(Loader.s("Spawn.SpawnSet"), s);
			return true;
			}else {
				Loader.msg(Loader.s("ConsoleErrorMessage"),s);
				return true;}
		}return true;}}