package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.PlayerData;

public class ClearConfirmToggle implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
	if(API.hasPerm(s, "ServerControl.ClearInventory")) {
	if(args.length==0) {
		if(s instanceof Player == false) {
			Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"), s);
		return true;
	}else {
			if(API.hasPerm(s,"ServerControl.ClearInv.Clear")) {
				PlayerData d = new PlayerData(s.getName());
			if(d.getBoolean("ClearInvConfirm")==true) {
				d.set("ClearInvConfirm", false);
				PlayerData.save();
				Loader.msg(Loader.s("Prefix")+Loader.s("ClearInventory.ConfirmEnabled"), s);
			return true;
		}else {
			d.set("ClearInvConfirm", true);
			PlayerData.save();
			Loader.msg(Loader.s("Prefix")+Loader.s("ClearInventory.ConfirmDisabled"), s);
			return true;
		}}return true;}}}
		return true;
	}}
