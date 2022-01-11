package me.devtec.scr.commands.other.control;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.CommandHolder;
import me.devtec.scr.commands.other.control.ControlPanel.StopType;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;

/**
 * @author StraikerinaCZ
 * 10.1 2022
 **/
public class Reload extends CommandHolder {
	
	public Reload(String command) {
		super(command, -1);
	}

	@Override
	public void command(CommandSender s, String[] args, boolean loop, boolean silent) {
		if(args.length==0) {
			ControlPanel.start(StopType.RELOAD, (int)StringUtils.calculate(PlaceholderAPI.setPlaceholders(null, Loader.config.getString("control-panel.reload.default-time"))));
			return;
		}
		if(args[0].equalsIgnoreCase("cancel")) {
			ControlPanel.cancel();
			return;
		}
		int time = (int)StringUtils.calculate(args[0]);
		if(time <= 0)
			ControlPanel.skipTime();
		else
			ControlPanel.start(StopType.RELOAD, time);
	}
	
	@Override
	public int[] playerPlaceholders(CommandSender s, String[] args) {
		return null;
	}
	
}