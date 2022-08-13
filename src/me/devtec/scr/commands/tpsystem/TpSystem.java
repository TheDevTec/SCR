package me.devtec.scr.commands.tpsystem;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.game.Position;

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
		Config c = me.devtec.shared.API.getUser(player.getUniqueId());
		c.set("back", new Position(player.getLocation()));
		c.save();
	}
	
	public static void teleportBack(Player player) {
		Position pos = me.devtec.shared.API.getUser(player.getUniqueId()).getAs("back", Position.class);
		setBack(player);
		player.teleport(pos.toLocation());
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
				MessageUtils.message(who, "tpaccept.expired", null);
				askTpaHere.remove(who);
				task.remove(who);
				//Scheduler.cancelTask( task.get(who) );
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
				MessageUtils.message(to, "tpaccept.expired", null);
				askTpa.remove(to);
				askTpa2.remove(who);
				task.remove(to);
				//Scheduler.cancelTask( task.get(to) );
			}
		}.runLater( 20*StringUtils.timeFromString(Loader.config.getString("options.tp-accept_cooldown")) )
		);
		
	}
	private static void stopTask(Player player) {
		Scheduler.cancelTask( task.get(player) );
		task.remove(player);
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
			stopTask(player);
			//task.remove(player);
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
			stopTask(player);
			//task.remove(player);
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
			stopTask(player);
			//task.remove(player);
			return;
		}
		if(askTpaHere.containsKey(player)) { //tpahere deny
			Player to = askTpaHere.get(player);

			MessageUtils.message(player, "tpadeny.sender", Placeholders.c().addPlayer("target", to).addPlayer("player", player));
			MessageUtils.message(to, "tpadeny.receiver", Placeholders.c().addPlayer("target", to).addPlayer("player", player));

			askTpaHere.remove(player);
			stopTask(player);
			//task.remove(player);
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
			stopTask(to);
			//task.remove(to);
			return;
		}
		//TODO - tpahere deny nebude, protože se tam dá použít i @a atd... :(
	}
}
