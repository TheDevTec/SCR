package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Immune implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		if (Loader.has(s, "Immune", "BanSystem")) {
			if (args.length == 0) {
				if (!(s instanceof Player)) {
					Loader.Help(s, "Immune", "BanSystem");
					return true;
				}
				Player p = (Player) s;
				boolean im = TheAPI.getUser(p).getBoolean("Immune");
				TheAPI.getUser(p).setAndSave("Immune", !im);
				String aw = "Immune." + (im ? "Disable.Other." : "Enabled.Other.");
				Loader.sendMessages(s, aw+"You");
				return true;
			}
			if (Loader.has(s, "Immune", "BanSystem", "Other")) {
				boolean im = TheAPI.getUser(args[0]).getBoolean("Immune");
				TheAPI.getUser(args[0]).setAndSave("Immune", !im);
				String aw = "Immune." + (im ? "Disable.Other." : "Enabled.Other.");
				Loader.sendMessages(s, aw+"Sender");
				if(TheAPI.getPlayerOrNull(args[0])!=null)
				Loader.sendMessages(TheAPI.getPlayerOrNull(args[0]), aw+"Receiver");
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Immune", "BanSystem");
		return true;
	}
}
