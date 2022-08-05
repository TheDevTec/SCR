package me.devtec.scr.commands.server_managment.rrs;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.server_managment.rrs.RRSTask.TaskType;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Reload implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			RRSTask.startTask(TaskType.RELOAD, Loader.config.getInt("rrs.reload.baseTime"));
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.argument(null, (s, structure, args) -> { // cmd {time}
					int time = (int) StringUtils.timeFromString(args[0]);
					if (time > 0)
						RRSTask.startTask(TaskType.RELOAD, time);
					else
						RRSTask.cancelTask(true);
				}).parent() // cmd
				.argument("cancel", (s, structure, args) -> {
					RRSTask.cancelTask(true);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "reload";
	}

}
