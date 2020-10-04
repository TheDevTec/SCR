package Commands.Info;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import ServerControl.API;
import ServerControl.API.SeenType;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Seen implements CommandExecutor {
	List<String> getS(String a) {
		if(a==null)return Lists.newArrayList();
		List<String> l = Lists.newArrayList();
		for (UUID s : TheAPI.getUsers()) {
			if (TheAPI.getUser(s).getName()!=null && TheAPI.getUser(s).getName().equalsIgnoreCase(a))
				l.add(TheAPI.getUser(s).getName());
		}
		return l;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Seen")) {
			if (args.length == 0) {
				Loader.Help(s, "/Seen <player>", "Seen");
				return true;
			}
			if (TheAPI.existsUser(args[0])) {
				if (TheAPI.getPlayerOrNull(args[0]) != null) {
					TheAPI.msg(Loader.s("Prefix") + API.replacePlayerName(Loader.s("Seen.Online"), args[0])
							.replace("%online%", API.getSeen(args[0], SeenType.Online)), s);
					return true;
				}
				TheAPI.msg(Loader.s("Prefix") + API.replacePlayerName(Loader.s("Seen.Offline"), args[0])
						.replace("%offline%", API.getSeen(args[0], SeenType.Offline)), s);
				return true;
			}
			List<String> sim = getS(args[0]);
			if (sim.isEmpty())
				TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
			else {
				TheAPI.msg(Loader.s("Seen.SimiliarNames").replace("%names%", StringUtils.join(sim, ", "))
						.replace("%list%", StringUtils.join(sim, ", ")), s);
			}
			return true;

		}
		return true;
	}

}
