package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.scr.JsonUtils;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.utils.StringUtils;

public class AsyncChat implements Listener {

	public static Map<String, List<String>> perWorldGroup;
	public static List<Rule> rules = new ArrayList<>();
	public static boolean jsonText;
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();
		if(isWaiting(s)) {
			event.setCancelled(true);
			return;
		}
		double radius = getChatRadius(s);
		if(radius>0) {
			Iterator<Player> recipients = event.getRecipients().iterator();
			Location loc = s.getLocation();
			while(recipients.hasNext()) {
				Player target = recipients.next();
				if(target.getWorld()!=loc.getWorld() || target.getLocation().distance(loc) > radius)
					recipients.remove();
			}
		}else {
			if(!perWorldGroup.isEmpty()) {
				Iterator<Player> recipients = event.getRecipients().iterator();
				Location loc = s.getLocation();
				while(recipients.hasNext()) {
					Player target = recipients.next();
					if(target.getWorld()!=loc.getWorld())
						recipients.remove();
				}
			}
		}
		String msg = event.getMessage();
		Object format = getChatFormat(s);
		String formatColor = JsonUtils.getChatFormatColorOf(format);
		msg=Colorizer.process(s, msg);
		msg=processPrevention(msg, formatColor.isEmpty()?"§f":formatColor, event.getRecipients(), 2, true, 25); //anti flood & anti caps
		for(Rule rule : rules) {
			msg=rule.apply(msg, s);
			if(msg==null) {
				event.getRecipients().clear();
				event.setCancelled(true);
				return;
			}
		}
		if(jsonText) {
			String consoleResult = JsonUtils.msgRaw(format, PlaceholderBuilder.make(s, "player").add("mesage", msg), s, event.getRecipients().toArray(new Player[0]));
			event.getRecipients().clear();
			event.setFormat(consoleResult);
			event.setMessage(msg);
		}else {
			String text = PlaceholderBuilder.make(s, "player").add("mesage", msg).apply(format.toString());
			event.setFormat(text);
			event.setMessage(msg);
		}
	}
	
	private Object getChatFormat(Player s) {
		return null;
	}

	private double getChatRadius(Player s) {
		return 15;
	}
	
	public boolean isWaiting(Player player) {
		long cooldown = getCooldown(player);
		if(cooldown<=0)return false;
		CooldownAPI cd = new CooldownAPI(player);
		if(cd.expired("chat")) {
			cd.createCooldown("chat", cooldown*20);
			return false;
		}
		return true;
	}
	
	private long getCooldown(Player player) {
		return 0;
	}
	
	static List<Pattern> prevent = new ArrayList<>();
	static {
		prevent.add(buildFor("OMG"));
		prevent.add(buildFor("LOL"));
		prevent.add(buildFor("WTF"));
		prevent.add(buildFor("HAHAHA"));
		prevent.add(buildFor("FLOOOOD"));
		prevent.add(buildFor("IT"));
	}
	
	private String processPrevention(String message, String defColor, Set<Player> seen, int sameLetters, boolean antiCaps, double capsPercent) {
		if(message.length()>3) {
			StringBuilder builder = new StringBuilder();
			Map<String, Integer> values = new HashMap<>();
			
			List<String> playerNames = new ArrayList<>();
			//player names
			int i = 0;
			for(Player player : seen) {
				playerNames.add(player.getName());
				Pattern sub = buildFor(player.getName());
				Matcher match = sub.matcher(message);
				while(match.find()) {
					String path = sub.pattern().replace("\\(", "(").replace("\\)", ")").replace("\\{", "{").replace("\\}", "}")
							.replace("\\.", ".").replace("\\$", "$").replace("\\%", "%");
					if(values.containsKey(path)) {
						message=match.replaceFirst("\\§\\§"+values.get(path));
						match.reset(message);
					}else {
						values.put(path,++i);
						message=match.replaceFirst("\\§\\§"+i);
						match.reset(message);
					}
				}
			}
			for(Pattern sub : prevent) {
				Matcher match = sub.matcher(message);
				while(match.find()) {
					String path = match.group();
					if(values.containsKey(path)) {
						message=match.replaceFirst("\\§\\§"+values.get(path));
						match.reset(message);
					}else {
						values.put(path,++i);
						message=match.replaceFirst("\\§\\§"+i);
						match.reset(message);
					}
				}
			}
			
			int length = (""+i).length();
			
			boolean con = false;
			char prev = 0;
			int amount = 0, countCaps = 0;
			int chars = 0;
			if(sameLetters!=0) { //anti flood & anti caps
				for(char c : message.toCharArray()) {
					char upper = Character.toUpperCase(c);
					if(c!=' ') { //ignore spaces
						if(prev==upper) {
							if(++amount>=sameLetters) {
								if(!con || !Character.isDigit(c) && con || Character.isDigit(c) && con && (++chars > length)) {
									con=false;
									chars=0;
									continue;
								}
							}
						}else 
							amount=0;
						boolean may = prev=='§';
						prev=upper;
						if(prev=='§' && may) {
							con=true;
						}
						if(upper==c)++countCaps;
					}
					builder.append(c);
				}
			}else { //anti caps
				if(!antiCaps) {
					builder.append(message);
				}else
					for(char c : message.toCharArray()) {
						char upper = Character.toUpperCase(c);
						if(c!=' ') { //ignore spaces
							if(prev==upper) {
								if(++amount>=sameLetters) {
									if(!con || !Character.isDigit(c) && con || Character.isDigit(c) && con && (++chars > length)) {
										con=false;
										chars=0;
										continue;
									}
								}
							}else 
								amount=0;
							boolean may = prev=='§';
							prev=upper;
							if(prev=='§' && may) {
								con=true;
							}
							if(upper==c)++countCaps;
						}
						builder.append(c);
					}
			}
			if(antiCaps && ((double)countCaps/builder.length())*100 >= capsPercent) {
				StringBuilder rebuild = new StringBuilder(builder.length());
				
				String[] builders = split(builder);
				char first = builders[0].charAt(0);
				for(String split : builders)
					rebuild.append(split.toLowerCase()).append(' ');
				rebuild.deleteCharAt(rebuild.length()-1); //remove last space
				rebuild.setCharAt(0, first); //first char (upper case?)
				
				message=rebuild.toString();
			}else
				message=builder.toString();
			for(Entry<String, Integer> ints : values.entrySet()) {
				if(playerNames.contains(ints.getKey())) {
					String replace = notifyReplacement(ints.getKey());
					if(replace.contains("&")||replace.contains("#")||replace.contains("§")) { //colors?
						//split to 2 pieces
						String[] pieces = message.split("\\§\\§"+ints.getValue());
						String color = StringUtils.getLastColors(pieces[0]);
						if(color.isEmpty())color=defColor;
						message=pieces[0]+replace+(color.equals("§u")?colorizeRainbow(color+pieces[1]):color+pieces[1]);
					}else
						message=message.replace("§§"+ints.getValue(), replace);
				}else {
					message=message.replace("§§"+ints.getValue(), ints.getKey());
				}
			}
		}
		return message;
	}
	private static final Pattern fixedSplit = Pattern.compile(
			"(#[A-Fa-f0-9]{6}([&§][K-Ok-oRr])*|[&§][Xx]([&§][A-Fa-f0-9]){6}([&§][K-Ok-oRr])*|[&§][A-Fa-f0-9K-ORrk-oUuXx]([&§][K-Ok-oRr])*)");

	
	private String colorizeRainbow(String msg) {
		String[] split = fixedSplit.split(msg);
		// atempt to add colors to split
		Matcher m = fixedSplit.matcher(msg);
		int id = 1;
		while (m.find()) {
			try {
				split[id] = m.group(1) + split[id++];
			} catch (Exception err) {
			}
		}
		// colors
		StringBuilder d = new StringBuilder(msg.length());
		for (String ff : split) {
			if (ff.toLowerCase().contains("§u"))
				ff = StringUtils.color.colorize(ff.replaceAll("\\§u", ""));
			d.append(ff);
		}
		return d.toString();
	}
	
	private String notifyReplacement(String key) {
		return key;
	}

	private static String[] split(StringBuilder builder) {
		List<String> splits = new ArrayList<>();
		StringBuilder sub = new StringBuilder();
		for(int i = 0; i < builder.length(); ++i) {
			char c = builder.charAt(i);
			if(c==' ') {
				splits.add(sub.toString());
				sub.delete(0, sub.length());
				continue;
			}
			sub.append(c);
		}
		splits.add(sub.toString());
		return splits.toArray(new String[0]);
	}

	public static Pattern buildFor(String text) {
		return Pattern.compile(text.replace("(", "\\(").replace(")", "\\)").replace("{", "\\{").replace("}", "\\}")
				.replace(".", "\\.").replace("$", "\\$").replace("%", "\\%"), Pattern.CASE_INSENSITIVE);
	}
	
}
