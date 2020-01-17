package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;

public class Repair implements CommandExecutor, TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(s instanceof Player) {
			if(API.hasPerm(s, "ServerControl.Repair")){
				Player p = (Player)s;
				if(args.length==0) {
				Material hand = p.getItemInHand().getType();
				if(hand != Material.AIR) {
					if(p.getInventory().getItemInHand().getDurability()!=0)
					p.getItemInHand().setDurability((short) 0);
					Loader.msg(Loader.s("Prefix")+Loader.s("Repair.Repaired"), s);
					return true;
					}
				
				Loader.msg(Loader.s("Prefix")+Loader.s("Repair.HandIsEmpty"), s);
				return true;
				}
				if(args[0].equalsIgnoreCase("all")){
					ItemStack[] items = p.getInventory().getContents();
					for(ItemStack t : items) {
						if(t!=null)
						if(t.getDurability()!=0)
						t.setDurability((short)0);
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("Repair.RepairedAll"), s);
					return true;
				}
				if(args[0].equalsIgnoreCase("hand")){
					Material hand = p.getItemInHand().getType();
					if(hand != Material.AIR) {
						if(p.getInventory().getItemInHand().getDurability()!=0)
						p.getItemInHand().setDurability((short) 0);
						Loader.msg(Loader.s("Prefix")+Loader.s("Repair.Repaired"), s);
						return true;
						}
					
					Loader.msg(Loader.s("Prefix")+Loader.s("Repair.HandIsEmpty"), s);
					return true;
				}
				if(!args[0].equalsIgnoreCase("hand")||!args[0].equalsIgnoreCase("all")) {
					Material hand = p.getItemInHand().getType();
					if(hand != Material.AIR) {
						if(p.getInventory().getItemInHand().getDurability()!=0)
						p.getItemInHand().setDurability((short) 0);
						Loader.msg(Loader.s("Prefix")+Loader.s("Repair.Repaired"), s);
						return true;
						}

					
					Loader.msg(Loader.s("Prefix")+Loader.s("Repair.HandIsEmpty"), s);
					return true;
				}
			}
			return true;
		}
		Loader.msg(Loader.s("ConsoleErrorMessage"), s);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<String>();
        List <String> s = Arrays.asList("Hand", "All");
		if(args.length==1)
		if(API.hasPerm(arg0, "ServerControl.Repair"))
			c.addAll(StringUtil.copyPartialMatches(args[0], s, new ArrayList<>()));
		return c;
	}

}
