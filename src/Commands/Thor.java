package Commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Thor implements CommandExecutor {

	public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		
		if(API.hasPerm(s, "ServerControl.Thor")) {
			if(args.length==0) {
				Player p2 = (Player) s;
				Block b = getTargetBlock(p2, 100);
				b.getWorld().strikeLightning(b.getLocation());
				Loader.msg(Loader.s("Prefix")+Loader.s("ThorOnBlock"), s);
				return true;
			}
			if(args.length==1) {
				if(args[0].equalsIgnoreCase("help")) {
					Loader.Help(s, "/Thor ", "ThorOnBlock");
					Loader.Help(s, "/Thor <player>", "Thor");
				}
				Player p = TheAPI.getPlayer(args[0]);
				if(p!=null) {
					p.getWorld().strikeLightning(p.getLocation());
					Loader.msg(Loader.s("Prefix")+Loader.s("Thor").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;
				}
				Loader.msg(Loader.PlayerNotOnline(args[0]),s);
				return true;
			}
			return true;
		}
		return true;
	}

}
