package Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControlEvents.PlayerBlockedCommandEvent;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.SudoType;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

/**
 * 1.2. 2020
 * 
 * @author Straiker123
 *
 */
@SuppressWarnings("deprecation")
public class SecurityListenerV3 implements Listener {
	public static enum Security {
		Spam, Swear
	}

	public static enum ALocation {
		Chat, Command
	}

	private boolean is(String s) {
		for (Player p : TheAPI.getOnlinePlayers()) {
			if (s.equalsIgnoreCase(p.getName()))
				return true;

		}
		return false;
	}

	private int count(String string) {
		int upperCaseCount = 0;
		for (int i = 0; i < string.length(); i++)
			if (Character.isAlphabetic(string.charAt(i)) && Character.isUpperCase(string.charAt(i)))
				upperCaseCount++;
		return upperCaseCount;
	}

	private String removeDoubled(String s) {
		char prevchar = 0;
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (prevchar != c)
				sb.append(c);
			prevchar = c;
		}
		return sb.toString();
	}

	private int countDoubled(String s) {
		return s.length() - removeDoubled(s).length();
	}

	static HashMap<Player, String> old = new HashMap<Player, String>();

	private boolean isSim(Player p, String msg) {
		if (Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
			if (old.containsKey(p)) {
				String o = old.get(p);
				old.remove(p);
				old.put(p, msg);
				if (o.length() >= 5 && msg.length() >= o.length()) {
					String f = o.substring(1, o.length() - 1);
					return o.equalsIgnoreCase(msg) || msg.startsWith(o) || f.startsWith(msg) || f.equalsIgnoreCase(msg);
				}
			} else
				old.put(p, msg);
		}
		return false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("ServerControl.CommandsAccess") && setting.cmdblock) {
			for (String cen : Loader.config.getStringList("Options.CommandsBlocker.List")) {
				String mes = e.getMessage().toLowerCase();
				if (mes.startsWith("/" + cen.toLowerCase()) || mes.startsWith("/bukkit:" + cen.toLowerCase())
						|| mes.startsWith("/minecraft:" + cen.toLowerCase())) {
					PlayerBlockedCommandEvent ed = new PlayerBlockedCommandEvent(p, e.getMessage(), cen);
					Bukkit.getPluginManager().callEvent(ed);
					if (ed.isCancelled()) {
						e.setCancelled(true);
						API.hasPerm(p, "ServerControl.CommandsAccess");
					}
				}
			}
		} else if (!p.hasPermission("ServerControl.Admin")) {
			String message = e.getMessage();
			String d = ""; // anti doubled letters
			int up = 0; // anti caps
			if (setting.spam_double) {
				for (String s : message.split(" ")) {
					if (!is(s)) {
						up = up + count(s);
						d = d + " " + removeDoubled(s);
					} else
						d = d + " " + s;
				}
			}
			d = d.replaceFirst(" ", "");
			String build = d;
			if (setting.caps_cmd) {
				build = "";
				if ((up / d.length()) * 100 >= 60 && !p.hasPermission("ServerControl.Caps") && d.length() > 5) {
					for (String s : d.split(" ")) {
						if (!is(s)) {
							build = build + " " + s.toLowerCase();
						} else
							build = build + " " + s;

					}

					build = build.replaceFirst(" ", "");
				}
			}
			message = build;
			if (setting.swear_cmd && API.getVulgarWord(build) || setting.spam_cmd && API.getSpamWord(build)) {
				if (API.getVulgarWord(build)) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, e.getMessage(), build);
					return;
				} else if (API.getVulgarWord(build.replace(" ", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, e.getMessage(),
							build.replace(" ", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, e.getMessage(),
							build.replaceAll("[^a-zA-Z0-9]+", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, e.getMessage(),
							build.replaceAll("[a-zA-Z0-9]+", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, e.getMessage(),
							build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, e.getMessage(),
							build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""));
					return;
				}
			}
			e.setMessage(build);
		}
		/*if(!e.isCancelled()) {
			if(e.getMessage().toLowerCase().startsWith("/reload")||e.getMessage().toLowerCase().startsWith("/rl")) {
			if (API.hasPerm(p, "ServerControl.Reload")) {
				e.setCancelled(true);
				String[] args = e.getMessage().split(" ");
				if (args.length == 0) {
					BigTask.start(TaskType.RELOAD, Loader.config.getLong("Options.WarningSystem.Reload.PauseTime"));
					return;
				}
				if (args[0].equalsIgnoreCase("cancel")) {
					if (BigTask.r != -1)
						BigTask.cancel();
					return;
				}
				if (args[0].equalsIgnoreCase("now")) {
					BigTask.start(TaskType.RELOAD, 0);
					return;
				}
				if (BigTask.r == -1)
					BigTask.start(TaskType.RELOAD, StringUtils.getTimeFromString(args[0]));
				return;
			}return;}
			if(e.getMessage().toLowerCase().startsWith("/restart")) {
			if (API.hasPerm(p, "ServerControl.Restart")) {
				e.setCancelled(true);
				String[] args = e.getMessage().split(" ");
				if (args.length == 0) {
					BigTask.start(TaskType.RESTART, Loader.config.getLong("Options.WarningSystem.Restart.PauseTime"));
					return;
				}
				if (args[0].equalsIgnoreCase("cancel")) {
					if (BigTask.r != -1)
						BigTask.cancel();
					return;
				}
				if (args[0].equalsIgnoreCase("now")) {
					BigTask.start(TaskType.RESTART, 0);
					return;
				}
				if (BigTask.r == -1)
					BigTask.start(TaskType.RESTART, StringUtils.getTimeFromString(args[0]));
				return;
			}return;}
			if(e.getMessage().toLowerCase().startsWith("/stop")) {
			if (API.hasPerm(p, "ServerControl.Stop")) {
				e.setCancelled(true);
				String[] args = e.getMessage().split(" ");
				if (args.length == 0) {
					BigTask.start(TaskType.STOP, Loader.config.getLong("Options.WarningSystem.Stop.PauseTime"));
					return;
				}
				if (args[0].equalsIgnoreCase("cancel")) {
					if (BigTask.r != -1)
						BigTask.cancel();
					return;
				}
				if (args[0].equalsIgnoreCase("now")) {
					BigTask.start(TaskType.STOP, 0);
					return;
				}
				if (BigTask.r == -1)
					BigTask.start(TaskType.STOP, StringUtils.getTimeFromString(args[0]));
				return;
			}return;}
			
		}*/
	}

	private void call(Security swear, Player s, String original, String replace) {
		User d = TheAPI.getUser(s);
		String name = swear == Security.Spam ? "Spam" : "VulgarWords";
		String r = swear == Security.Spam ? "Spam" : "Swear";
		Loader.config.set(name, Loader.config.getInt(name) + 1);
		d.set(name, d.getInt(name) + 1);
		if (Loader.config.getBoolean("TasksOnSend." + r + ".Use-Commands")) {
			for (String cmds : Loader.config.getStringList("TasksOnSend." + r + ".Commands")) {
				TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName())));
			}
		}
		if (Loader.config.getBoolean("AutoKickLimit." + r + ".Use")) {
			if (d.getInt(name) >= Loader.config.getInt("AutoKickLimit." + r + ".Number")) {
				d.set(name, d.getInt(name) - Loader.config.getInt("AutoKickLimit." + r + ".Number"));
				if (Loader.config.getBoolean("AutoKickLimit." + r + ".Message.Use")) {
					for (String cmds : Loader.config.getStringList("AutoKickLimit." + r + ".Message.List")) {
						TheAPI.msg(cmds.replace("%player%", s.getName()).replace("%number%",
								Loader.config.getInt("AutoKickLimit." + r + ".Number") + ""), s);
					}
				}
				if (Loader.config.getBoolean("AutoKickLimit." + r + ".Commands.Use")) {
					for (String cmds : Loader.config.getStringList("AutoKickLimit." + r + ".Commands.List")) {
						TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName())
								.replace("%number%", Loader.config.getInt("AutoKickLimit." + r + ".Number") + "")));
						if (cmds.toLowerCase().startsWith("kick")) {
							d.set("Kicks", d.getInt("Kicks") + 1);
						}
					}
				}
			}
		}
		if (Loader.config.getBoolean("AutoKickLimit.Kick.Use")) {
			if (d.getInt("Kicks") >= Loader.config.getInt("AutoKickLimit.Kick.Number")) {
				d.set("Kicks", d.getInt("Kicks") - Loader.config.getInt("AutoKickLimit.Kick.Number"));
				if (Loader.config.getBoolean("AutoKickLimit.Kick.Message.Use")) {
					for (String cmds : Loader.config.getStringList("AutoKickLimit.Kick.Message.List")) {
						TheAPI.msg(cmds.replace("%player%", s.getName()).replace("%number%",
								Loader.config.getInt("AutoKickLimit.Kick.Number") + ""), s);
					}
				}
				if (Loader.config.getBoolean("AutoKickLimit.Kick.Commands.Use")) {
					for (String cmds : Loader.config.getStringList("AutoKickLimit.Kick.Commands.List")) {
						TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", s.getName())
								.replace("%number%", Loader.config.getInt("AutoKickLimit.Kick.Number") + "")));
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("ServerControl.Admin")) {
			String message = e.getMessage();
			String d = ""; // anti doubled letters
			int up = 0; // anti caps
			if (setting.spam_double) {
				if (message.split(" ").length == 0) {
					if (!is(message)) {
						up = up + count(message);
						d = d + " " + (countDoubled(message) >= 5 ? removeDoubled(message) : message);
					} else
						d = d + " " + message;
				} else
					for (String s : message.split(" ")) {
						if (!is(s)) {
							up = up + count(s);
							d = d + " " + (countDoubled(s) >= 5 ? removeDoubled(s) : s);
						} else
							d = d + " " + s;
					}
				d = d.replaceFirst(" ", "");
			} else
				d = message;
			String build = d;
			if (setting.caps_chat) {
				if (up != 0
						? up / ((double) d.length() / 100) >= 60 && !p.hasPermission("ServerControl.Caps")
								&& d.length() > 5
						: false) {
					build = "";
					if (d.split(" ").length == 0) {
						if (!is(d)) {
							build = build + " " + d.toLowerCase();
						} else
							build = build + " " + d;
					} else
						for (String s : d.split(" ")) {
							if (!is(s)) {
								build = build + " " + s.toLowerCase();
							} else
								build = build + " " + s;
						}
					build = build.replaceFirst(" ", "");
				}
			}
			message = build;
			if (Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
				if (isSim(p, e.getMessage())) {
					e.setCancelled(true);
					return;
				}
			}
			if (setting.swear_chat && API.getVulgarWord(build) || setting.spam_chat && API.getSpamWord(build)) {
				if (API.getVulgarWord(build)) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, message, build);
					return;
				} else if (API.getVulgarWord(build.replace(" ", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, message,
							build.replace(" ", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, message,
							build.replaceAll("[^a-zA-Z0-9]+", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, message,
							build.replaceAll("[a-zA-Z0-9]+", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, message,
							build.replaceAll("[^a-zA-Z0-9]+", "").replace(" ", ""));
					return;
				} else if (API.getVulgarWord(build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""))) {
					e.setCancelled(true);
					call((API.getVulgarWord(build) ? Security.Swear : Security.Spam), p, message,
							build.replaceAll("[a-zA-Z0-9]+", "").replace(" ", ""));
					return;
				}
			}
			e.setMessage(message);
		}
	}
}