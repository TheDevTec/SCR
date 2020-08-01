package Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import ServerControl.Loader;
import me.DevTec.ItemCreatorAPI;
import me.DevTec.GUI.GUICreatorAPI;
import me.DevTec.GUI.ItemGUI;

public class gui {
	
	public static enum Type {
		n, Chat, Cmd
	}

	public static void openGUI(Player p, Type type) {
		GUICreatorAPI a = new GUICreatorAPI("&eSCR Manager" + (type==Type.Cmd?" of Commands":(type==Type.Chat?" of Chat":"")),9,p);
		if (Type.n==type) {
			a.setItem(0, new ItemGUI(item("&cClose", XMaterial.BARRIER)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					g.close(s);
				}
			});
			a.setItem(3, new ItemGUI(item("&6Chat", XMaterial.PAPER)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					openGUI(s, Type.Chat);
				}
			});
			a.setItem(5, new ItemGUI(item("&6Commands", XMaterial.COMMAND_BLOCK)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					openGUI(s, Type.Cmd);
				}
			});
		} else {
			String path = type == Type.Cmd?"Commands.":"Chat.";
			a.setItem(3, new ItemGUI(item("&eAntiSpam", Loader.config.getBoolean(path + "AntiSpam")?XMaterial.LIME_DYE:XMaterial.RED_DYE)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					Loader.config.set(path + ".AntiSpam", !Loader.config.getBoolean(path + ".AntiSpam"));
					openGUI(s, type);
				}
			});

			a.setItem(4, new ItemGUI(item("&eCaps", Loader.config.getBoolean(path + "Caps")?XMaterial.LIME_DYE:XMaterial.RED_DYE)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					Loader.config.set(path + ".Caps", !Loader.config.getBoolean(path + ".Caps"));
					openGUI(s, type);
				}
			});

			a.setItem(5, new ItemGUI(item("&eAntiSwear", Loader.config.getBoolean(path + "AntiSwear")?XMaterial.LIME_DYE:XMaterial.RED_DYE)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					Loader.config.set(path + ".AntiSwear", !Loader.config.getBoolean(path + ".AntiSwear"));
					openGUI(s, type);
				}
			});

			a.setItem(0, new ItemGUI(item("&cBack", XMaterial.ARROW)) {
				@Override
				public void onClick(Player s, GUICreatorAPI g, ClickType c) {
					openGUI(s, Type.n);
				}
			});
		}
	}
	
	public static ItemStack item(String name, XMaterial typ) {
		return ItemCreatorAPI.create(typ.parseMaterial(), 1, name);
	}
}
