package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.XMaterial;
import me.DevTec.TheAPI.APIs.ItemCreatorAPI;
import me.DevTec.TheAPI.GUIAPI.GUI;
import me.DevTec.TheAPI.GUIAPI.ItemGUI;

public class Trash implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Trash", "Other")) {
			if (s instanceof Player)
				Trash.s.open((Player) s);
			return true;
		}
		Loader.noPerms(s, "Trash", "Other");
		return true;
	}
	
	private static ItemGUI clear;
	private static GUI s;
	static {
		clear=new ItemGUI(ItemCreatorAPI.create(XMaterial.LAVA_BUCKET.parseMaterial(), 1, "&6Clear")) {
			public void onClick(Player s, GUI g, ClickType c) {
				for (int i = 0; i < 45; ++i)
				g.remove(i);
			}
		};
		s = new GUI(Loader.getTranslation("Trash").toString(), 54);
		s.setInsertable(true);
		MultiWorldsGUI.smallInv(s);
		s.setItem(49, clear);
	}
}