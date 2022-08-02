package me.devtec.scr.commands.gamemode;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class GamemodeC implements ScrCommand{

	@Override
	public void init(List<String> cmds) {
		// /gmc {Player} {time}
		// /gmc {time}
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			if(s instanceof Player) {
				Player p = (Player)s;
				
				p.setGameMode(GameMode.CREATIVE);
				msg(s, "gamemodes.target."+p.getGameMode().name().toLowerCase());
			}else {
				help(s, "usage");
			}
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
		.selector(Selector.PLAYER, (s, structure, args) ->{
			//TODO
		})
		;
		
	}

	@Override
	public String configSection() {
		return "gamemodeCreative";
	}

}