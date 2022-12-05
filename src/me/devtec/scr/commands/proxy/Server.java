package me.devtec.scr.commands.proxy;

import java.util.List;

import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.structures.CommandStructure;

public class Server implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		if (Ref.getClass("org.spigotmc.SpigotConfig") != null)
			if (SpigotConfig.bungee)
				CommandStructure.create(Player.class, PLAYER_PERMS_CHECKER, (s, structure, args) -> { // cmd [server]
					help(s, "usage");
				}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")).argument(null, 1, (s, structure, args) -> { // cmd [server]
					msgSec(s, "connecting", Placeholders.c().addPlayer("player", s).add("server", args[0]));
					s.sendPluginMessage(Loader.plugin, "BungeeCord", sendToServer(args[0]));
				}).argument("-s", (s, structure, args) -> { // cmd [server] -s
					s.sendPluginMessage(Loader.plugin, "BungeeCord", sendToServer(args[0]));
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
		return "server";
	}

}
