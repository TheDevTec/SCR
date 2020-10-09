package Commands.Other;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Item implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		//must rewrite code
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		//must rewrite code
		return c;
	}
}
