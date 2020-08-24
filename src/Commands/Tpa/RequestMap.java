package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

/**
 * 
 * @author waskerSK
 * 
 * Updated by StraikerinaCZ
 */
public class RequestMap {
	public static void addRequest(String target, String sender, Type tp) {
		User s = TheAPI.getUser(sender);
		s.set("Tp." + target + ".Type", tp.toString());
		s.setAndSave("Tp." + target + ".Time", System.currentTimeMillis() / 1000);
		if (setting.tp_onreqloc)
			s.setAndSave("Tp." + target + ".Location",
					StringUtils.getLocationAsString(TheAPI.getPlayer(target).getLocation()));
	}

	public static void removeRequest(String target, String sender) {
		TheAPI.getUser(target).setAndSave("Tp." + sender, null);
	}

	public static String getRequest(String p) {
		User s = TheAPI.getUser(p);
		if (s.exist("Tp")) {
			List<String> f = new ArrayList<String>();
			for (String d : s.getKeys("Tp"))
				if (TheAPI.getPlayer(d) != null)
					f.add(d);
			if (!f.isEmpty())
				return f.get(0);
			return null;
		}
		return null;
	}

	public static Location getLocation(String p, String t) {
		User s = TheAPI.getUser(p);
		if (s.exist("Tp." + t + ".Location"))
			return StringUtils.getLocationFromString(s.getString("Tp." + t + ".Location"));
		return null;
	}

	public static enum Type {
		TPA, TPAHERE
	}

	public static Type getTeleportType(String target, String sender) {
		return Type.valueOf(TheAPI.getUser(target).getString("Tp." + sender + ".Type"));
	}

	public static boolean containsRequest(String target, String sender) {
		if (TheAPI.getUser(target).getLong("Tp." + sender + ".Time") - System.currentTimeMillis() / 1000 + StringUtils.getTimeFromString(Loader.config.getString("Options.Teleport.RequestTime")) > 0)
			return true;
		else {
			removeRequest(target, sender);
			return false;
		}
	}

}