package me.devtec.servercontrolreloaded.commands.info;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.API.SeenType;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.SPlayer;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.punishmentapi.PunishmentAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Reader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhoIs implements CommandExecutor, TabCompleter {

	private static HashMap<String, Object> empty = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCountry(String a) {
		try {
			URL url = new URL("http://ip-api.com/json/" + a.replace("_", "."));
			return (Map<String,Object>)Reader.read(StreamUtils.fromStream(url.openStream()));
		} catch (Exception e) {
			return empty;
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
					else ip=ip.replace("_", ".");
					Map<String, Object> country = getCountry(ip);
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
					Loader.sendMessages(s, "WhoIs."+(d?"Online":"Offline"), Placeholder.c().add("%player%", c.getName()).add("%playername%", c.getName()).add("%customname%", c.getCustomName()).add("%ip%", ip)
							.add("%country%", (String)country.getOrDefault("country", "Uknown")).add("%region%", (String)country.getOrDefault("regionName", "Uknown"))
							.add("%city%", (String)country.getOrDefault("city", "Uknown"))
							.add("%afk%", afk).add("%seen%", seen).add("%fly%", c.hasFlyEnabled()+"").add("%god%", c.hasGodEnabled()+"").add("%tempfly%", c.hasTempFlyEnabled()+"")
							.add("%op%", Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(a[0]))+"").add("%uuid%", Bukkit.getOfflinePlayer(a[0]).getUniqueId().toString())
							.add("%vanish%", API.hasVanish(c.getName())+"").add("%firstjoin%", c.getUser().getString("FirstJoin")+"").add("%group%", Staff.getGroup(a[0]))
							.add("%money%", EconomyAPI.format(EconomyAPI.getBalance(a[0])))
							.add("%health%", c.getPlayer()!=null ? c.getPlayer().getHealthScale()+"":"-1").add("%food%", c.getPlayer()!=null ? c.getPlayer().getFoodLevel()+"":"-1")
							.add("%xp%", c.getPlayer()!=null ? c.getPlayer().getTotalExperience()+"":"-1").add("%level%", c.getPlayer()!=null ? c.getPlayer().getLevel()+"":"-1")
							.add("%banned%", PunishmentAPI.getBanList(c.getName()).isBanned()+"")
							.add("%arrested%", PunishmentAPI.getBanList(c.getName()).isJailed()+"")
							.add("%ip-arrested%", PunishmentAPI.getBanList(c.getName()).isIPJailed()+"")
							.add("%ip-muted%", PunishmentAPI.getBanList(c.getName()).isIPMuted()+"")
							.add("%muted%", PunishmentAPI.getBanList(c.getName()).isMuted()+"")
							.add("%playtime%", d?StringUtils.timeToString(TabList.playtime(c.getPlayer())):"-1s")
							.add("%x%", d?StringUtils.fixedFormatDouble(c.getPlayer().getLocation().getX()):"-1")
							.add("%y%", d?StringUtils.fixedFormatDouble(c.getPlayer().getLocation().getY()):"-1")
							.add("%z%", d?StringUtils.fixedFormatDouble(c.getPlayer().getLocation().getZ()):"-1")
							.add("%yaw%", d?StringUtils.fixedFormatDouble(c.getPlayer().getLocation().getYaw()):"-1")
							.add("%pitch%", d?StringUtils.fixedFormatDouble(c.getPlayer().getLocation().getPitch()):"-1")
							.add("%world%", d?c.getPlayer().getWorld().getName():"Uknown")
							.add("%ip-banned%", PunishmentAPI.getBanList(c.getName()).isIPBanned()+""));
				}}.runTask();
			return true;
		}
		Loader.noPerms(s, "WhoIs", "Info");
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(args.length==1 && Loader.has(s, "WhoIs", "Info"))
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}

}
