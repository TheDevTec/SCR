package me.devtec.scr.commands.economy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.API;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.sorting.RankingAPI;
import me.devtec.shared.sorting.SortingAPI.ComparableObject;
import me.devtec.shared.utility.StringUtils;

public class BalanceTop implements ScrCommand {
	static RankingAPI<UUID, Double> ranking;
	
	@Override
	public void init(List<String> cmds) {
		if(Loader.economy == null)return;
		
		ranking = new RankingAPI<>();
		
		new Tasker() {
			public void run() {
				refleshBaltop();
			}
		}.runRepeating(0, 20*60);
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			listBaltop(s, 0);
		}).permission("scr."+configSection()).fallback((s, structure, args) -> {
			msgConfig(s, configSection()+".invalidPage", args[0]);
			})
			.selector(Selector.INTEGER, (s, structure, args) -> { // cmd [integer]
				if(StringUtils.getInt(args[0])-1 < 0) {
					msgConfig(s, configSection()+".invalidPage", args[0]);
					return;
				}
				listBaltop(s, StringUtils.getInt(args[0])-1);
			}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	public void refleshBaltop() {
		ranking.clear();
		Map<UUID, Double> map = new HashMap<>();
		File file = new File("plugins/TheAPI/Users");
		if(file.exists())
		for(String userFile : file.list()) {
			UUID id = UUID.fromString(userFile.split("/")[userFile.split("/").length-1].replace(".yml", ""));
			map.put(id, ((net.milkbowl.vault.economy.Economy)Loader.economy).getBalance(API.offlineCache().lookupNameById(id)));
		}
		ranking.load(map);
	}

	public void listBaltop(CommandSender s, int page) {
		int rank = page+1;
		msgConfig(s, "balancetop.header", page);
		for(ComparableObject<UUID, Double> comp : ranking.getTop(page*10, (page+1)*10)) {
			msgConfig(s, "balancetop.format", rank++, API.offlineCache().lookupNameById(comp.getKey()), ((net.milkbowl.vault.economy.Economy)Loader.economy).format(comp.getValue()));
		}
		msgConfig(s, "balancetop.footer", page);
	}

	@Override
	public String configSection() {
		return "balancetop";
	}
	
}
