package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Tpall implements CommandExecutor, TabCompleter {


	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Tpall")) {
			if(s instanceof Player) {
				ArrayList<String> list = new ArrayList<String>();
				for(Player p:TheAPI.getOnlinePlayers()) {
					if(p==s)continue;
if(!Loader.me.getBoolean("Players."+p.getName()+".TpBlock."+s.getName())&&!Loader.me.getBoolean("Players."+p.getName()+".TpBlock-Global")
		||
		Loader.me.getBoolean("Players."+p.getName()+".TpBlock."+s.getName())&&!Loader.me.getBoolean("Players."+p.getName()+".TpBlock-Global")&&s.hasPermission("ServerControl.Tpall.Blocked")
		||
		Loader.me.getBoolean("Players."+p.getName()+".TpBlock."+s.getName())&&Loader.me.getBoolean("Players."+p.getName()+".TpBlock-Global")&&s.hasPermission("ServerControl.Tpall.Blocked")
		||
		!Loader.me.getBoolean("Players."+p.getName()+".TpBlock."+s.getName())&&Loader.me.getBoolean("Players."+p.getName()+".TpBlock-Global")&&s.hasPermission("ServerControl.Tpall.Blocked")
		) {
					list.add(p.getName());
					p.teleport(((Player) s));}}
					Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Tpall")
					.replace("%players%", TheAPI.getStringUtils().join(list,", ")), s);
					return true;
					}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		return c;
	}}