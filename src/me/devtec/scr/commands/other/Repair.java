package me.devtec.scr.commands.other;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.theapi.TheAPI;

/**
 * @author StraikerinaCZ
 * 25.12. 2021
 **/
public class Repair extends CommandHolder {
	
	public Repair(String command) {
		super(command, -1);
	}
	
	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		switch(args.length) {
		case 0:
			if(s instanceof Player) {
				apply(s, (Player)s, true, silent);
			}else
				help(s, 0);
			break;
		case 1:
			if(s instanceof Player) {
				if(args[0].equalsIgnoreCase("hand")||args[0].equalsIgnoreCase("hold")) {
					apply(s, (Player)s, true, silent);
				}else
					apply(s, (Player)s, false, silent);
			}else
				help(s, 1);
			break;
		default:
			if(loop) {
				for(Player player : TheAPI.getOnlinePlayers())
					apply(s, player, args[0].equalsIgnoreCase("hand")||args[0].equalsIgnoreCase("hold"), silent);
			}else {
				Player player;
				if((player=requireOnline(s, args[0]))==null)break;
				apply(s, player, args[0].equalsIgnoreCase("hand")||args[0].equalsIgnoreCase("hold"), silent);
			}
			break;
		}
	}
	
	public void apply(CommandSender s, Player player, boolean hand, boolean silent) {
		if(hand) {
			ItemStack item = player.getItemInHand();
			if(!isRepairable(item.getType())) {
				if(!silent) {
					if(s == player)
						Loader.send(s, "Repair.notRepairable.self", PlaceholderBuilder.make(s, "sender").add("value", item.getType().name()));
					else
						Loader.send(s, "Repair.notRepairable.other", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", item.getType().name()));
				}
				return;
			}
			item.setDurability((short)0);
			player.setItemInHand(item);
			return;
		}
		int repaired = 0;
		for(ItemStack item : player.getInventory().getContents()) {
			if(!isRepairable(item.getType()))continue;
			item.setDurability((short)0);
			++repaired;
		}
		if(!silent) {
			if(player==s) {
				Loader.send(s, "Repair.repaired.all.self", PlaceholderBuilder.make(s, "sender").add("value", repaired+""));
			}else {
				Loader.send(s, "Repair.repaired.all.other.sender", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", repaired+""));
				Loader.send(player, "Repair.repaired.all.other.target", PlaceholderBuilder.make(s, "sender").player(player, "target").add("value", repaired+""));
			}
		}
	}

	public boolean isRepairable(Material material) {
		return Loader.isArmor(material)||Loader.isTool(material);
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return args.length>=2?placeholder_TWO:null;
	}
	
}