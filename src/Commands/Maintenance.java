package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;

public class Maintenance implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {

		if(args.length ==0) {
			if(API.hasPerm(s, "ServerControl.Maintenance")) {
	        if(setting.lock_server) {
	            Loader.config.set("Options.Maintenance.Enabled", false);
	            setting.lock_server=false;
        Loader.msg(Loader.s("Prefix")+"&e----------------- &bMaintenance is Disabled &e-----------------",s);
        Loader.msg("",s);
        Loader.msg(Loader.s("Prefix")+Loader.s("MaintenanceMode.TurnOff"),s);
        return true;
     }

         Loader.config.set("Options.Maintenance.Enabled", true);
         setting.lock_server=true;
         Loader.msg(Loader.s("Prefix")+"&e----------------- &bMaintenance is Enabled &e-----------------",s);
         Loader.msg("",s);
         Loader.msg(Loader.s("Prefix")+Loader.s("MaintenanceMode.TurnOn"),s);
         return true;
    		}
			return true;
    		}
		return false;
    		}}
