package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class BookEdit implements Listener {

	@EventHandler
	public void onBook(PlayerEditBookEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();
		BookMeta meta = event.getNewBookMeta();
		String author = meta.getAuthor();
		if(author!=null) {
			//TODO rules
			meta.setAuthor(author);
		}
		String title = meta.getTitle();
		if(title!=null) {
			//TODO rules
			meta.setTitle(title);
		}
		List<String> pages = new ArrayList<>();
		for(String page : meta.getPages()) {
			//TODO rules
			if(page!=null)pages.add(page);
		}
		meta.setPages(pages);
	}
}
