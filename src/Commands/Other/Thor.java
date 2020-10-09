package Commands.Other;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.BlocksAPI.BlocksAPI;

public class Thor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if (Loader.has(s, "Thor","Other")) {
			if (args.length == 0) {
				if(s instanceof Player) {
				Player p2 = (Player) s;
				Block b = BlocksAPI.getLookingBlock(p2, 100);
				b.getWorld().strikeLightning(b.getLocation());
				Loader.sendMessages(s, "Thor.Block");
				return true;
				}
				Loader.Help(s, "Thor", "Other");
				return true;
			}
			if (args.length == 1) {
				Player p = TheAPI.getPlayer(args[0]);
				if (p != null) {
					p.getWorld().strikeLightning(p.getLocation());
					Loader.sendMessages(s, "Thor.Player", Placeholder.c().add("%player%", p.getName()).replace("%playername%", p.getDisplayName()));
					return true;
				}
				Loader.notOnline(s, args[0]);
				return true;
			}
			return true;
		}
		return true;
	}

}
