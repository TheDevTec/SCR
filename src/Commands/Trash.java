package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import com.google.common.collect.Lists;

import ServerControl.API;
import ServerControl.Loader;
import Utils.MultiWorldsGUI;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.GUIAPI.GUI;
import me.DevTec.TheAPI.GUIAPI.ItemGUI;

public class Trash implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (API.hasPerm(s, "ServerControl.Trash")) {
			if (s instanceof Player) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Inventory.OpeningTrash"), s);
				openInv((Player) s);
				return true;
			}
			TheAPI.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}
	
	private static ItemGUI clear;
	static {
		clear=new ItemGUI(MultiWorldsGUI.createItem("&6Clear", Utils.XMaterial.LAVA_BUCKET, Lists.newArrayList())) {
			public void onClick(Player s, GUI g, ClickType c) {
				for (int i = 0; i < 45; ++i)
				g.remove(i);
			}
		};
	}

	public static void openInv(Player p) {
		GUI s = new GUI(Loader.s("TrashTitle"), 54, p);
		s.setInsertable(true);
		MultiWorldsGUI.smallInv(s);
		s.setItem(49, clear);
	}

}