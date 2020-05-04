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
import me.Straiker123.TheAPI;
import me.Straiker123.User;

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
        if(!(entirePage.toString().contains("\"country\":\"")))
            return null;
        return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
    	}catch(Exception e) {
    		return "&7Uknown.";
    	}
    }
    
    private String getName(String s) {
    	if(TheAPI.getPlayer(s)!=null)return TheAPI.getPlayer(s).getDisplayName();
    	return s;
    }
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] a) {
		if(API.hasPerm(s, "ServerControl.WhoIs")) {
		if(a.length==0){
			Loader.Help(s, "/WhoIs <player>", "WhoIs");
			return true;
		}
		if(!TheAPI.existsUser(a[0])) {
			Loader.msg(Loader.PlayerNotEx(a[0]),s);
			return true;
		}
		String ip = TheAPI.getPunishmentAPI().getIP(a[0]);
		if(ip==null)ip="&7Uknown";
		String what = "Offline";
		String afk = "No";
		String seen = API.getSeen(a[0], SeenType.Offline);
		if(TheAPI.getPlayer(a[0])!=null && TheAPI.getPlayer(a[0]).getName().equals(a[0])) {
			what="Online";
			seen = API.getSeen(a[0], SeenType.Online);
			if(API.isAFK(TheAPI.getPlayer(a[0])))afk="Yes";
		}
		String fly = "Disabled";
		String god = "Disabled";
		User f= TheAPI.getUser(a[0]);
		if(f.getBoolean("Fly"))fly="Enabled";
		if(f.getBoolean("God"))god="Enabled";
		String op = "No";
		for(OfflinePlayer w : Bukkit.getOperators()) {
			if(w.getName().equals(a[0]))op="Yes";
		}
		Loader.msg("&8----- &a"+getName(a[0])+" &8-----",s);
		Loader.msg("&6Nickname: &a"+a[0],s);
		Loader.msg("&6"+what+": &a"+seen,s);
		Loader.msg("&6Fly: &a"+fly,s);
		Loader.msg("&6God: &a"+god,s);
		Loader.msg("&6AFK: &a"+afk,s);
		Loader.msg("&6OP: &a"+op,s);
		Loader.msg("&6IP: &a"+ip.replaceFirst("/", ""),s);
		Loader.msg("&6Country: &a"+getCountry(ip),s);
		Loader.msg("&6Punishment: &a",s);
		if(TheAPI.getPunishmentAPI().hasMute(a[0])) {
			Loader.msg("  &6Muted", s);
		}
		if(TheAPI.getPunishmentAPI().hasTempMute(a[0])) {
			
			long tmtime = TheAPI.getPunishmentAPI().getTempMuteExpireTime(a[0]);
			Loader.msg("  &6TempMuted: &a"+TheAPI.getStringUtils().setTimeToString(tmtime), s);
		}
		if(API.getBanSystemAPI().hasJail(a[0])) {
			Loader.msg("  &6Jailed", s);
		}
		if(API.getBanSystemAPI().hasTempJail(a[0])) {
			long tjtime = API.getBanSystemAPI().getTempJailTime(a[0]);
			Loader.msg("  &6Temp-arrested: &a"+TheAPI.getStringUtils().setTimeToString(tjtime), s);
		}
		if(TheAPI.getPunishmentAPI().hasBan(a[0])) {
			Loader.msg("  &6Banned", s);
		}
		if(TheAPI.getPunishmentAPI().hasTempBan(a[0])) {
			long tbtime = TheAPI.getPunishmentAPI().getTempBanExpireTime(a[0]);
			Loader.msg("  &6TempBanned: &a"+TheAPI.getStringUtils().setTimeToString(tbtime), s);
		}

		if(TheAPI.getPunishmentAPI().hasBanIP(a[0])) {
			Loader.msg("  &6IPBanned", s);
		}
		if(TheAPI.getPunishmentAPI().hasTempBanIP(a[0])) {
			long tbiptime = TheAPI.getPunishmentAPI().getTempBanIPExpireTime(a[0]);
			Loader.msg("  &6TempIPBanned: &a"+TheAPI.getStringUtils().setTimeToString(tbiptime), s);
		}
		Loader.msg("&8---------------",s);
		return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==1)
		return null;
		return new ArrayList<>();
	}

}
