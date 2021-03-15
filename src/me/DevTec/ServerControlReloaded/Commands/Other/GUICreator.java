package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.utils.StringUtils;

public class GUICreator implements CommandExecutor {
    private Config c = Loader.guicreator;
    private String a;

    public GUICreator(String a){
        this.a=a;
    }

    public boolean onCommand(CommandSender s, Command uu, String u, String[] args) {
        if(!(s instanceof Player)){
            return true;
        }
        udelator(a,(Player)s,null);
        return true;
    }
    public void udelator(String b,Player s,String u){
        GUI gui = new GUI(c.getString("GUI."+b+".title"),c.getInt("GUI."+b+".size"));
        ItemGUI itemGUI;
        for(String j : c.getKeys("GUI."+b+".items")){
            if(u==null){
                if(c.exists("GUI."+b + ".items."+j+".lore")){
                    itemGUI=new ItemGUI(ItemCreatorAPI.create(Material.getMaterial(c.getString("GUI."+b+".items."+j+".type").toUpperCase()),1,c.getString("GUI."+b+".items."+j+".name"),c.getStringList("GUI."+b + ".items."+j+".lore"))) {
                        @Override
                        public void onClick(Player player, HolderGUI holderGUI, GUI.ClickType clickType) {
                            vecinator(gui,player, a,j,clickType);
                        }
                    };
                }else{
                    itemGUI=new ItemGUI(ItemCreatorAPI.create(Material.getMaterial(c.getString("GUI."+b+".items."+j+".type").toUpperCase()),1,c.getString("GUI."+b+".items."+j+".name"))) {
                        @Override
                        public void onClick(Player player, HolderGUI holderGUI, GUI.ClickType clickType) {
                            vecinator(gui,player, a,j,clickType);
                        }
                    };
                }
            } else {
                if(c.exists("GUI."+b + ".items."+j+".lore")){
                    itemGUI=new ItemGUI(ItemCreatorAPI.create(Material.getMaterial(c.getString("GUI."+b+".items."+j+".type").toUpperCase()),1,c.getString("GUI."+b+".items."+j+".name"),c.getStringList("GUI."+b + ".items."+j+".lore"))) {
                        @Override
                        public void onClick(Player player, HolderGUI holderGUI, GUI.ClickType clickType) {
                            vecinator(gui,player, u,j,clickType);
                        }
                    };
                }else{
                    itemGUI=new ItemGUI(ItemCreatorAPI.create(Material.getMaterial(c.getString("GUI."+b+".items."+j+".type").toUpperCase()),1,c.getString("GUI."+b+".items."+j+".name"))) {
                        @Override
                        public void onClick(Player player, HolderGUI holderGUI, GUI.ClickType clickType) {
                            vecinator(gui,player, u,j,clickType);
                        }
                    };
                }
            }
            for(String slot : j.split(",[ ]*"))
                gui.setItem(StringUtils.getInt(slot), itemGUI);
        }
        gui.open(s);
    }

    public void vecinator(GUI g ,Player p, String b, String s, GUI.ClickType clickType){
        if(EconomyAPI.has(p,c.getDouble("GUI."+b+".items."+s+".cost"))) {
            TheAPI.bcMsg("mám");
            if(c.getBoolean("GUI."+b+".items."+s+".takeMoney")){
                EconomyAPI.withdrawPlayer(p,c.getDouble("GUI."+b+".items."+s+".cost"));
            }
            if (c.get("GUI." + b + ".items." + s + ".action") instanceof List) {
                for (String a : c.getStringList("GUI." + b + ".items." + s + ".action")) {
                    if (a.startsWith("sleft")) {
                        if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("sright")) {
                        if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(7));
                        }
                    } else if (a.startsWith("left")) {
                        if (clickType == GUI.ClickType.LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(5));
                        }
                    } else if (a.startsWith("right")) {
                        if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("any")) {
                        methodenzi(g, p, a.substring(4));
                    }
                }
            } else {
                String a = c.getString("GUI." + b + ".items." + s + ".action");
                if (a.startsWith("sleft")) {
                    if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("sright")) {
                    if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(7));
                    }
                } else if (a.startsWith("left")) {
                    if (clickType == GUI.ClickType.LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(5));
                    }
                } else if (a.startsWith("right")) {
                    if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("any")) {
                    methodenzi(g, p, a.substring(4));
                }
            }
        }else{
            if (c.get("GUI." + b + ".items." + s + ".noMoney") instanceof List) {
                TheAPI.bcMsg("nemám");
                for (String a : c.getStringList("GUI." + b + ".items." + s + ".noMoney")) {
                    if (a.startsWith("sleft")) {
                        if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("sright")) {
                        if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(7));
                        }
                    } else if (a.startsWith("left")) {
                        if (clickType == GUI.ClickType.LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(5));
                        }
                    } else if (a.startsWith("right")) {
                        if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("any")) {
                        methodenzi(g, p, a.substring(4));
                    }
                }
            } else {
                TheAPI.bcMsg("nemám");
                String a = c.getString("GUI." + b + ".items." + s + ".noMoney");
                if (a.startsWith("sleft")) {
                    if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("sright")) {
                    if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(7));
                    }
                } else if (a.startsWith("left")) {
                    if (clickType == GUI.ClickType.LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(5));
                    }
                } else if (a.startsWith("right")) {
                    if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("any")) {
                    methodenzi(g, p, a.substring(4));
                }
            }
        }
    }

    public void methodenzi(GUI g ,Player p,String a){
        if(a.startsWith("close")){
            g.close(p);
        } else if (a.startsWith("msg")){
            TheAPI.msg(a.replace("%player%",p.getName()).substring(4),p);
        } else if(a.startsWith("cmd")){
            if(a.substring(4).startsWith("player")){
            	TheAPI.sudo(p,SudoType.COMMAND,a.substring(11).replace("%player%",p.getName()));
            } else if(a.substring(4).startsWith("console")){
                TheAPI.sudoConsole(a.substring(12).replace("%player%",p.getName()));
            }
        } else if(a.startsWith("open")){
            udelator(a.substring(5),p,a.substring(5));
        }
    }
}
