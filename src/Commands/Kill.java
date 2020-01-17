package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Kill implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Kill")) {
		if(args.length==0) {
			if(s instanceof Player) {
				Player p = (Player)s;
				p.damage(p.getMaxHealth());
				if(p.isDead())
				Loader.msg(API.replacePlayerName(Loader.s("Kill.Killed"),p), s);
				return true;
			}else {
				Loader.Help(s, "/Kill <player>", "Kill");
				return true;
			}
		}
		if(args.length==1) {
			Player p = Bukkit.getPlayer(args[0]);
			if(p==null) {
				Loader.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}
			p.damage(p.getMaxHealth());
			if(p.isDead())
			Loader.msg(API.replacePlayerName(Loader.s("Kill.Killed"),p), s);
		}}
		return true;
	}}
