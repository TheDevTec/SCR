package Commands;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.XMaterial;
import me.Straiker123.GUICreatorAPI;
import me.Straiker123.GUICreatorAPI.Options;
import me.Straiker123.ItemCreatorAPI;
import me.Straiker123.TheAPI;

public class Trash implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Trash")) {
			if(s instanceof Player) {
				Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningTrash"), s);
				openInv((Player)s);
				return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"),s);
			return true;
		}
		return true;
	}
	public static void openInv(Player p) {
		ItemCreatorAPI a =TheAPI.getItemCreatorAPI(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial());
		a.setDisplayName(" ");
		
		ItemCreatorAPI b =TheAPI.getItemCreatorAPI(Material.BUCKET);
		b.setDisplayName("&6Clear");
		GUICreatorAPI s = TheAPI.getGUICreatorAPI(p);
		s.setSize(54);
		s.setTitle(Loader.s("TrashTitle"));
		HashMap<Options, Object> set = new HashMap<Options, Object>();
		set.put(Options.CANT_BE_TAKEN, true);
		

		HashMap<Options, Object> clear = new HashMap<Options, Object>();
		clear.put(Options.CANT_BE_TAKEN, true);
		clear.put(Options.RUNNABLE, new Runnable() {
			@Override
			public void run() {
				Trash.openInv(p);
			}
		});
		s.setItem(45, a.create(),set);
		s.setItem(46, a.create(),set);
		s.setItem(47, a.create(),set);
		s.setItem(48, a.create(),set);
		s.setItem(49, b.create(),clear);
		s.setItem(50, a.create(),set);
		s.setItem(51, a.create(),set);
		s.setItem(52, a.create(),set);
		s.setItem(53, a.create(),set);
		s.open();
	}

}