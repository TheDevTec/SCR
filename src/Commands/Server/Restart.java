package Commands.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import Commands.Server.BigTask.TaskType;
import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Restart implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Restart")) {
			if (args.length == 0) {
				BigTask.start(TaskType.RESTART, Loader.config.getLong("Options.WarningSystem.Restart.PauseTime"));
				return true;
			}
			if (args[0].equalsIgnoreCase("cancel")) {
				BigTask.cancel();
				return true;
			}
			if (args[0].equalsIgnoreCase("now")) {
				BigTask.start(TaskType.RESTART, 0);
				return true;
			}
			if (BigTask.r == -1)
				BigTask.start(TaskType.RESTART, StringUtils.getTimeFromString(args[0]));
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("ServerControl.Restart") && args.length == 1)
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("15s", "30s", "now", "cancel"),
					new ArrayList<>()));
		return c;
	}
}
