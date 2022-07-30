package me.devtec.scr.commands.inventory.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class EnderChest implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			((Player) s).openInventory(((Player) s).getEnderChest());
			msgSec(s, "self");
		}).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).argument("-s", (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			((Player) s).openInventory(((Player) s).getEnderChest());
		}).parent().selector(Selector.PLAYER, (s, structure, args) -> {
			Player p = Bukkit.getPlayer(args[0]);
			p.openInventory(p.getEnderChest());
			msgSec(s, "other.sender", Placeholders.c().add("target", p.getName()));
			msgSec(p, "other.target", Placeholders.c().add("target", s.getName()));
		}).permission(permission("other")).argument("-s", (s, structure, args) -> {
			Player p = Bukkit.getPlayer(args[0]);
			p.openInventory(p.getEnderChest());
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "enderchest";
	}

}
