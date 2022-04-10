package me.devtec.scr.commands.inventory.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Grindstone implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		if(Ref.isOlderThan(14))return;
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if(!(s instanceof Player)) {
				help(s, 0);
				return;
			}
			((Player)s).openInventory(Bukkit.createInventory((Player)s, InventoryType.GRINDSTONE));
			msgConfig(s, configSection()+".self");
		}).permission("scr."+configSection()).fallback((s, structure, args) -> {
			msg(s, "offlinePlayer", args[0]);
			}).argument("-s", (s, structure, args) -> {
				if(!(s instanceof Player)) {
					help(s, 0);
					return;
				}
				((Player)s).openInventory(Bukkit.createInventory((Player)s, InventoryType.GRINDSTONE));
			})
			.parent()
			.selector(Selector.PLAYER, (s, structure, args) -> {
				Player p = Bukkit.getPlayer(args[0]);
				p.openInventory(Bukkit.createInventory(p, InventoryType.GRINDSTONE));
				msgConfig(s, configSection()+".other.sender", p.getName());
				msgConfig(p, configSection()+".other.target", p.getName());
				}).argument("-s", (s, structure, args) -> {
					Player p = Bukkit.getPlayer(args[0]);
					p.openInventory(Bukkit.createInventory(p, InventoryType.GRINDSTONE));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "grindstone";
	}

}
