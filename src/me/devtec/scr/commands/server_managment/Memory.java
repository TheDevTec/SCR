package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.StringUtils.FormatType;

public class Memory implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(Loader.config.getBoolean("options.ram-percentage"))
				msg(s, "memory", Placeholders.c()
						.add("ram_free", ""+getFreeMemory(true))
						.add("ram_max", ""+getMaxMemory())
						.add("ram_used", ""+getUsedMemory(true))
						);
			else
				msg(s, "memory", Placeholders.c()
						.add("ram_free", ""+getFreeMemory(false))
						.add("ram_max", ""+getMaxMemory())
						.add("ram_used", ""+getUsedMemory(false))
						);
			
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "memory";
	}
	
	public static double getFreeMemory(boolean inPercentage) {
		if (!inPercentage)
			return StringUtils.getDouble(
					StringUtils.formatDouble(FormatType.BASIC, getMaxMemory() - getRawUsedMemory(false))); 
		return StringUtils.getDouble(
				StringUtils.formatDouble(FormatType.BASIC,(getMaxMemory() - getRawUsedMemory(false)) / getMaxMemory() * 100.0D));
		} 
	public static double getMaxMemory() {
		return Runtime.getRuntime().maxMemory() / 1048576.0D;
		}
	public static double getUsedMemory(boolean inPercentage) {
		if (!inPercentage)
			return StringUtils.getDouble(StringUtils.formatDouble(FormatType.BASIC, getRawUsedMemory(false))); 
		return StringUtils.getDouble(StringUtils.formatDouble(FormatType.BASIC, getRawUsedMemory(false) / getMaxMemory() * 100.0D));
	}
	public static double getRawUsedMemory(boolean inPercentage) {
		if (!inPercentage)
			return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576.0D; 
		return getRawUsedMemory(false) / getMaxMemory() * 100.0D;
	}
	public static double getRawFreeMemory(boolean inPercentage) {
		if (!inPercentage)
			return getMaxMemory() - getRawUsedMemory(false); 
		return (getMaxMemory() - getRawUsedMemory(false)) / getMaxMemory() * 100.0D;
	}
}
