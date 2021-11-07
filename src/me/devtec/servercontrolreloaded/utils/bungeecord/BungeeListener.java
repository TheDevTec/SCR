package me.devtec.servercontrolreloaded.utils.bungeecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.servercontrolreloaded.commands.message.PrivateMessageManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.skins.SkinData;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Json;

public class BungeeListener implements PluginMessageListener {
	
	static List<String> players = new ArrayList<>();
	
	@Override
	@SuppressWarnings("unchecked")
	public void onPluginMessageReceived(String channel, Player arg1, byte[] data) {
		if(!channel.equals("scr:community"))return;
		
		ByteArrayDataInput d = ByteStreams.newDataInput(data);
		String action = d.readUTF();
		switch(action) {
		case "players": {
			players.clear();
			int count = d.readInt();
			for(int i = 0; i != count; ++i)
				players.add(d.readUTF());
			return;
		}
		case "portal": {
			String name = d.readUTF();
			String server = d.readUTF();
			String wait = d.readUTF();
			String msg = "";
			msg+=server;
			while(true) {
				try {
					msg+=d.readUTF();
				}catch(Exception e) {break;}
			}
			try {
			Collection<String> cmds = (Collection<String>) Json.reader().simpleRead(msg.replaceFirst(msg.split("\\[")[0], ""));
			new Tasker() {
				public void run() {
					Player p = TheAPI.getPlayerOrNull(name);
					if(p==null)return;
					for(String s : cmds)
						TheAPI.sudoConsole(TabList.replace(s, p, true));
				}
			}.runLaterSync(StringUtils.getInt(wait));
			}catch(Exception err) {}
			return;
		}
		case "helpop": {
			String name = d.readUTF();
			String dname = d.readUTF();
			String msg = "";
			while(true) {
				try {
					msg+=d.readUTF();
				}catch(Exception e) {break;}
			}
			TheAPI.broadcast(Loader.config.getString("Format.HelpOp").replace("%sender%", name)
					.replace("%sendername%", dname).replace("%message%", msg), Loader.cmds.exists("Message.Helpop.SubPermissions.Receive")?Loader.cmds.getString("Message.Helpop.SubPermissions.Receive"):"SCR.Command.Helpop.Receive");
		}
		case "pm": {
			String target = d.readUTF();
			Player player = TheAPI.getPlayer(target);
			if(target==null)return;
			String sender = d.readUTF();
			String senderName = d.readUTF();
			String msg = d.readUTF();
			PrivateMessageManager.sendMessage(sender, senderName, player, msg);
		}
		case "text": {
			String target = d.readUTF();
			Player player = TheAPI.getPlayer(target);
			if(target==null)return;
			while(true) {
				try {
					TheAPI.msg(d.readUTF(), player);
				}catch(Exception e) {break;}
			}
		}
		}
	}
	
	public static void sendText(Player sender, String target, String...strings) {
		if(!Loader.hasBungee)return;
		
		ByteArrayDataOutput data = ByteStreams.newDataOutput();
		data.writeUTF("text");
		data.writeUTF(target);
		for(String s : strings)
		data.writeUTF(s);
		sender.sendPluginMessage(Loader.getInstance, "scr:community", data.toByteArray());
	}
	
	public static void sendPm(Player sender, String target, String text) {
		if(!Loader.hasBungee)return;
		
		ByteArrayDataOutput data = ByteStreams.newDataOutput();
		data.writeUTF("pm");
		data.writeUTF(sender.getName());
		data.writeUTF(sender.getDisplayName()==null?sender.getName():sender.getDisplayName());
		data.writeUTF(target);
		data.writeUTF(text);
		sender.sendPluginMessage(Loader.getInstance, "scr:community", data.toByteArray());
	}

	public static boolean isOnline(String name) {
		for(String s : players)if(s.equalsIgnoreCase(name))return true;
		return false;
	}
	
	public static List<String> getOnline(){
		return players;
	}
	
	public static void requestSkinUpdate(Player player, SkinData data) {
		ByteArrayDataOutput d = ByteStreams.newDataOutput();
		d.writeUTF("skin");
		d.writeUTF(player.getName());
		d.writeUTF(data.value);
		d.writeUTF(data.signature);
		player.sendPluginMessage(Loader.getInstance, "scr:community", d.toByteArray());
	}
	
	public static void requestOnlinePlayers() {
		if(!Loader.hasBungee)return;
		
		ByteArrayDataOutput data = ByteStreams.newDataOutput();
		data.writeUTF("players");
		Player s = TheAPI.getPlayer(0);
		if(s!=null)
			s.sendPluginMessage(Loader.getInstance, "scr:community", data.toByteArray());
	}
}
