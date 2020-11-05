package me.DevTec.ServerControlReloaded.Commands.Info;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.API.SeenType;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.SPlayer;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.PunishmentAPI.PunishmentAPI;

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
				return "Uknown";
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		} catch (Exception e) {
			return "Uknown";
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] a) {
		if (Loader.has(s, "WhoIs", "Info")) {
			if (a.length == 0) {
				Loader.Help(s, "WhoIs", "Info");
				return true;
			}
			if (!TheAPI.existsUser(a[0])) {
				Loader.sendMessages(s, "Missing.Player.NotExist", Placeholder.c()
						.add("%player%", a[0]));
				return true;
			}
			String ip = PunishmentAPI.getIP(a[0]);
			if (ip == null)
				ip = "Uknown";
			String country = getCountry(ip);
			boolean d = false;
			String afk = "false";
			String seen = null;
			if (TheAPI.getPlayerOrNull(a[0]) != null && TheAPI.getPlayer(a[0]).getName().equals(a[0])) {
				seen = API.getSeen(a[0], SeenType.Online);
				if (API.isAFK(TheAPI.getPlayerOrNull(a[0])))
					afk = "true";
				d=true;
			}else seen = API.getSeen(a[0], SeenType.Offline);
			SPlayer c = API.getSPlayer(a[0]);
			Loader.sendMessages(s, "WhoIs."+(d?"Online":"Offline"), Placeholder.c().add("%ip%", ip).add("%country%", country)
					.add("%afk%", afk).add("%seen%", seen).add("%fly%", c.hasFlyEnabled()+"").add("%god%", c.hasGodEnabled()+"").add("%tempfly%", c.hasTempFlyEnabled()+"")
					.add("%op%", Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(a[0]))+"").add("%uuid%", Bukkit.getOfflinePlayer(a[0]).getUniqueId().toString())
					.add("%vanish%", c.hasVanish()+"").add("%firstjoin%", TheAPI.getUser(a[0]).getString("FirstJoin")+"").add("%group%", Staff.getGroup(a[0]))
					.add("%money%", EconomyAPI.format(EconomyAPI.getBalance(a[0])))
					.add("%health%", c.getPlayer()!=null ? c.getPlayer().getHealthScale()+"":"Uknown").add("%food%", c.getPlayer()!=null ? c.getPlayer().getFoodLevel()+"":"Uknown")
					.add("%xp%", c.getPlayer()!=null ? c.getPlayer().getTotalExperience()+"":"Uknown").add("%level%", c.getPlayer()!=null ? c.getPlayer().getLevel()+"":"Uknown"));
			/*boolean send = false;
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
			}*/
			return true;
		}
		Loader.noPerms(s, "WhoIs", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 1)
			return null;
		return new ArrayList<>();
	}

}
