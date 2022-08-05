package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class TPS implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			msg(s, "", Placeholders.c()
					.add("tps", ""+getServerTPS())
					.add("tps_1", ""+getServerTPS(TPSType.ONE_MINUTE))
					.add("tps_5", ""+getServerTPS(TPSType.FIVE_MINUTES))
					.add("tps_15", ""+getServerTPS(TPSType.FIFTEEN_MINUTES))
					);
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "tps";
	}

	 public static double getServerTPS() {
	    return getServerTPS(TPSType.ONE_MINUTE);
	  }
	  
	  public enum TPSType {
	    ONE_MINUTE, FIVE_MINUTES, FIFTEEN_MINUTES;
	  }
	  
	  public static double getServerTPS(TPSType type) {
		  try {
			  double tps = BukkitLoader.getNmsProvider().getServerTPS()[(type == TPSType.ONE_MINUTE) ? 0 : ((type == TPSType.FIVE_MINUTES) ? 1 : 2)];
			  if (tps > 20.0D)
				  tps = 20.0D; 
			  return StringUtils.getDouble(String.format("%2.02f", new Object[] { Double.valueOf(tps) }));
		  } catch (Exception e) {
			  return 20.0D;
	    } 
	  }

}
