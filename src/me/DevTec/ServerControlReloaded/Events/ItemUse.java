package me.DevTec.ServerControlReloaded.Events;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.nms.nbt.NBTEdit;

public class ItemUse implements Listener {
	public static int STRING = 2, COLLECTION = 1, NUMBER = 0;

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(e.getItem()!=null && e.getItem().getType()!=Material.AIR) {
			String has = (String)getActions(e.getItem(), "process", STRING);
			if(has==null||has.trim().equals(""))return;
			e.setCancelled(true);
			if(!canUse(e.getItem()))return;
			Collection<String> c = (Collection<String>)getActions(e.getItem(), "process.msg", COLLECTION);
			if(c!=null)for(String f : c)
				TheAPI.msg(PlaceholderAPI.setPlaceholders(e.getPlayer(), f.replace("%player%", e.getPlayer().getName())), e.getPlayer());
			c = (Collection<String>)getActions(e.getItem(), "process.console.cmd", COLLECTION);
			if(c!=null)for(String f : c)
				TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(e.getPlayer(), f.replace("%player%", e.getPlayer().getName())));
			c = (Collection<String>)getActions(e.getItem(), "process.player.cmd", COLLECTION);
			if(c!=null)for(String f : c)
				TheAPI.sudo(e.getPlayer(), SudoType.COMMAND, PlaceholderAPI.setPlaceholders(e.getPlayer(), f.replace("%player%", e.getPlayer().getName())));
			doUse(e.getPlayer(), e.getItem());
		}
	}
	
	private boolean canUse(ItemStack item) {
		NBTEdit e = new NBTEdit(item);
		long val = e.getLong("process.cooldown");
		if(val==0)return true; //no cooldown
		long last = e.getLong("process.last");
		if(last+val - System.currentTimeMillis()/1000 <= 0) {
			e.setLong("process.last", System.currentTimeMillis()/1000);
			NMSAPI.setNBT(item, e);
			return true;
		}
		return false;
	}
	
	private void doUse(Player p, ItemStack item) {
		NBTEdit e = new NBTEdit(item);
		long val = e.getLong("process.usage");
		if(val==0)return; //infinity uses
		long uses = e.getLong("process.uses")+1;
		if(uses>=val) {
			item.setAmount(item.getAmount()-1);
			if(item.getAmount()<=0) {
				p.getInventory().remove(item);
				return;
			}else
				e.setLong("process.uses", 0);
		}else e.setLong("process.uses", uses);
		NMSAPI.setNBT(item, e);
	}

	public static Object getActions(ItemStack item, String path, int type) {
		String val = new NBTEdit(item).getString(path);
		if(val==null)return null;
		Object o = Reader.read(val);
		if(type==1 && o instanceof Collection == false)return null;
		if(type==0 && o instanceof Number == false)return null;
		if(type==2 && o instanceof String == false)return null;
		return o;
	}
}
