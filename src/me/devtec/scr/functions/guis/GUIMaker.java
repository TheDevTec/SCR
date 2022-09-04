package me.devtec.scr.functions.guis;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.functions.guis.NewLayout.Item;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.gui.GUI;
import me.devtec.theapi.bukkit.gui.GUI.ClickType;
import me.devtec.theapi.bukkit.gui.HolderGUI;
import me.devtec.theapi.bukkit.gui.ItemGUI;

public class GUIMaker {
	
	public Config config;
	private CommandStructure<CommandSender> cmd;
	
	public String title;
	public boolean dynamic;
	public NewLayout layout;
	private GUI gui;
	
	public GUIMaker(Config file) {
		dynamic = file.getBoolean("dynamic");
		title = file.getString("title");
		layout = new NewLayout(file); // Loading layout and Items
		//if(!dynamic)
		//	build(null);
		config = file;
	}
	
	public void newConfig(Config file) {
		this.config=file;
		dynamic = file.getBoolean("dynamic");
		title = file.getString("title");
		layout = new NewLayout(file); // Loading layout and Items
		//if(!dynamic)
		//	build(null);
	}
	
	public void createCommand() {
		if(!config.exists("commands"))
			return;
		
		List<String> cmds = config.getStringList("commands");

		cmd = CommandStructure.create(CommandSender.class, ScrCommand.PERMS_CHECKER, (s, structure, args) -> {
			if(!(s instanceof Player)) {
				s.sendMessage(StringUtils.colorize("/cmd <player>"));
				return;
			}
			if (config.getBoolean("enabled"))
				open((Player)s);
		}).permission(permission())
				.selector(Selector.PLAYER, (s, structure, args) -> {
					Player p = Bukkit.getPlayer(args[0]);
					if(p!=null && config.getBoolean("enabled"))
						open(p);
				}).permission(permission()+".other");

		cmd.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}
	private String permission() {
		if (config.exists("permission") && !config.getString("permission").isEmpty())
			return config.getString("permission");
		return null;
	}

	public long lastUpdate;
	public void open(Player p) {
		if(dynamic)
			build(p);
		if(gui == null)
			build(p);
		if(gui!=null && dynamic==false) {
			long updateAfter = 900;
			if(config.exists("update"))
				updateAfter = StringUtils.timeFromString(config.getString("update"));
			if( (lastUpdate - System.currentTimeMillis() / 1000 + updateAfter) <= 0)
				build(p);
		}
		gui.open(p);
	}

	private void build(Player p) {
		gui = new GUI(
				PlaceholderAPISupport.replace(title, p, true), layout.lines.size()*9);
		
		int pos = -1; // item pos in menu
		for(String cc : layout.lines) { // each line of layout
			for(char c : cc.toCharArray()) { // each char
				++pos;
				//ItemBuilder builder = layout.getItem(p, c);
				Item builder = layout.getItem(p, c);
				if(builder!=null)
					gui.setItem(pos, new ItemGUI(builder.build(p)) {
						public void onClick(Player player, HolderGUI gui, ClickType click) {
							builder.process(player, gui, click, "default");
						}
					});
			}
		}

		lastUpdate= System.currentTimeMillis()/1000;
	}
}
