package me.devtec.scr.commands.proxy;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Send implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		if (Ref.getClass("org.spigotmc.SpigotConfig") != null)
			if (SpigotConfig.bungee)
				CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd [entity_selector] [server]
					help(s, "usage");
				}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).fallback((s, structure, args) -> {
					offlinePlayer(s, args[0]);
				}).selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { // cmd [entity_selector]
					help(s, "usage");
				}).argument(null, 1, (s, structure, args) -> { // cmd [entity_selector] [server]
					for (Player player : playerSelectors(s, args[0])) {
						msgSec(s, "other.sender", Placeholders.c().addPlayer("player", s).addPlayer("target", player).add("server", args[1]));
						msgSec(player, "other.receiver", Placeholders.c().addPlayer("target", player).addPlayer("player", s).add("server", args[1]));
						player.sendPluginMessage(Loader.plugin, "BungeeCord", sendToServer(args[1]));
					}
				}).argument("-s", (s, structure, args) -> { // cmd [entity_selector] -s
					for (Player player : playerSelectors(s, args[0]))
						player.sendPluginMessage(Loader.plugin, "BungeeCord", sendToServer(args[1]));
				}).build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	private byte[] sendToServer(String serverName) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF("Connect");
		output.writeUTF(serverName);
		return output.toByteArray();
	}

	@Override
	public String configSection() {
		return "send";
	}

}
