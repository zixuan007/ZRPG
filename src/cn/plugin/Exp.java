package cn.plugin;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;


/**
 * Created by Handsomezixuan on 2017/5/17.
 */
public class Exp implements Listener {
    private ZRPGMain main;

    public Exp(ZRPGMain main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        Entity DeathEntity = event.getEntity();
        EntityDamageEvent cause = DeathEntity.getLastDamageCause();
        if (cause instanceof EntityDamageByEntityEvent) {
            Entity killer = ((EntityDamageByEntityEvent) cause).getDamager();
            Entity killEntity = cause.getEntity();
            if(killer.getName().equals(killEntity.getName()))
                return;
            if (main.isJoinjob(killer.getName())) {
                Config PlayerConfig = main.getPlayerConfig(killer.getName());
                Integer Grade = (Integer) PlayerConfig.get("等级");
                switch (Grade / 10) {
                    case 0:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 1:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 2:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 3:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 4:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 5:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 6:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 7:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                    case 8:
                        PlayerConfig.set("Exp", ((Integer) PlayerConfig.get("Exp")) + 100);
                        PlayerConfig.save();
                        break;
                }

                for (Entity entity : killer.getLevel().getEntities()) {
                    if (entity instanceof Player) {
                        ((Player) entity).sendMessage(DeathEntity.getName() + "被" + killer.getName() + "杀死");
                    }
                }
            }
        }

    }

    public void upGrade(String name) {
        if (!main.isJoinjob(name))
            return;
        Config PlayerConfig = main.getPlayerConfig(name);
        Integer Grade = (Integer) PlayerConfig.get("等级");
        Player player = main.getServer().getPlayer(name);
        Integer Exp = (Integer) PlayerConfig.get("Exp");
        switch (Grade / 10) {
            case 0:
                setGradeConfig(name, PlayerConfig, player);

                break;
            case 1:
                setGradeConfig(name, PlayerConfig, player);
                break;
            case 2:
                setGradeConfig(name, PlayerConfig, player);
                break;
            case 3:
                setGradeConfig(name, PlayerConfig, player);
                break;
            case 4:
                setGradeConfig(name, PlayerConfig, player);
                break;
            case 5:
                setGradeConfig(name, PlayerConfig, player);
                break;
            case 6:
                setGradeConfig(name, PlayerConfig, player);
                break;
        }

    }

    private void setJobPrefix(Config PlayerConfig) {
        PlayerConfig.set("Prefix", String.valueOf(PlayerConfig.get("等级")));
        PlayerConfig.save();
    }

    private String getGradeKey(String name) {
        Config PlayerConfig = main.getPlayerConfig(name);
        Integer Grad = (Integer) PlayerConfig.get("等级");
        switch (Grad / 10) {
            case 0:
                return "10级";
            case 1:
                return "20级";
            case 2:
                return "30级";
            case 3:
                return "40级";
            case 4:
                return "50级";
            case 5:
                return "60级";
            case 6:
                return "70级";
            case 7:
                return "80级";
            case 8:
                return "90级";
        }
        return null;
    }

    private void setGradeConfig(String name, Config PlayerConfig, Player player) {
        Integer ConfigExp = (Integer) main.getPluginConfig().get(getGradeKey(name));
        Integer Exp = (Integer) PlayerConfig.get("Exp");
        Integer Grade = (Integer) PlayerConfig.get("等级");
        if (Exp >= ConfigExp) {
            PlayerConfig.set("等级", Grade + 1);
            PlayerConfig.set("Exp", Exp - ConfigExp);
            PlayerConfig.save();
            player.sendMessage("恭喜你升级到" + ((Integer) PlayerConfig.get("等级") + "级"));
        }
    }

    public boolean addExp(Player player,int Exp){

        return false;
    }
}
