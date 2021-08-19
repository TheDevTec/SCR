package me.devtec.servercontrolreloaded.events.functions;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.nms.nbt.NBTEdit;

public class ItemProcessUse implements Listener {
	public static final int COLLECTION = 1;

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)||e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
				Object has = getActions(e.getItem(), "process", 0);
    			if(has==null||has.toString().trim().isEmpty())return;
				e.setCancelled(true);
				if (!canUse(e.getItem())) return;
				Collection<String> c = (Collection<String>) getActions(e.getItem(), "process.msg", COLLECTION);
				if (c != null) for (String f : c)
					TheAPI.msg(PlaceholderAPI.setPlaceholders(e.getPlayer(), f.replace("%player%", e.getPlayer().getName())
							.replace("%whoused%", e.getPlayer().getName())), e.getPlayer());
				c = (Collection<String>) getActions(e.getItem(), "process.console.cmd", COLLECTION);
				if (c != null) for (String f : c)
					TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(e.getPlayer(), f.replace("%player%", e.getPlayer().getName())
							.replace("%whoused%", e.getPlayer().getName())));
				c = (Collection<String>) getActions(e.getItem(), "process.player.cmd", COLLECTION);
				if (c != null) for (String f : c)
					TheAPI.sudo(e.getPlayer(), SudoType.COMMAND, PlaceholderAPI.setPlaceholders(e.getPlayer(), f.replace("%player%", e.getPlayer().getName())
							.replace("%whoused%", e.getPlayer().getName())));
				doUse(e.getPlayer(), e.getItem());
			}
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getAction()==InventoryAction.PLACE_ALL||e.getAction()==InventoryAction.PLACE_ONE||e.getAction()==InventoryAction.PLACE_SOME
				||e.getAction()==InventoryAction.DROP_ALL_CURSOR||e.getAction()==InventoryAction.DROP_ALL_SLOT||e.getAction()==InventoryAction.DROP_ONE_CURSOR
						||e.getAction()==InventoryAction.DROP_ONE_SLOT
				||e.getAction()==InventoryAction.PICKUP_ALL||e.getAction()==InventoryAction.PICKUP_HALF||e.getAction()==InventoryAction.PICKUP_ONE||e.getAction()==InventoryAction.PICKUP_SOME
				|| e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
			if(e.getCurrentItem()!=null && e.getCurrentItem().getType()!=Material.AIR) {
				Object has = getActions(e.getCurrentItem(), "process", 0);
    			if(has==null||has.toString().trim().isEmpty())return;
    			Object invclick = getActions(e.getCurrentItem(), "process.inventoryclick", 0);
    			if(invclick==null || invclick.toString().trim().isEmpty())
    				return;
    			Object movable = getActions(e.getCurrentItem(), "process.movable", 0);
    			if(movable!=null && !movable.toString().trim().isEmpty() && !StringUtils.getBoolean(movable.toString()))
    				e.setCancelled(true);
    			Player player = Bukkit.getPlayer(e.getWhoClicked().getName());
    			if(player==null) return;
				if (!canUse(e.getCurrentItem())) return;
				Collection<String> c = (Collection<String>) getActions(e.getCurrentItem(), "process.msg", COLLECTION);
				if (c != null) for (String f : c)
					TheAPI.msg(PlaceholderAPI.setPlaceholders(player, f.replace("%player%", player.getName())
							.replace("%whoused%", player.getName())), player);
				c = (Collection<String>) getActions(e.getCurrentItem(), "process.console.cmd", COLLECTION);
				if (c != null) for (String f : c)
					TheAPI.sudoConsole(PlaceholderAPI.setPlaceholders(player, f.replace("%player%", player.getName())
							.replace("%whoused%", player.getName())));
				c = (Collection<String>) getActions(e.getCurrentItem(), "process.player.cmd", COLLECTION);
				if (c != null) for (String f : c)
					TheAPI.sudo(player, SudoType.COMMAND, PlaceholderAPI.setPlaceholders(player, f.replace("%player%", player.getName())
							.replace("%whoused%", player.getName())));
				doUse(player, e.getCurrentItem());
			}
		}
		
	}
	@EventHandler
	public void onInventoryClick(PlayerDropItemEvent e) {
		if(e.getItemDrop().getItemStack().getType() != Material.AIR) {
			Object has = getActions(e.getItemDrop().getItemStack(), "process", 0);
			if(has==null||has.toString().trim().isEmpty())return;
			Object movable = getActions(e.getItemDrop().getItemStack(), "process.movable", 0);
			if(movable!=null && !movable.toString().trim().isEmpty() && !StringUtils.getBoolean(movable.toString()))
				e.setCancelled(true);
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
		if(type==1 && !(o instanceof Collection))return null;
		return o;
	}
}
