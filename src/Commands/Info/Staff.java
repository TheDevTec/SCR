package Commands.Info;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Staff implements CommandExecutor {

	private static List<String> p = new ArrayList<String>(); // Player, group;

	private static String getGroup(Player a) {
		try {
			if (API.existVaultPlugin())
				if (Loader.vault != null)
					if (Loader.vault.getPrimaryGroup(a) != null)
						return Loader.vault.getPrimaryGroup(a);
		} catch (Exception e) {
			return "default";
		}
		return "default";
	}

	public static void sortPlayers() {
		p.clear();
		for (Player a : TheAPI.getOnlinePlayers()) {
			if (Loader.config.getStringList("StaffList").contains(getGroup(a))) {
				p.add(a.getName());
			}
		}
	}

	public static String getStaff() {
		if (p.isEmpty())
			return "0";
		return StringUtils.join(p, ", ");
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Staff", "Info")) {
			sortPlayers();
			for (String a : Loader.trans.getStringList("PlayerList.Staff")) {
				TheAPI.msg(a.replace("%online%", p.size() + "").replace("%max_players%", Bukkit.getMaxPlayers() + "")
						.replace("%prefix%", Loader.s("Prefix")).replace("%staff%", getStaff()), s);
			}
			return true;
		}
		Loader.noPerms(s, "Staff", "Info");
		return true;
	}

}
