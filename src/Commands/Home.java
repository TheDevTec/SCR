package Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;

public class Home implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if(s instanceof Player) {
			Player p = (Player)s;
			if(API.hasPerm(s, "ServerControl.Home")) {
			if(args.length==0) {
				if(Loader.me.getString("Players."+p.getName()+".Homes.home")!=null) {
				World w = Bukkit.getWorld(Loader.me.getString("Players."+p.getName()+".Homes.home.World"));
				double x = Loader.me.getDouble("Players."+p.getName()+".Homes.home.X");
				double y = Loader.me.getDouble("Players."+p.getName()+".Homes.home.Y");
				double z = Loader.me.getDouble("Players."+p.getName()+".Homes.home.Z");
				float pitch = Loader.me.getInt("Players."+p.getName()+".Homes.home.Pitch");
				float yaw = Loader.me.getInt("Players."+p.getName()+".Homes.home.Yaw");
				Location loc = new Location(w,x,y,z,yaw,pitch);
				API.setBack(p);
				if(w!=null)
				p.teleport(loc);
				Loader.msg(Loader.s("Prefix")+Loader.s("Homes.Teleporting")
                .replace("%player%", p.getName())
                .replace("%playername%", p.getDisplayName())
				.replace("%home%", "home"), s);
				return true;
			}
				float x_head;
				float z_head;
				World world = null;
				Location locs = null;
				if(Loader.config.getString("Spawn")!=null) {
					 x_head = Loader.config.getInt("Spawn.X_Pos_Head");
					 z_head = Loader.config.getInt("Spawn.Z_Pos_Head");
					 world = Bukkit.getWorld(Loader.config.getString("Spawn.World"));
					 locs = new Location(world, Loader.config.getDouble("Spawn.X"), Loader.config.getDouble("Spawn.Y") ,Loader.config.getDouble("Spawn.Z"), x_head, z_head);
						}
						if(Loader.config.getString("Spawn")==null) {
							world = Bukkit.getWorlds().get(0);
							 locs = Bukkit.getWorlds().get(0).getSpawnLocation();
						}
						if(world!=null) {
							API.setBack(p);
						p.teleport(locs);
						
						Loader.msg(Loader.s("Prefix")+Loader.s("Spawn.NoHomesTeleportedToSpawn")
				.replace("%world%", world.getName())
				.replace("%player%", s.getName())
				.replace("%playername%", ((Player)s).getDisplayName()),s);
				return true;
			}}
			if(args.length==1) {
		if(Loader.me.getString("Players."+p.getName()+".Homes."+args[0])!=null) {
			World w = Bukkit.getWorld(Loader.me.getString("Players."+p.getName()+".Homes."+args[0]+".World"));
			double x = Loader.me.getDouble("Players."+p.getName()+".Homes."+args[0]+".X");
			double y = Loader.me.getDouble("Players."+p.getName()+".Homes."+args[0]+".Y");
			double z = Loader.me.getDouble("Players."+p.getName()+".Homes."+args[0]+".Z");
			float pitch = Loader.me.getInt("Players."+p.getName()+".Homes."+args[0]+".Pitch");
			float yaw = Loader.me.getInt("Players."+p.getName()+".Homes."+args[0]+".Yaw");
			API.setBack(p);
			Location loc = new Location(w,x,y,z,yaw,pitch);
			if(w!=null)
			p.teleport(loc);
			Loader.msg(Loader.s("Prefix")+Loader.s("Homes.Teleporting")
            .replace("%player%", p.getName())
            .replace("%playername%", p.getDisplayName())
			.replace("%home%", args[0]), s);
			return true;
			}
		Loader.msg(Loader.s("Prefix")+Loader.s("Homes.NotExists")
        .replace("%player%", p.getName())
        .replace("%playername%", p.getDisplayName())
		.replace("%home%", args[0]), s);
		return true;
			}
			}
			return true;
			}
		Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"), s);
		return true;
	}
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(s instanceof Player) {
    	if(args.length==1) {
        	if(s.hasPermission("ServerControl.Home")) {
        		try {
        		Set<String> homes = Loader.me.getConfigurationSection("Players."+s.getName()+".Homes").getKeys(false);
        		if(!homes.isEmpty() && homes != null)
        		c.addAll(StringUtil.copyPartialMatches(args[0], homes, new ArrayList<>()));
        		}catch(Exception e) {
        			
        		}
            }}}
        return c;
}}