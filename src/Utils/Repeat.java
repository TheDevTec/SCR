package Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

public class Repeat {
	public static void a(CommandSender s, String c) {
		for(Player p:TheAPI.getCountingAPI().getOnlinePlayers()) {
			String r = c.replace("*", p.getName());
			TheAPI.sudoConsole(SudoType.COMMAND, r);
		}
	}
}
