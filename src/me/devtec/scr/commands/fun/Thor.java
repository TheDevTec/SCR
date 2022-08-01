package me.devtec.scr.commands.fun;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Thor implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if(s instanceof Player) {
				Player p = (Player)s;
				Location loc = getLookingBlock(p, 30).getLocation();
				loc.getWorld().strikeLightningEffect(loc);
				msgSec(s, "loc", Placeholders.c().replace("world", loc.getWorld().getName()).replace("x", loc.getBlockX())
						.replace("y", loc.getBlockY()).replace("z", loc.getBlockZ()) );
			}else {
				help(s, "usage");
			}
		}).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).permission(permission("cmd"))
			.argument("-s", (s, structure, args) -> { // cmd -s
				if(s instanceof Player) {
					Player p = (Player)s;
					Location loc = getLookingBlock(p, 30).getLocation();
					loc.getWorld().strikeLightningEffect(loc);
				}else {
					help(s, "usage");
				}
			})
			.parent() // cmd
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
				for(Player p : playerSelectors(s, args[0])) {
					p.getWorld().strikeLightning(p.getLocation());
					msgSec(s, "player", Placeholders.c().replace("target", p.getName()));
				}
			}).permission(permission("other"))
				.argument("-s", (s, structure, args) -> { // cmd [entity_selector] -s
					for(Player p : playerSelectors(s, args[0])) {
						p.getWorld().strikeLightning(p.getLocation());
					}
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}
	
	public static Block getLookingBlock(Player player, int range) {
		BlockIterator iter = new BlockIterator(player, range);
		Block lastBlock = iter.next();
		while (iter.hasNext()) {
			lastBlock = iter.next();
			if (lastBlock.getType() == Material.AIR || lastBlock.getType().name().equals("CAVE_AIR") || lastBlock.isLiquid() || !lastBlock.getType().isSolid())
				continue;
			break;
		}
		return lastBlock;
	}

	@Override
	public String configSection() {
		return "thor";
	}
	
}
