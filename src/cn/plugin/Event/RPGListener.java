package cn.plugin.Event;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import cn.plugin.LoginAPI;
import cn.plugin.ZRPGMain;

import java.util.ArrayList;

/**
 * Created by Handsomezixuan on 2017/5/15.
 */
public class RPGListener implements Listener {
    public ZRPGMain main;

    public RPGListener(ZRPGMain main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!main.isJoinjob(player.getName())) {
            player.sendMessage("请输入/zrpg 职业");
            player.sendMessage("加入职业吧");
            return;
        }
        if (main.isJoinjob(player.getName())) {
            Inventory inventory = player.getInventory();
            String PlayerJobName = main.getJobName(player.getName());
            Item item = main.getJobItem(PlayerJobName);
            if (!main.existsJobItem(inventory, item)) {
                main.addJobItem(player,PlayerJobName);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (!main.isJoinjob(player.getName())) {
            player.sendTitle("§4请先加入职业");
            return;
        }
        Level level = player.getLevel();
        if (main.isMainLevel(level)) {
            main.MainLevelSendMessage(player, level, event.getMessage());
        } else {
            main.LevelSendMessage(level, event.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDropItem(PlayerDropItemEvent event) {
        if (!LoginAPI.getInstance.isLogin(event.getPlayer()))
            return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        Item DropItem = event.getItem();
        player.sendMessage("禁止丢弃物品");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!LoginAPI.getInstance.isLogin(player))
            return;
        event.setKeepInventory(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity killer = ((EntityDamageByEntityEvent) event).getDamager();
            Entity entity = event.getEntity();
            if (killer instanceof Player) {
                Item JobItem = ((Player) killer).getInventory().getItemInHand();
                switch (((Player) killer).getInventory().getItemInHand().getId()) {
                    case 336:
                        int force = JobItem.getNamedTag().getInt("force");
                        Effect effect = Effect.getEffect(5);
                        effect.setDuration(force * 20).setAmplifier(1).setAmbient(true);
                        effect.add(killer);
                        break;
                    case 337:
                        effect = Effect.getEffect(1);
                        int time = JobItem.getNamedTag().getInt("speed");
                        effect.setDuration(time * 20).setAmplifier(1).setAmbient(true);
                        effect.add(killer);
                        break;
                    case 338:
                        entity.setOnFire(JobItem.getNamedTag().getInt("Fire"));
                        break;
                }
            }
        }
    }
}
