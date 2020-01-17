package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

public class Maintenance implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {

		if(args.length ==0) {
			if(API.hasPerm(s, "ServerControl.Maintenance")) {
	        if(Loader.config.getBoolean("MaintenanceMode.Enabled")== true) {
        Loader.config.set("MaintenanceMode.Enabled", false);
        Configs.config.save(); Loader.msg(Loader.s("Prefix")+"&e----------------- &bOff &e-----------------",s);
        Loader.msg("",s);
        Loader.msg(Loader.s("Prefix")+Loader.s("MaintenanceMode.TurnOff"),s);
        return true;
     }else
         if(Loader.config.getBoolean("MaintenanceMode.Enabled")== false) {

         Loader.config.set("MaintenanceMode.Enabled", true);
         Configs.config.save(); Loader.msg(Loader.s("Prefix")+"&e----------------- &bOn &e-----------------",s);
         Loader.msg("",s);
         Loader.msg(Loader.s("Prefix")+Loader.s("MaintenanceMode.TurnOn"),s);
         
         return true;
    		}}return true;}return false;}}
