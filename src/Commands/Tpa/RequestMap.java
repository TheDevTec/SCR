package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

/**
 * 
 * @author waskerSK
 * 
 * 9.10. 2020
 * Updated by StraikerinaCZ (DevTec)
 */
public class RequestMap {
	//Types: 0 = TPA, 1 = TPAHERE
	public static void add(Player sender, String target, int type) {
		if(has(sender.getName(), target)) {
			Loader.sendMessages(sender, "TpSystem.HaveRequest", Placeholder.c().replace("%player%", target).replace("%playername%", target)
					.replace("%type%", TheAPI.getUser(sender.getUniqueId()).getInt("teleport." + target + ".b")==0?"TPA":"TPAHERE"));
			return;
		}
		if(isBlocking(target, sender.getName())) {
			Loader.sendMessages(sender, "TpSystem.Block.IsBlocked.Request", Placeholder.c().replace("%player%", target).replace("%playername%", target));
			return;
		}
		User s = TheAPI.getUser(sender.getUniqueId());
		s.set("teleport." + target + ".a", type);
		s.setAndSave("teleport." + target + ".b", System.currentTimeMillis()/1000);
		if(type==1) //Tpahere
		if (setting.tp_onreqloc)
			s.setAndSave("teleport." + target + ".c", new Position(sender.getLocation()).toString()); //teleport target to request position

		User d = TheAPI.getUser(target);
		d.setAndSave("tpcancel."+sender.getName(), type);
	}
	
	public static boolean isBlocking(String sender, String target) {
		return TheAPI.getUser(sender).getBoolean("TpBlock." + target) || TheAPI.getUser(sender).getBoolean("TpBlock-Global");
	}
	
	public static void cancel(Player sender) {
		List<String> aw = new ArrayList<>(TheAPI.getUser(sender.getUniqueId()).getKeys("tpcancel"));
		if(aw.isEmpty()) {
			Loader.sendMessages(sender, "TpSystem.NoRequest");
		}else {
			String first = aw.get(0);
			remove(sender.getName(), first);
			Loader.sendMessages(sender, "TpSystem.Cancel.Sender", Placeholder.c().add("%player%", first).add("%playername%", TheAPI.getPlayerOrNull(first)!=null?TheAPI.getPlayerOrNull(first).getDisplayName():first));
			if(TheAPI.getPlayerOrNull(first)!=null)
				Loader.sendMessages(TheAPI.getPlayerOrNull(first), "TpSystem.Cancel.Receiver",
						Placeholder.c().add("%player%", sender.getName()).add("%playername%", sender.getDisplayName()));
		}
	}

	public static void remove(String sender, String target) {
		TheAPI.getUser(sender).getData().remove("teleport." + target);
		TheAPI.getUser(sender).save();
		TheAPI.getUser(target).getData().remove("tpcancel." + sender);
		TheAPI.getUser(target).save();
	}

	public static Player getFirst(String sender) {
		List<String> acceptable = new ArrayList<>();
		for(String s : TheAPI.getUser(sender).getKeys("teleport"))
			if (has(sender, s))
				acceptable.add(s);
		return acceptable.isEmpty()?null:TheAPI.getPlayerOrNull(acceptable.get(0));
	}

	public static boolean has(String sender, String target) {
		if(!TheAPI.getUser(sender).exists("teleport."+target))return false;
		if (TheAPI.getPlayerOrNull(target) != null && TheAPI.getUser(sender).getLong("teleport." + sender + ".b") - System.currentTimeMillis()/1000 + StringUtils.getTimeFromString(Loader.config.getString("Options.Teleport.RequestTime")) > 0)
			return true;
		else {
			remove(sender, target);
			return false;
		}
	}
	
	public static Position getPos(String sender, String target) {
		return Position.fromString(TheAPI.getUser(sender).getString("teleport."+target+".c"));
	}
	
	public static boolean accept(Player sender) {
		Player target = getFirst(sender.getName());
		if(target==null) {
			Loader.sendMessages(sender, "TpSystem.NoRequest");
			return false;
		}
		//Tpa
		if(TheAPI.getUser(sender).getInt("teleport."+target.getName()+".a")==0) {
			API.setBack(target);
		if (setting.tp_safe)
			API.safeTeleport(target,sender.getLocation());
		else
			target.teleport(sender);
		Loader.sendMessages(sender, "TpSystem.Tpa.Accept.Sender", Placeholder.c()
				.add("%player%", target.getName()).add("%playername%", target.getDisplayName()));
		Loader.sendMessages(target, "TpSystem.Tpa.Accept.Receiver", Placeholder.c()
				.add("%player%", sender.getName()).add("%playername%", sender.getDisplayName()));
		remove(sender.getName(), target.getName());
		return true;
		}
		//Tpahere
		API.setBack(sender);
		if (setting.tp_safe)
			if (setting.tp_onreqloc && getPos(sender.getName(), target.getName()) != null)
				API.safeTeleport(sender,getPos(sender.getName(), target.getName()).toLocation());
			else
				API.safeTeleport(sender,target.getLocation());
		else
			if (setting.tp_onreqloc && getPos(sender.getName(), target.getName()) != null)
				sender.teleport(getPos(sender.getName(), target.getName()).toLocation());
			else
				sender.teleport(target);
		Loader.sendMessages(sender, "TpSystem.Tpahere.Accept.Sender", Placeholder.c()
				.add("%player%", target.getName()).add("%playername%", target.getDisplayName()));
		Loader.sendMessages(target, "TpSystem.Tpahere.Accept.Receiver", Placeholder.c()
				.add("%player%", sender.getName()).add("%playername%", sender.getDisplayName()));
		remove(sender.getName(), target.getName());
		return true;
	}
	
	public static boolean deny(Player sender) {
		Player target = getFirst(sender.getName());
		if(target==null) {
			Loader.sendMessages(sender, "TpSystem.NoRequest");
			return false;
		}
		int type = TheAPI.getUser(sender).getInt("teleport."+target.getName()+".a");
		remove(sender.getName(), target.getName());
		Loader.sendMessages(sender, "TpSystem."+(type==0?"Tpa":"Tpahere")+".Reject.Sender", Placeholder.c()
				.add("%player%", target.getName()).add("%playername%", target.getDisplayName()));
		Loader.sendMessages(target, "TpSystem."+(type==0?"Tpa":"Tpahere")+".Reject.Receiver", Placeholder.c()
				.add("%player%", sender.getName()).add("%playername%", sender.getDisplayName()));
		return true;
	}
}