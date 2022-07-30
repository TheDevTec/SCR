package me.devtec.scr.commands.teleport.home;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class DeleteHome implements ScrCommand {

	@Override
	public void init(int cd, List<String> cmds) {
		cooldownMap.put(CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).permission(permission("cmd")).fallback((s, structure, args) -> {
			msgSec(s, "not_exists", Placeholders.c().add("home", args[0]));
		}).callableArgument((s, structure, args) -> new ArrayList<>(HomeManager.homesOf(s.getUniqueId())), (s, structure, args) -> { // cmd [home]
			HomeHolder home = HomeManager.find(s.getUniqueId(), args[0]);
			HomeManager.delete(s.getUniqueId(), home.name());
			msgSec(s, "deleted", Placeholders.c().add("home", home.name()));
		}).build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure(), new CooldownHolder(this, cd));
	}

	@Override
	public String configSection() {
		return "delhome";
	}

}
