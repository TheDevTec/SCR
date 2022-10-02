package me.devtec.scr.commands.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class Give implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.fallback((s, structure, args) -> { // /give [player]
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // give [player]
					help(s, "usage");
				}).permission("other").fallback((s, structure, args) -> { // /give [player] [material]
					msg(s, "missing.material", Placeholders.c().replace("material", args[1]));
				}).selector(Selector.MATERIAL, (s, structure, args) -> { // give [player] [material]
					ItemStack item = XMaterial.matchXMaterial(args[1].toUpperCase()).get().parseItem();

					for (Player player : playerSelectors(s, args[0])) {
						player.getInventory().addItem(item);
						msgSec(s, "other.sender", Placeholders.c().addPlayer("target", player).add("item", args[1].toUpperCase()).add("amount", item.getAmount()));
						msgSec(player, "other.target", Placeholders.c().addPlayer("sender", s).add("item", args[1].toUpperCase()).add("amount", item.getAmount()));
					}
				}).fallback((s, structure, args) -> { // /give [player] [material] [amount]
					msg(s, "missing.number", Placeholders.c().replace("number", args[2]));
				}).selector(Selector.INTEGER, (s, structure, args) -> { // give [player] [material] [amount]
					ItemStack item = XMaterial.matchXMaterial(args[1].toUpperCase()).get().parseItem();
					item.setAmount(StringUtils.getInt(args[2]));

					for (Player player : playerSelectors(s, args[0])) {
						player.getInventory().addItem(item);
						msgSec(s, "other.sender", Placeholders.c().addPlayer("target", player).add("item", args[1].toUpperCase()).add("amount", item.getAmount()));
						msgSec(player, "other.target", Placeholders.c().addPlayer("sender", s).add("item", args[1].toUpperCase()).add("amount", item.getAmount()));
					}
				}).argument(null, -1, (s, structure, args) -> { // give [player] [material] [amount] [nbt]
					ItemStack item = XMaterial.matchXMaterial(args[1].toUpperCase()).get().parseItem();
					String nbt = StringUtils.join(args, " ", 3);
					item = BukkitLoader.getNmsProvider().setNBT(item, nbt);
					item.setAmount(StringUtils.getInt(args[2]));

					for (Player player : playerSelectors(s, args[0])) {
						player.getInventory().addItem(item);
						msgSec(s, "other.sender-nbt", Placeholders.c().addPlayer("target", player).add("item", args[1].toUpperCase()).add("amount", item.getAmount()).add("nbt", nbt));
						msgSec(player, "other.target-nbt", Placeholders.c().addPlayer("sender", s).add("item", args[1].toUpperCase()).add("amount", item.getAmount()).add("nbt", nbt));
					}
				}, (sender, structure, args) -> Arrays.asList("<nbt>")).first() // give
				.fallback((s, structure, args) -> { // /give [material]
					msg(s, "missing.material", Placeholders.c().replace("material", args[0]));
				}).priority(1).selector(Selector.MATERIAL, (s, structure, args) -> { // give [material]
					if (!(s instanceof Player)) {
						help(s, "usage");
						return;
					}
					ItemStack item = XMaterial.matchXMaterial(args[0].toUpperCase()).get().parseItem();

					((Player) s).getInventory().addItem(item);
					msgSec(s, "self", Placeholders.c().add("item", args[0].toUpperCase()).add("amount", item.getAmount()));
				}).fallback((s, structure, args) -> { // /give [material] [amount]
					msg(s, "missing.number", Placeholders.c().replace("number", args[1]));
				}).selector(Selector.INTEGER, (s, structure, args) -> { // give [material] [amount]
					ItemStack item = XMaterial.matchXMaterial(args[0].toUpperCase()).get().parseItem();
					item.setAmount(StringUtils.getInt(args[1]));

					((Player) s).getInventory().addItem(item);
					msgSec(s, "self", Placeholders.c().add("item", args[0].toUpperCase()).add("amount", item.getAmount()));
				}).argument(null, -1, (s, structure, args) -> { // give [material] [amount] [nbt]
					ItemStack item = XMaterial.matchXMaterial(args[0].toUpperCase()).get().parseItem();
					String nbt = StringUtils.join(args, " ", 2);
					item = BukkitLoader.getNmsProvider().setNBT(item, nbt);
					item.setAmount(StringUtils.getInt(args[1]));

					((Player) s).getInventory().addItem(item);
					msgSec(s, "self-nbt", Placeholders.c().add("item", args[0].toUpperCase()).add("amount", item.getAmount()).add("nbt", nbt));
				}, (sender, structure, args) -> Arrays.asList("<nbt>")).build().register(cmds.remove(0), cmds.toArray(new String[0]));

	}

	@Override
	public String configSection() {
		return "give";
	}

}
