package me.devtec.scr.commands.economy;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Economy implements ScrCommand {
	
	@Override
	public void init(int cd, List<String> cmds) {
		if(Loader.economy == null)return;
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).permission("scr."+configSection()).fallback((s, structure, args) -> {
			offlinePlayer(s, args[0]);
			})
			.argument("add", (s, structure, args) -> { // cmd add
				help(s, "add");
				})
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd add [entity_selector]
					help(s, "add");
					})
					.argument(null, (s, structure, args) -> { // cmd add [entity_selector] [any string]
						double money = ScrEconomy.balanceFromString(args[2]);
						String bal = ((net.milkbowl.vault.economy.Economy)Loader.economy).format(money);
						for(Player p : playerSelectors(s, args[1])) {
							((net.milkbowl.vault.economy.Economy)Loader.economy).depositPlayer(p, money);
							/*msgSec(s, "add.sender", p.getName(), bal);
							msgSec(p, "add.target", p.getName(), bal);*/
						}
						})
						.argument("-s", (s, structure, args) -> { // cmd add [entity_selector] [any string] -s
							double money = ScrEconomy.balanceFromString(args[2]);
							for(Player p : playerSelectors(s, args[1])) {
								((net.milkbowl.vault.economy.Economy)Loader.economy).depositPlayer(p, money);
							}
						})
						.parent() //any string
					.parent() //entity selector
				.parent() //add
			.parent() //cmd
			.argument("remove", (s, structure, args) -> { // cmd remove
				help(s, "remove");
				})
				.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd remove [entity_selector]
					help(s, "remove");
					})
					.argument(null, (s, structure, args) -> { // cmd remove [entity_selector] [any string]
						double money = ScrEconomy.balanceFromString(args[2]);
						String bal = ((net.milkbowl.vault.economy.Economy)Loader.economy).format(money);
						for(Player p : playerSelectors(s, args[1])) {
							((net.milkbowl.vault.economy.Economy)Loader.economy).withdrawPlayer(p, money);
							/*msgSec(s, "remove.sender", p.getName(), bal);
							msgSec(p, "remove.target", p.getName(), bal);*/
						}
						})
						.argument("-s", (s, structure, args) -> { // cmd remove [entity_selector] [any string] -s
							double money = ScrEconomy.balanceFromString(args[2]);
							for(Player p : playerSelectors(s, args[1])) {
								((net.milkbowl.vault.economy.Economy)Loader.economy).withdrawPlayer(p, money);
							}
						})
						.parent() //any string
					.parent() //entity selector
				.parent() //remove
				.argument("set", (s, structure, args) -> { // cmd set
					help(s, "set");
					})
					.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd set [entity_selector]
						help(s, "set");
						})
						.argument(null, (s, structure, args) -> { // cmd set [entity_selector] [any string]
							double money = ScrEconomy.balanceFromString(args[2]);
							String bal = ((net.milkbowl.vault.economy.Economy)Loader.economy).format(money);
							for(Player p : playerSelectors(s, args[1])) {
								((net.milkbowl.vault.economy.Economy)Loader.economy).withdrawPlayer(p, ((net.milkbowl.vault.economy.Economy)Loader.economy).getBalance(p));
								((net.milkbowl.vault.economy.Economy)Loader.economy).depositPlayer(p, money);
								/*msgSec(s, "set.sender", p.getName(), bal);
								msgSec(p, "set.target", p.getName(), bal);*/
							}
							})
							.argument("-s", (s, structure, args) -> { // cmd set [entity_selector] [any string] -s
								double money = ScrEconomy.balanceFromString(args[2]);
								for(Player p : playerSelectors(s, args[1])) {
									((net.milkbowl.vault.economy.Economy)Loader.economy).withdrawPlayer(p, ((net.milkbowl.vault.economy.Economy)Loader.economy).getBalance(p));
									((net.milkbowl.vault.economy.Economy)Loader.economy).depositPlayer(p, money);
								}
							}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "economy";
	}
	
}
