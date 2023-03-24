package me.devtec.scr.commands.economy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.sorting.SortingAPI;
import me.devtec.shared.sorting.SortingAPI.ComparableObject;
import me.devtec.shared.utility.ParseUtils;

public class BalanceTop implements ScrCommand {
	private static ComparableObject<UUID, Double>[] ranking;

	@Override
	public void init(List<String> cmds) {
		if (Loader.economy == null)
			return;

		new Tasker() {
			@Override
			public void run() {
				refleshBaltop();
			}
		}.runRepeating(0, 20 * 60 * 5);

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			listBaltop(s, 0);
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "invalidPage", Placeholders.c().replace("page", args[0]));
		}).selector(Selector.INTEGER, (s, structure, args) -> { // cmd [integer]
			if (ParseUtils.getInt(args[0]) - 1 < 0) {
				msgSec(s, "invalidPage", Placeholders.c().replace("page", args[0]));
				return;
			}
			listBaltop(s, ParseUtils.getInt(args[0]) - 1);
		}).parent().argument("last", (s, structure, args) -> { // cmd last
			listBaltop(s, Math.max(0, ranking.length / 10));
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	public void refleshBaltop() {
		Map<UUID, Double> map = new HashMap<>();
		File file = new File("plugins/TheAPI/Users");
		if (file.exists() && file.isDirectory())
			for (String userFile : file.list()) {
				UUID id = UUID.fromString(userFile.substring(0, userFile.length() - 4));
				String playerName = API.offlineCache().lookupNameById(id);
				if (playerName != null) {
					double bal = ((net.milkbowl.vault.economy.Economy) Loader.economy).getBalance(playerName);
					if (bal > 0)
						map.put(id, bal);
				}
			}
		ranking = SortingAPI.sortByValueArray(map, true);
	}

	public void listBaltop(CommandSender s, int page) {
		awaitInitRanking();
		int totalPages = Math.max(0, ranking.length / 10);
		if (page > totalPages)
			page = totalPages;
		msgSec(s, "header", Placeholders.c().replace("page", page + 1).replace("pages", totalPages + 1));
		for (int i = page * 10; i < (page + 1) * 10 && i < ranking.length; ++i) {
			ComparableObject<UUID, Double> comp = ranking[i];
			msgSec(s, "format", Placeholders.c().replace("position", i + 1).replace("playername", API.offlineCache().lookupNameById(comp.getKey())).replace("money",
					((net.milkbowl.vault.economy.Economy) Loader.economy).format(comp.getValue())));
		}
		msgSec(s, "footer", Placeholders.c().replace("page", page + 1).replace("pages", totalPages + 1));
	}

	@Override
	public String configSection() {
		return "balancetop";
	}

	public static ComparableObject<UUID, Double>[] getRanking() {
		return ranking;
	}

	/**
	 * @apiNote Freeze current thread and wait for Ranking init if Vault economy is
	 *          found
	 */
	public static void awaitInitRanking() {
		if (Loader.economy == null)
			return;
		while (ranking == null)
			;
	}

}
