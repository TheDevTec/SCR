package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

@SuppressWarnings("deprecation")
public class SecurityListenerAntiAD implements Listener {
	public Loader plugin = Loader.getInstance;

	public String getMatches(String where) {
		String list = null;
		if (!API.getAdvertisementMatches(where).isEmpty())
			list = StringUtils.join(API.getAdvertisementMatches(where), ", ");
		return list;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(PlayerChatEvent event) {
		Player p = event.getPlayer();
		String message = event.getMessage().toLowerCase();

		if (setting.ad_chat) {
			if (!p.hasPermission("ServerControl.Advertisement")) {
				if (getMatches(message)!=null) {
						sendBroadcast(p, getMatches(message), AdType.OTHER);
						event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommands(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		if (setting.ad_cmd) {
			if (!p.hasPermission("ServerControl.Advertisement")) {
				if (getMatches(message)!=null) {
						sendBroadcast(p, getMatches(message), AdType.OTHER);
						event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void signCreate(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (setting.ad_sign) {
			if (!p.hasPermission("ServerControl.Advertisement")) {
				for (String line : e.getLines()) {
					if (getMatches(line)!=null) {
							sendBroadcast(p, getMatches(line), AdType.OTHER);
							e.setCancelled(true);
							e.getBlock().breakNaturally();
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void BookSave(PlayerEditBookEvent e) {
		Player p = e.getPlayer();
		if (setting.ad_book) {
			if (!p.hasPermission("ServerControl.Advertisement")) {
				String page = "";
				for (String line : e.getNewBookMeta().getPages()) {
					page = page + " " + line;
				}
				page = page.replace(" ", "");
				if (getMatches(page)!=null) {
						sendBroadcast(p, getMatches(page), AdType.OTHER);
						e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (setting.ad_anvil) {
			if (event.getInventory().getType() == InventoryType.ANVIL) {
				if (event.getSlotType() == InventoryType.SlotType.RESULT) {
					if (!p.hasPermission("ServerControl.Advertisement")) {
						String displayName = "";
						if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName())
							displayName = event.getCurrentItem().getItemMeta().getDisplayName();
						if (getMatches(displayName)!=null) {
								sendBroadcast(p, getMatches(displayName), AdType.OTHER);
								event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PickupAntiADEvent(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		String displayName = event.getItem().getItemStack().getItemMeta().getDisplayName();
		if (setting.ad_itempick) {
			if (!p.hasPermission("ServerControl.Advertisement")) {
				if (displayName != null) {
					if (getMatches(displayName)!=null) {
							sendBroadcast(p, getMatches(displayName), AdType.ITEM);
							event.setCancelled(true);
							event.getItem().remove();
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void DropAntiADEvent(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		String displayName = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
		if (setting.ad_itemdrop) {
			if (!p.hasPermission("ServerControl.Advertisement")) {
				if (displayName != null) {
					if (getMatches(displayName)!=null) {
							sendBroadcast(p, getMatches(displayName), AdType.ITEM);
							event.getItemDrop().remove();
					}
				}
			}
		}
	}

	public static enum AdType {
		ITEM, OTHER
	}

	public void sendBroadcast(Player p, String message, AdType type) {
		String AD = "Broadcast.Advertisement";
		switch (type) {
		case ITEM:
			AD += ".Item";
			break;
		case OTHER:
			break;
		}
		Loader.sendBroadcasts(p, AD, Placeholder.c().add("%message%", message), "ServerControl.Advertisement");
		if (Loader.config.getBoolean("Task.Ad.Use")) 
			for (String cmds : Loader.config.getStringList("Task.Ad.Commands"))
				TheAPI.sudoConsole(TheAPI.colorize(Loader.placeholder(p, cmds, null)));
	}
}