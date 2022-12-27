package me.devtec.scr.commands.economy;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Economy implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		if (Loader.economy == null)
			return;
		// /economy [add/remove/set] [player] [amount]

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			help(s, "usage");
		}).argument("add", (s, structure, args) -> { // cmd add
			help(s, "add");
		}).fallback((s, structure, args) -> {
			offlinePlayer(s, args[1]);
		}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd add [entity_selector]
			help(s, "add");
		}).argument(null, (s, structure, args) -> { // cmd add [entity_selector] [any string]
			double money = ScrEconomy.balanceFromString(args[2]);
			String bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).format(money);
			for (Player p : playerSelectors(s, args[1])) {
				((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(p, money);
				msgSec(s, "add.sender", Placeholders.c().replace("money", bal).addPlayer("target", p));
				msgSec(p, "add.target", Placeholders.c().replace("money", bal).addPlayer("player", s));
			}
		}).argument("-s", (s, structure, args) -> { // cmd add [entity_selector] [any string] -s
			double money = ScrEconomy.balanceFromString(args[2]);
			for (Player p : playerSelectors(s, args[1]))
				((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(p, money);
		}).parent(3).argument(null, 1, (s, structure, args) -> { // cmd add [playerName]
			help(s, "add");
		}).argument(null, (s, structure, args) -> { // cmd add [playerName] [any string]
			double money = ScrEconomy.balanceFromString(args[2]);
			String bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).format(money);
			if (API.offlineCache().lookupQuery(args[1]) == null) {
				offlinePlayer(s, args[1]);
				return;
			}
			((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(args[1], money);
			msgSec(s, "add.sender", Placeholders.c().replace("money", bal).addOffline("target", args[1]));
		}).argument("-s", (s, structure, args) -> { // cmd add [entity_selector] [any string] -s
			double money = ScrEconomy.balanceFromString(args[2]);
			if (API.offlineCache().lookupQuery(args[1]) == null) {
				offlinePlayer(s, args[1]);
				return;
			}
			((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(args[1], money);
		}).first() // cmd
				.argument("remove", (s, structure, args) -> { // cmd remove
					help(s, "remove");
				}).fallback((s, structure, args) -> {
					offlinePlayer(s, args[1]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd remove [entity_selector]
					help(s, "remove");
				}).argument(null, (s, structure, args) -> { // cmd remove [entity_selector] [any string]
					double money = ScrEconomy.balanceFromString(args[2]);
					String bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).format(money);
					for (Player p : playerSelectors(s, args[1])) {
						((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(p, money);
						msgSec(s, "remove.sender", Placeholders.c().replace("money", bal).addPlayer("target", p));
						msgSec(p, "remove.target", Placeholders.c().replace("money", bal).addPlayer("player", s));
					}
				}).argument("-s", (s, structure, args) -> { // cmd remove [entity_selector] [any string] -s
					double money = ScrEconomy.balanceFromString(args[2]);
					for (Player p : playerSelectors(s, args[1]))
						((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(p, money);
				}).parent(3).argument(null, 1, (s, structure, args) -> { // cmd remove [playerName]
					help(s, "remove");
				}).argument(null, (s, structure, args) -> { // cmd remove [playerName] [any string]
					double money = ScrEconomy.balanceFromString(args[2]);
					String bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).format(money);
					if (API.offlineCache().lookupQuery(args[1]) == null) {
						offlinePlayer(s, args[1]);
						return;
					}
					((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(args[1], money);
					msgSec(s, "remove.sender", Placeholders.c().replace("money", bal).addOffline("target", args[1]));
				}).argument("-s", (s, structure, args) -> { // cmd remove [entity_selector] [any string] -s
					double money = ScrEconomy.balanceFromString(args[2]);
					if (API.offlineCache().lookupQuery(args[1]) == null) {
						offlinePlayer(s, args[1]);
						return;
					}
					((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(args[1], money);
				}).first().argument("set", (s, structure, args) -> { // cmd set
					help(s, "set");
				}).fallback((s, structure, args) -> {
					offlinePlayer(s, args[1]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd set [entity_selector]
					help(s, "set");
				}).argument(null, (s, structure, args) -> { // cmd set [entity_selector] [any string]
					double money = ScrEconomy.balanceFromString(args[2]);
					String bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).format(money);
					for (Player p : playerSelectors(s, args[1])) {
						((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(p, ((net.milkbowl.vault.economy.Economy) Loader.economy).getBalance(p));
						((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(p, money);
						msgSec(s, "set.sender", Placeholders.c().replace("money", bal).addPlayer("target", p));
						msgSec(p, "set.target", Placeholders.c().replace("money", bal).addPlayer("player", s));
					}
				}).argument("-s", (s, structure, args) -> { // cmd set [entity_selector] [any string] -s
					double money = ScrEconomy.balanceFromString(args[2]);
					for (Player p : playerSelectors(s, args[1])) {
						((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(p, ((net.milkbowl.vault.economy.Economy) Loader.economy).getBalance(p));
						((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(p, money);
					}
				}).parent(3).argument(null, 1, (s, structure, args) -> { // cmd set [playerName]
					help(s, "set");
				}).argument(null, (s, structure, args) -> { // cmd set [playerName] [any string]
					double money = ScrEconomy.balanceFromString(args[2]);
					String bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).format(money);
					if (API.offlineCache().lookupQuery(args[1]) == null) {
						offlinePlayer(s, args[1]);
						return;
					}
					((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(args[1], ((net.milkbowl.vault.economy.Economy) Loader.economy).getBalance(args[1]));
					((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(args[1], money);
					msgSec(s, "set.sender", Placeholders.c().replace("money", bal).addOffline("target", args[1]));
				}).argument("-s", (s, structure, args) -> { // cmd set [entity_selector] [any string] -s
					double money = ScrEconomy.balanceFromString(args[2]);
					if (API.offlineCache().lookupQuery(args[1]) == null) {
						offlinePlayer(s, args[1]);
						return;
					}
					((net.milkbowl.vault.economy.Economy) Loader.economy).withdrawPlayer(args[1], ((net.milkbowl.vault.economy.Economy) Loader.economy).getBalance(args[1]));
					((net.milkbowl.vault.economy.Economy) Loader.economy).depositPlayer(args[1], money);
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "economy";
	}

}
