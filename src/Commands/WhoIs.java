package Commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import ServerControl.API;
import ServerControl.API.SeenType;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PunishmentAPI.PlayerBanList;
import me.DevTec.TheAPI.PunishmentAPI.PlayerBanList.PunishmentType;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

public class WhoIs implements CommandExecutor, TabCompleter {

	public static String getCountry(String a) {
		try {
			InetSocketAddress ip = new InetSocketAddress(a, 0);
			URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
			BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder entirePage = new StringBuilder();
			String inputLine;
			while ((inputLine = stream.readLine()) != null)
				entirePage.append(inputLine);
			stream.close();
			if (!(entirePage.toString().contains("\"country\":\"")))
				return null;
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		} catch (Exception e) {
			return "&7Uknown.";
		}
	}

	private String getName(String s) {
		if (TheAPI.getPlayer(s) != null)
			return TheAPI.getPlayer(s).getDisplayName();
		return s;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] a) {
		if (API.hasPerm(s, "ServerControl.WhoIs")) {
			if (a.length == 0) {
				Loader.Help(s, "/WhoIs <player>", "WhoIs");
				return true;
			}
			if (!TheAPI.existsUser(a[0])) {
				TheAPI.msg(Loader.PlayerNotEx(a[0]), s);
				return true;
			}
			String ip = PunishmentAPI.getIP(a[0]);
			if (ip == null)
				ip = "&7Uknown";
			String what = "Offline";
			String afk = "No";
			String seen = API.getSeen(a[0], SeenType.Offline);
			if (TheAPI.getPlayer(a[0]) != null && TheAPI.getPlayer(a[0]).getName().equals(a[0])) {
				what = "Online";
				seen = API.getSeen(a[0], SeenType.Online);
				if (API.isAFK(TheAPI.getPlayer(a[0])))
					afk = "Yes";
			}
			String fly = "Disabled";
			String god = "Disabled";
			User f = TheAPI.getUser(a[0]);
			if (f.getBoolean("Fly"))
				fly = "Enabled";
			if (f.getBoolean("God"))
				god = "Enabled";
			String op = "No";
			for (OfflinePlayer w : Bukkit.getOperators()) {
				if (w.getName().equals(a[0]))
					op = "Yes";
			}
			TheAPI.msg("&8----- &a" + getName(a[0]) + " &8-----", s);
			TheAPI.msg("&6Nickname: &a" + a[0], s);
			TheAPI.msg("&6" + what + ": &a" + seen, s);
			TheAPI.msg("&6Fly: &a" + fly, s);
			TheAPI.msg("&6God: &a" + god, s);
			TheAPI.msg("&6AFK: &a" + afk, s);
			TheAPI.msg("&6OP: &a" + op, s);
			TheAPI.msg("&6IP: &a" + ip.replaceFirst("/", ""), s);
			TheAPI.msg("&6Country: &a" + getCountry(ip), s);
			TheAPI.msg("&6Punishment: &a", s);
			PlayerBanList d = PunishmentAPI.getBanList(a[0]);
			if (d.isMuted()) {
				TheAPI.msg("  &6Muted", s);
			}
			if (d.isTempMuted()) {
				long tmtime = d.getExpire(PunishmentType.TEMPMUTE);
				TheAPI.msg("  &6TempMuted: &a" + StringUtils.setTimeToString(tmtime), s);
			}
			if (d.isJailed()) {
				TheAPI.msg("  &6Jailed", s);
			}
			if (d.isTempJailed()) {
				long tjtime = d.getExpire(PunishmentType.TEMPJAIL);
				TheAPI.msg("  &6Temp-arrested: &a" + StringUtils.setTimeToString(tjtime), s);
			}
			if (d.isBanned()) {
				TheAPI.msg("  &6Banned", s);
			}
			if (d.isTempBanned()) {
				long tbtime = d.getExpire(PunishmentType.TEMPBAN);
				TheAPI.msg("  &6TempBanned: &a" + StringUtils.setTimeToString(tbtime), s);
			}

			if (d.isIPBanned()) {
				TheAPI.msg("  &6IPBanned", s);
			}
			if (d.isTempIPBanned()) {
				long tbiptime = d.getExpire(PunishmentType.TEMPBANIP);
				TheAPI.msg("  &6TempIPBanned: &a" + StringUtils.setTimeToString(tbiptime), s);
			}
			TheAPI.msg("&8---------------", s);
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 1)
			return null;
		return new ArrayList<>();
	}

}
