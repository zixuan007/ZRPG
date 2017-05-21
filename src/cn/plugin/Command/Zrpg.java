package cn.plugin.Command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import cn.plugin.ZRPGMain;

/**
 * Created by Handsomezixuan on 2017/5/15.
 */
public class Zrpg extends Command {
    ZRPGMain main;

    public Zrpg(ZRPGMain main) {
        super("zrpg", "职业总指令", "/zrpg");
        this.main = main;
        getCommandParameters().put("default", new CommandParameter[]{
                new CommandParameter("help", CommandParameter.ARG_TYPE_STRING, true)
        });
        getCommandParameters().put("1", new CommandParameter[]{
                new CommandParameter("add", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("职业名称", CommandParameter.ARG_TYPE_RAW_TEXT, true)
        });
        getCommandParameters().put("2", new CommandParameter[]{
                new CommandParameter("change", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("职业名称", CommandParameter.ARG_TYPE_RAW_TEXT, true)
        });
        getCommandParameters().put("3", new CommandParameter[]{
                new CommandParameter("chat", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("set", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("玩家名称", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("称号", CommandParameter.ARG_TYPE_RAW_TEXT, true)
        });
        getCommandParameters().put("4", new CommandParameter[]{
                new CommandParameter("chat", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("remove", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("玩家名称", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("称号", CommandParameter.ARG_TYPE_RAW_TEXT, true)
        });
        getCommandParameters().put("4", new CommandParameter[]{
                new CommandParameter("chat", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("add", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("玩家名称", CommandParameter.ARG_TYPE_STRING, true),
                new CommandParameter("称号", CommandParameter.ARG_TYPE_RAW_TEXT, true)
        });

    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        String PlayerName = sender.getName();
        if (getName().toLowerCase().equals("zrpg")) {
            if (args.length < 1) {
                sender.sendMessage("/zrpg help >>获取帮助");
                return true;
            }
            switch (args[0]) {
                case "add":
                    if (args.length < 2) {
                        sender.sendMessage("/zrpg add [职业名称] >>加入职业");
                        return true;
                    }
                    if (main.isJoinjob(PlayerName)) {
                        sender.sendMessage("你已经加入了职业无法再次加入");
                        return true;
                    }
                    if (!main.joinJob(PlayerName, args[1])) {
                        sender.sendMessage("没有这个职业请重新加入");
                        return true;
                    }
                    sender.sendMessage("加入成功");
                    return true;
                case "help":
                    sender.sendMessage("/zrpg add [职业名称] >>加入职业");
                    sender.sendMessage("/zrpg change [职业名称] >>转职");
                    return true;
                case "change":
                    if (args.length < 2) {
                        sender.sendMessage("/zrpg change [职业名称] >>转职");
                        return true;
                    }
                    if (!main.isJoinjob(PlayerName)) {
                        sender.sendMessage("请先加入职业，你当前还没加入职业");
                        return true;
                    }
                    String JobName=main.getJobName(PlayerName);
                    if(JobName.equals(args[1])){
                        sender.sendMessage("无法转相同的职业，请重新转职");
                        return true;
                    }
                    main.removePlayerJobFile(PlayerName);
                    main.joinJob(PlayerName, args[1]);
                    main.removeJobItem((Player) sender);
                    sender.sendMessage("给予" + args[1] + "职业专属物品");
                    sender.sendMessage("转职成功,等级经验重0开始");
                    return true;
                case "chat":
                    if (!sender.hasPermission("ZRPGMain.command.zrpg.chat")) {
                        sender.sendMessage("你没有权限");
                        return true;
                    }
                    if (args.length < 2) {
                        sender.sendMessage("/zrpg chat help  >>获取称号权限帮助");
                        return true;
                    }
                    switch (args[1]) {
                        case "set":
                            if (!sender.hasPermission("ZRPGMain.command.zrpg.chat")) {
                                sender.sendMessage("你没有权限");
                                return true;
                            }
                            if (args.length < 4) {
                                sender.sendMessage("/zrpg chat add [玩家名字] [称号]");
                                return true;
                            }
                            if (!main.isJoinjob(args[2])) {
                                sender.sendMessage("玩家" + args[2] + "还没有加入职业,请先加入职业");
                                return true;
                            }
                            if (!main.setPrefix(args[2], args[3])) {
                                sender.sendMessage("玩家" + args[2] + "已经设置了称号,请添加称号");
                            } else {
                                sender.sendMessage("设置称号成功");
                            }
                            return true;

                        case "remove":
                            if (!sender.hasPermission("ZRPGMain.command.zrpg.chat")) {
                                sender.sendMessage("你没有权限");
                                return true;
                            }
                            if (args.length < 4) {
                                sender.sendMessage("/zrpg chat remove [玩家名字] [称号]");
                                return true;
                            }
                            if (!main.isJoinjob(args[2])) {
                                sender.sendMessage("玩家" + args[2] + "还没有加入职业,请先加入职业");
                                return true;
                            }
                            if (!main.removePrefix(args[2], args[3])) {
                                sender.sendMessage("玩家" + args[2] + "还没有称号");
                            } else {
                                sender.sendMessage("成功给玩家:" + args[2] + "移除>>" + args[3] + "称号");
                            }
                            return true;
                        case "add":
                            if (!sender.hasPermission("ZRPGMain.command.zrpg.chat")) {
                                sender.sendMessage("你没有权限");
                                return true;
                            }
                            if (args.length < 4) {
                                sender.sendMessage("/zrpg chat add [游戏名字] [称号]  >>添加称号");
                                return true;
                            }
                            if (!main.isJoinjob(args[2])) {
                                sender.sendMessage("玩家" + args[2] + "还没有加入职业,请先加入职业");
                                return true;
                            }
                            if (!main.existsJobPrefix(args[2])) {
                                sender.sendMessage("玩家" + args[2] + "还没有称号请先设置称号");
                                return true;
                            }
                            if (!main.addPrefix(args[2], args[3])) {
                                sender.sendMessage("玩家" + args[2] + "还没有设置过称号无法添加,或者是添加了相同的称号");
                            } else {
                                sender.sendMessage("成功给玩家" + args[2] + "添加" + args[3] + "称号");
                            }
                            return true;
                    }

            }
        }
        return false;
    }
}
