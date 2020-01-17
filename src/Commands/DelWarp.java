package Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class DelWarp implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(API.hasPerm(s, "ServerControl.DelWarp")) {
			if(args.length==0) {
				Loader.Help(s, "/DelWarp <warp>", "Warp.DelWarp");
				return true;
			}
			if(args[0]!=null) {
				if(Loader.config.getString("Warps."+args[0])!=null) {
		Loader.config.set("Warps."+args[0], null);
		Configs.config.save();Loader.msg(Loader.s("Warp.Deleted")
					.replace("%warp%", args[0])
					.replace("%player%", s.getName())
					.replace("%prefix%", Loader.s("Prefix"))
					.replace("%playername%", ((Player)s).getDisplayName()), s);
			return true;
			}else {
				Loader.msg(Loader.s("Warp.NotExists")
						.replace("%player%", s.getName())
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%playername%", ((Player)s).getDisplayName())
						.replace("%warp%", args[0]), s);
				return true;
			}}}return true;}
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(cmd.getName().equalsIgnoreCase("DelWarp") && args.length==1) {
        	if(s.hasPermission("ServerControl.DelWarp")) {
        		Set<String> homes = Loader.config.getConfigurationSection("Warps").getKeys(false);
        		if(!homes.isEmpty() && homes != null)
        		c.addAll(StringUtil.copyPartialMatches(args[0], homes, new ArrayList<>()));
            }
    }
        return c;
}}