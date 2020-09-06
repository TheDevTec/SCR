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
				return "&7Uknown.";
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		} catch (Exception e) {
			return "&7Uknown.";
		}
	}
	
	@SuppressWarnings("deprecation")
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
			String afk = "false";
			String seen = API.getSeen(a[0], SeenType.Offline);
			if (TheAPI.getPlayer(a[0]) != null && TheAPI.getPlayer(a[0]).getName().equals(a[0])) {
				what = "Online";
				seen = API.getSeen(a[0], SeenType.Online);
				if (API.isAFK(TheAPI.getPlayer(a[0])))
					afk = "true";
			}
			String fly = "false";
			String god = "false";
			User f = TheAPI.getUser(a[0]);
			if (f.getBoolean("Fly"))
				fly = "true";
			if (f.getBoolean("God"))
				god = "true";
			String op = "false";
			for (OfflinePlayer w : Bukkit.getOperators()) {
				if (w.getName().equals(a[0]))
					op = "true";
			}
			TheAPI.msg("&eName: &a" + a[0]+(TheAPI.getPlayerOrNull(a[0])!=null?"&7 ("+TheAPI.getPlayerOrNull(a[0]).getDisplayName()+"&r&7)":""), s);
			TheAPI.msg("&e" + what + ": &a" + seen, s);
			TheAPI.msg("&eFly: &a" + fly, s);
			TheAPI.msg("&eGod: &a" + god, s);
			TheAPI.msg("&eAFK: &a" + afk, s);
			TheAPI.msg("&eOP: &a" + op, s);
			TheAPI.msg("&eIP: &a" + ip.replaceFirst("/", ""), s);
			TheAPI.msg("&eCountry: &a" + getCountry(ip), s);
			TheAPI.msg("&eUUID: &a" + Bukkit.getOfflinePlayer(a[0]).getUniqueId(), s);
			boolean send = false;
			PlayerBanList d = PunishmentAPI.getBanList(a[0]);
			if (d.isMuted()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				TheAPI.msg("  &6Muted", s);
			}
			if (d.isTempMuted()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				long tmtime = d.getExpire(PunishmentType.TEMPMUTE);
				TheAPI.msg("  &6TempMuted: &a" + StringUtils.setTimeToString(tmtime), s);
			}
			if (d.isJailed()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				TheAPI.msg("  &6Jailed", s);
			}
			if (d.isTempJailed()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				long tjtime = d.getExpire(PunishmentType.TEMPJAIL);
				TheAPI.msg("  &6Temp-arrested: &a" + StringUtils.setTimeToString(tjtime), s);
			}
			if (d.isBanned()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				TheAPI.msg("  &6Banned", s);
			}
			if (d.isTempBanned()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				long tbtime = d.getExpire(PunishmentType.TEMPBAN);
				TheAPI.msg("  &6TempBanned: &a" + StringUtils.setTimeToString(tbtime), s);
			}

			if (d.isIPBanned()) {
				if(!send) {
					send=true;
					TheAPI.msg("&ePunishment:", s);
				}
				TheAPI.msg("  &6IPBanned", s);
			}
			if (d.isTempIPBanned()) {
				long tbiptime = d.getExpire(PunishmentType.TEMPBANIP);
				TheAPI.msg("  &6TempIPBanned: &a" + StringUtils.setTimeToString(tbiptime), s);
			}
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
