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
import me.devtec.shared.utility.StringUtils;

public class BalanceTop implements ScrCommand {
	static ComparableObject<UUID, Double>[] ranking;

	@Override
	public void init(int cd, List<String> cmds) {
		if (Loader.economy == null)
			return;

		new Tasker() {
			@Override
			public void run() {
				refleshBaltop();
			}
		}.runRepeating(0, 20 * 60); //TODO - too quick?

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			listBaltop(s, 0);
		}).permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "invalidPage", Placeholders.c().replace("page", args[0]));
		}).selector(Selector.INTEGER, (s, structure, args) -> { // cmd [integer]
			if (StringUtils.getInt(args[0]) - 1 < 0) {
				msgSec(s, "invalidPage", Placeholders.c().replace("page", args[0]));
				return;
			}
			listBaltop(s, StringUtils.getInt(args[0]) - 1);
		}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	public void refleshBaltop() {
		Map<UUID, Double> map = new HashMap<>();
		File file = new File("plugins/TheAPI/Users");
		if (file.exists())
			for (String userFile : file.list()) {
				UUID id = UUID.fromString(userFile.split("/")[userFile.split("/").length - 1].replace(".yml", ""));
				map.put(id, ((net.milkbowl.vault.economy.Economy) Loader.economy).getBalance(API.offlineCache().lookupNameById(id)));
			}
		ranking = SortingAPI.sortByValueArray(map, true);
	}

	public void listBaltop(CommandSender s, int page) {
		int rank = page + 1;
		msgSec(s, "header", Placeholders.c().replace("page", page+1).replace("pages", ranking.length) );
		for (int i = page * 10; i < (page + 1) * 10 && i < ranking.length; ++i) {
			ComparableObject<UUID, Double> comp = ranking[i];
			//msgSec(s, "balancetop.format", rank++, API.offlineCache().lookupNameById(comp.getKey()), ((net.milkbowl.vault.economy.Economy) Loader.economy).format(comp.getValue()));
			msgSec(s, "format", Placeholders.c().replace("position", rank++)
					.replace("playername", API.offlineCache().lookupNameById(comp.getKey()))
					.replace("money", ((net.milkbowl.vault.economy.Economy) Loader.economy).format(comp.getValue())) );
			}
		msgSec(s, "footer", Placeholders.c().replace("page", page+1).replace("pages", ranking.length) );
	}

	@Override
	public String configSection() {
		return "balancetop";
	}

}
