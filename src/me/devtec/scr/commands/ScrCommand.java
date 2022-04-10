package me.devtec.scr.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.listeners.commands.PluginEnable;
import me.devtec.shared.commands.manager.PermissionChecker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public interface ScrCommand {
	public static final PermissionChecker<CommandSender> PERMS_CHECKER = (sender, perm, tablist) -> {return sender.hasPermission(perm);};
	public static final PermissionChecker<Player> PLAYER_PERMS_CHECKER = (sender, perm, tablist) -> {return sender.hasPermission(perm);};
	
	public default void msgConfig(CommandSender sender, String path, Object... placeholders) {
		MessageUtils.msgConfig(sender, path, placeholders);
	}
	
	public default void msg(CommandSender sender, String path, Object... placeholders) {
		MessageUtils.msg(sender, path, placeholders);
	}
	
	public default Collection<? extends Player> playerSelectors(CommandSender sender, String selector) {
		char lowerCase = selector.equals("*")?'*':Character.toLowerCase(selector.charAt(1));
		if(lowerCase=='*' || selector.charAt(0)=='@') {
			switch(lowerCase) {
			case 'a':
			case 'e':
			case '*':
				return BukkitLoader.getOnlinePlayers();
			case 'r':
				return Arrays.asList(StringUtils.getRandomFromCollection(BukkitLoader.getOnlinePlayers()));
			case 's':
			case 'p':
				Location pos = null;
				if(sender instanceof Player) {
					pos = ((Player) sender).getLocation();
				}else
				if(sender instanceof BlockCommandSender) {
					pos = ((BlockCommandSender) sender).getBlock().getLocation();
				}else pos = new Location(Bukkit.getWorlds().get(0), 0, 0 , 0);
				double distance = -1;
				Player nearestPlayer = null;
				for(Player sameWorld : pos.getWorld().getPlayers()) {
					if(distance == -1 || distance < sameWorld.getLocation().distance(pos)) {
						distance=sameWorld.getLocation().distance(pos);
						nearestPlayer=sameWorld;
					}
				}
				return Arrays.asList(nearestPlayer == null ? BukkitLoader.getOnlinePlayers().iterator().next() : nearestPlayer);
			}
		}
		return Arrays.asList(Bukkit.getPlayer(selector));
	}
	
	public default void help(CommandSender sender, int arg) {
		Object val = Loader.commands.get(configSection()+".help."+arg);
		if(val instanceof Collection) {
			for(String list : Loader.commands.getStringList(configSection()+".help."+arg))
				msg(sender, list);
		}else
			msg(sender, Loader.commands.getString(configSection()+".help."+arg));
	}
	
	// Do not overide this - onLoad
	@SuppressWarnings("unchecked")
	public default void initFirst(List<String> cmds) {
		List<String> loadAfter = Loader.commands.getStringList(configSection()+".loadAfter");
		if(!loadAfter.isEmpty()) {
			List<String> registered = new ArrayList<>();
			for(String pluginName : loadAfter) {
				if(Bukkit.getPluginManager().getPlugin(pluginName)!=null && !Bukkit.getPluginManager().getPlugin(pluginName).isEnabled())registered.add(pluginName);
			}
			if(!registered.isEmpty()) {
				PluginEnable.init();
				PluginEnable.waiting.put(this, new List[] {registered, cmds});
				return;
			}
		}
		String firstUp = Character.toUpperCase(configSection().charAt(0)) + configSection().substring(1);
		Loader.plugin.getLogger().info("["+firstUp+"] Registering command.");
		init(cmds);
	}
	
	public void init(List<String> cmds);
	
	public default void disabling() {}
	
	public String configSection();
}
