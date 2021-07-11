package me.devtec.servercontrolreloaded.utils;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Reader;

public class BungeeListener implements PluginMessageListener {

	@Override
	@SuppressWarnings("unchecked")
	public void onPluginMessageReceived(String ccc, Player arg1, byte[] arg2) {
		if(!ccc.equals("scr:community"))return;
		ByteArrayDataInput d = ByteStreams.newDataInput(arg2);
		String action = d.readUTF();
		if(action.equals("portal")) {
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
			Collection<String> cmds = (Collection<String>)Reader.read(msg.replaceFirst(msg.split("\\[")[0], ""));
			new Tasker() {
				public void run() {
					Player p = TheAPI.getPlayerOrNull(name);
					if(p==null)return;
					for(String s : cmds)
						TheAPI.sudoConsole(TabList.replace(s, p, true));
				}
			}.runLaterSync(StringUtils.getInt(wait));
			return;
		}
		if(action.equals("helpop")) {
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
			return;
		}
	}

}
