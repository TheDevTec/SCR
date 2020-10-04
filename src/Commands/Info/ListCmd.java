package Commands.Info;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class ListCmd implements CommandExecutor {
	HashMap<Player, String> p = new HashMap<Player, String>(); // Player, group;

	public String getGroup(Player a) {
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

	public void sortPlayers() {
		p.clear();
		for (Player a : TheAPI.getOnlinePlayers()) {
			if (p.get(a) != null)
				p.put(a, getGroup(a));
		}
	}

	public String normalPlayers() {
		ArrayList<String> w = new ArrayList<String>();
		for (Player a : TheAPI.getOnlinePlayers()) {
			if (!Loader.config.getStringList("StaffList").contains(p.get(a))) {
				w.add(a.getName());
			}
		}
		if (w.isEmpty())
			return "0";
		return StringUtils.join(w, ", ");
	}

	public String allPlayers() {
		ArrayList<String> w = new ArrayList<String>();
		for (Player a : TheAPI.getOnlinePlayers()) {
			w.add(a.getName());
		}
		if (w.isEmpty())
			return "0";
		return StringUtils.join(w, ", ");
	}

	public String getPlayersInGroup(String f) {
		ArrayList<String> w = new ArrayList<String>();
		for (Player a : TheAPI.getOnlinePlayers()) {
			if (f.equals(p.get(a))) {
				w.add(a.getName());
			}
		}
		if (w.isEmpty())
			return "";
		return StringUtils.join(w, " ");
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.List")) {
			ArrayList<String> w = new ArrayList<String>();
			sortPlayers();
			w.clear();
			if (API.existVaultPlugin() && Loader.vault != null)
				for (String d : Loader.vault.getGroups()) {
					w.add(d);
				}
			for (String a : Loader.trans.getStringList("PlayerList.Normal")) {
				if (!w.isEmpty())
					for (String wa : w) {
						a = a.replace("%" + wa + "%", getPlayersInGroup(wa));
					}
				TheAPI.msg(a.replace("%online%", TheAPI.getOnlinePlayers().size() + "")
						.replace("%max_players%", Bukkit.getMaxPlayers() + "").replace("%staff%", Staff.getStaff())
						.replace("%players%", normalPlayers()).replace("%all%", allPlayers())
						.replace("%prefix%", Loader.s("Prefix")), s);
			}
			return true;
		}
		return true;

	}
}
