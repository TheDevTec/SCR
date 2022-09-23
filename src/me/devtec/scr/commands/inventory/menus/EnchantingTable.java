package me.devtec.scr.commands.inventory.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.listeners.additional.EnchantingTableListener;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class EnchantingTable implements ScrCommand {

	public List<UUID> openedInv = new ArrayList<>();

	private Location loc;

	@Override
	public void init(List<String> cmds) {
		// TODO packets

		Loader.plugin.getLogger().info("[Enchanting] Using PrepareItemEnchant listener");
		Loader.registerListener(new EnchantingTableListener(this));

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			msgSec(s, "self");
			openedInv.add(((Player) s).getUniqueId());
			((Player) s).openEnchanting(loc, true);
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
		}).argument("-s", (s, structure, args) -> {
			if (!(s instanceof Player)) {
				help(s, "usage");
				return;
			}
			openedInv.add(((Player) s).getUniqueId());
			((Player) s).openEnchanting(loc, true);
		}).parent().selector(Selector.PLAYER, (s, structure, args) -> {
			Player p = Bukkit.getPlayer(args[0]);
			openedInv.add(p.getUniqueId());
			p.openEnchanting(loc, true);
			msgSec(s, "other.sender", Placeholders.c().addPlayer("target", p));
			msgSec(p, "other.target", Placeholders.c().addPlayer("player", s));
		}).permission(permission("other")).argument("-s", (s, structure, args) -> {
			Player p = Bukkit.getPlayer(args[0]);
			openedInv.add(p.getUniqueId());
			p.openEnchanting(loc, true);
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "enchanting";
	}

}
