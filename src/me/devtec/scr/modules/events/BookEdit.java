package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class BookEdit implements Listener {
	
	public static List<Rule> rules = new ArrayList<>();

	@EventHandler
	public void onBook(PlayerEditBookEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();
		BookMeta meta = event.getNewBookMeta();
		String author = meta.getAuthor();
		if(author!=null) {
			for(Rule rule : rules) {
				author=rule.apply(author, s);
				if(author==null)break;
			}
			meta.setAuthor(author);
		}
		String title = meta.getTitle();
		if(title!=null) {
			for(Rule rule : rules) {
				title=rule.apply(title, s);
				if(title==null)break;
			}
			meta.setTitle(title);
		}
		List<String> pages = new ArrayList<>();
		for(String page : meta.getPages()) {
			for(Rule rule : rules) {
				page=rule.apply(page, s);
				if(page==null)
					page="";
			}
			if(page!=null)pages.add(page);
		}
		meta.setPages(pages);
	}
}
