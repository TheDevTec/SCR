package Commands;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class KillAll implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.KillAll")) {
			int amount = 0;
			ArrayList<String> pl = new ArrayList<String>();
			for(Player p :Bukkit.getOnlinePlayers()) {
				p.setHealth(0);
				if(p.isDead()) {
				pl.add(p.getName());
				++amount;
				}
			}
				Loader.msg(Loader.s("Kill.KilledAll").replace("%amount%", amount+"").replace("%players%", StringUtils.join(pl,",")), s);
				return true;
		}
		return true;
	}}
