package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;

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
			if(Loader.me.getBoolean("Players."+s.getName()+".ClearInvConfirm")==true) {
				Loader.me.set("Players."+s.getName()+".ClearInvConfirm", false);
				Configs.chatme.save();Loader.msg(Loader.s("Prefix")+Loader.s("ClearInventory.ConfirmEnabled"), s);
			return true;
		}else {
			Loader.me.set("Players."+s.getName()+".ClearInvConfirm", true);
			Configs.chatme.save();Loader.msg(Loader.s("Prefix")+Loader.s("ClearInventory.ConfirmDisabled"), s);
			return true;
		}}return true;}}}
		return true;
	}}
