package me.devtec.scr.commands.tpsystem;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;

public class TpSystem {

	
	public static void teleport(Player who, Player to) {
		//TODO - safe TP ?
		setBack(who);
		who.teleport(to);
	}
	
	public static void teleport(Player who, Location to) {
		//TODO - safe TP ?
		setBack(who);
		who.teleport(to);
	}
	
	public static void setBack(Player player) {
		
	}

	public static HashMap<Player, Player> askTpa = new HashMap<>();  // ke komu | kdo
	public static HashMap<Player, Player> askTpa2 = new HashMap<>();  // kdo | ke komu
	
	public static HashMap<Player, Player> askTpaHere = new HashMap<>();  // kdo | ke komu
	private static HashMap<Player, Integer> task = new HashMap<>(); //kdo | task
	
	public static void askTpaHere(Player who, Player to) { // jeho (who) ke mně (to)
		if(askTpaHere.containsKey(who)) {
			Scheduler.cancelTask( task.get(who) );
			askTpaHere.remove(who);
			task.remove(who);
		}
		askTpaHere.put(who, to);
		
		task.put(who, new Tasker() {
			
			@Override
			public void run() {
				MessageUtils.message(who, "tpaaccept.expired", null);
				Scheduler.cancelTask( task.get(who) );
				askTpaHere.remove(who);
				task.remove(who);
			}
		}.runLater( 20*StringUtils.timeFromString(Loader.config.getString("options.tp-accept_cooldown")) )
		);
		
	}
	// who - tpa sender
	public static void askTpa(Player who, Player to) { // kdo | ke komu
		if(askTpa.containsKey(to)) {
			Scheduler.cancelTask( task.get(to) );
			askTpa.remove(to);
			askTpa2.remove(who);
			task.remove(to);
		}
		askTpa.put(to, who);
		askTpa2.put(who, to);
		
		task.put(to, new Tasker() {
			
			@Override
			public void run() {
				MessageUtils.message(to, "tpaaccept.expired", null);
				Scheduler.cancelTask( task.get(to) );
				askTpa.remove(to);
				askTpa2.remove(who);
				task.remove(to);
			}
		}.runLater( 20*StringUtils.timeFromString(Loader.config.getString("options.tp-accept_cooldown")) )
		);
		
	}
	public static void accept(Player player) {
		if(askTpa.containsKey(player)) { //tpa request
			Player who = askTpa.get(player);
			//Accept
			MessageUtils.message(player, "tpaccept.accepted.sender", Placeholders.c().addPlayer("target", who).addPlayer("player", player));
			MessageUtils.message(who, "tpaccept.accepted.receiver", Placeholders.c().addPlayer("target", who).addPlayer("player", player));
			//Teleporting
			MessageUtils.message(player, "tpaccept.teleporting.to", Placeholders.c().addPlayer("player", who).addPlayer("target", player));
			MessageUtils.message(who, "tpaccept.teleporting.who", Placeholders.c().addPlayer("player", who).addPlayer("target", player));
			
			teleport(who, player);
			askTpa.remove(player);
			askTpa2.remove(who);
			task.remove(player);
			return;
		}
		if(askTpaHere.containsKey(player)) { //tpahere request
			Player to = askTpaHere.get(player);
			//Accept
			MessageUtils.message(player, "tpaccept.accepted.sender", Placeholders.c().addPlayer("target", to).addPlayer("player", player));
			MessageUtils.message(to, "tpaccept.accepted.receiver", Placeholders.c().addPlayer("target", to).addPlayer("player", player));
			//Teleporting
			MessageUtils.message(player, "tpaccept.teleporting.who", Placeholders.c().addPlayer("target", to).addPlayer("player", player));
			MessageUtils.message(to, "tpaccept.teleporting.to", Placeholders.c().addPlayer("target", to).addPlayer("player", player));
			
			teleport(player, to);
			askTpaHere.remove(player);
			task.remove(player);
			return;
		}
	}
	public static void deny(Player player) {
		if(askTpa.containsKey(player)) { //tpa deny
			Player who = askTpa.get(player);
			
			MessageUtils.message(player, "tpadeny.sender", Placeholders.c().addPlayer("target", who).addPlayer("player", player));
			MessageUtils.message(who, "tpadeny.receiver", Placeholders.c().addPlayer("target", who).addPlayer("player", player));

			askTpa.remove(player);
			askTpa2.remove(who);
			task.remove(player);
			return;
		}
		if(askTpaHere.containsKey(player)) { //tpahere deny
			Player to = askTpaHere.get(player);

			MessageUtils.message(player, "tpadeny.sender", Placeholders.c().addPlayer("target", to).addPlayer("player", player));
			MessageUtils.message(to, "tpadeny.receiver", Placeholders.c().addPlayer("target", to).addPlayer("player", player));

			askTpaHere.remove(player);
			task.remove(player);
			return;
		}
		
	}
	
	public static void cancel(Player player) {
		if(askTpa.containsKey(player)) { //tpa cancel
			Player to = askTpa2.get(player);
			
			MessageUtils.message(player, "tpacancel.sender", Placeholders.c().addPlayer("player", to).addPlayer("target", player));
			MessageUtils.message(to, "tpacancel.receiver", Placeholders.c().addPlayer("player", to).addPlayer("target", player));

			askTpa.remove(to);
			askTpa2.remove(player);
			Scheduler.cancelTask( task.get(to) );
			task.remove(to);
			return;
		}
		//TODO - tpahere deny nebude, protože se tam dá použít i @a atd... :(
	}
}
