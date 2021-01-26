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
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.scheduler.Tasker;

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
				return "Unknown";
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		} catch (Exception e) {
			return "Unknown";
		}
	}
	
	
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
			Loader.sendMessages(s, "WhoIs.Loading", Placeholder.c().add("%player%", a[0]));
			new Tasker() {
				public void run() {
					String ip = PunishmentAPI.getIP(a[0]);
					if (ip == null)
						ip = "Unknown";
					String country = getCountry(ip);
					boolean d = false;
					String afk = "false";
					String seen = null;
					if (TheAPI.getPlayerOrNull(a[0]) != null) {
						seen = API.getSeen(a[0], SeenType.Online);
						if (API.isAFK(TheAPI.getPlayerOrNull(a[0])))
							afk = "true";
						d=true;
					}else seen = API.getSeen(a[0], SeenType.Offline);
					SPlayer c = API.getSPlayer(a[0]);
					Loader.sendMessages(s, "WhoIs."+(d?"Online":"Offline"), Placeholder.c().add("%player%", c.getName()).add("%playername%", c.getName()).add("%customname%", c.getCustomName()).add("%ip%", ip).add("%country%", country)
							.add("%afk%", afk).add("%seen%", seen).add("%fly%", c.hasFlyEnabled()+"").add("%god%", c.hasGodEnabled()+"").add("%tempfly%", c.hasTempFlyEnabled()+"")
							.add("%op%", Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(a[0]))+"").add("%uuid%", Bukkit.getOfflinePlayer(a[0]).getUniqueId().toString())
							.add("%vanish%", c.hasVanish()+"").add("%firstjoin%", TheAPI.getUser(a[0]).getString("FirstJoin")+"").add("%group%", Staff.getGroup(a[0]))
							.add("%money%", EconomyAPI.format(EconomyAPI.getBalance(a[0])))
							.add("%health%", c.getPlayer()!=null ? c.getPlayer().getHealthScale()+"":"Unknown").add("%food%", c.getPlayer()!=null ? c.getPlayer().getFoodLevel()+"":"Unknown")
							.add("%xp%", c.getPlayer()!=null ? c.getPlayer().getTotalExperience()+"":"Unknown").add("%level%", c.getPlayer()!=null ? c.getPlayer().getLevel()+"":"Unknown")
							.add("%banned%", PunishmentAPI.getBanList(c.getName()).isBanned()+"")
							.add("%arrested%", PunishmentAPI.getBanList(c.getName()).isJailed()+"")
							.add("%ip-arrested%", PunishmentAPI.getBanList(c.getName()).isIPJailed()+"")
							.add("%ip-muted%", PunishmentAPI.getBanList(c.getName()).isIPMuted()+"")
							.add("%muted%", PunishmentAPI.getBanList(c.getName()).isMuted()+"")
							.add("%ip-banned%", PunishmentAPI.getBanList(c.getName()).isIPBanned()+""));
				}}.runTask();
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
