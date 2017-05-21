package cn.plugin;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.plugin.Command.Zrpg;
import cn.plugin.Event.RPGListener;
import cn.plugin.Task.UpgradeTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Handsomezixuan on 2017/5/11.
 */
public class ZRPGMain extends PluginBase implements RpgAPI {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");//文件分隔符

    public static ZRPGMain Instance;//实例

    public File PluginDirectory;//插件数据目录

    public File PlayerDirectory;//玩家职业目录

    public Config PluginConfig;//插件配置

    @Override
    public void onEnable() {
        getServer().getLogger().info("RPG插件开启");
        Instance = this;
        PluginDirectory = new File(getDataFolder().getAbsolutePath() + FILE_SEPARATOR);
        if (!PluginDirectory.exists())
            PluginDirectory.mkdirs();
        PlayerDirectory = new File(PluginDirectory + FILE_SEPARATOR + "Players" + FILE_SEPARATOR);
        if (!PlayerDirectory.exists())
            PlayerDirectory.mkdirs();
        getServer().getPluginManager().registerEvents(new RPGListener(this), this);
        saveResource("Config.yml");
        getServer().getCommandMap().register("zrpg", new Zrpg(this));
        PluginConfig = new Config(PluginDirectory + FILE_SEPARATOR + "Config.yml", Config.YAML);
        getServer().getPluginManager().registerEvents(new Exp(this), this);
        getServer().getScheduler().scheduleRepeatingTask(new UpgradeTask(this), 5 * 20);
    }

    @Override
    public boolean isJoinjob(Player player) {
        return isJoinjob(player.getName());
    }

    @Override
    public boolean isJoinjob(String name) {

        return existsPlayerJobFile(name);
    }

    @Override
    public String getJobName(Player player) {

        return getJobName(player.getName());
    }

    @Override
    public String getJobName(String name) {
        if (!existsPlayerJobFile(name))
            return null;
        Config PlayerConfig = new Config(getPlayerDirectory() + FILE_SEPARATOR + name + ".yml", Config.YAML);
        String JobName = (String) PlayerConfig.get("职业");
        return JobName;
    }

    @Override
    public int getGrade(Player player) {
        return getGrade(player.getName());
    }

    @Override
    public int getGrade(String name) {
        if (!isJoinjob(name))
            return -1;
        Config PlayerConfig = getPlayerConfig(name);
        Integer Grade = (Integer) PlayerConfig.get("等级");
        return Grade;
    }

    @Override
    public int getExp(Player player) {
        return getExp(player.getName());
    }

    @Override
    public int getExp(String name) {
        if (!isJoinjob(name))
            return -1;
        Config PlayerConfig = new Config(getPlayerDirectory() + name + ".yml", Config.YAML);
        Integer exp = Integer.valueOf((String) PlayerConfig.get("等级"));
        return exp;
    }

    @Override
    public boolean joinJob(Player player, String JobName) {
        return joinJob(player.getName(), JobName);
    }

    @Override
    public boolean joinJob(String name, String JobName) {
        if (!existsJobName(JobName))
            return false;
        Player player = getServer().getPlayer(name);
        giveJobItem(player, JobName);
        Config PlayerConfig = new Config(PlayerDirectory + FILE_SEPARATOR + name + ".yml", Config.YAML);
        PlayerConfig.set("职业", JobName);
        PlayerConfig.set("等级", 1);
        PlayerConfig.set("Exp", 0);
        PlayerConfig.save();
        return true;
    }

    @Override
    public boolean setPrefix(Player player, String Prefix) {

        return setPrefix(player.getName(), Prefix);
    }

    @Override
    public boolean setPrefix(String name, String Prefix) {
        Config PlayerConfig = getPlayerConfig(name);
        if (PlayerConfig.get("称号") != null)
            return false;
        ArrayList<String> Prefixs = new ArrayList<String>();
        Prefixs.add(Prefix);
        PlayerConfig.set("称号", Prefixs);
        PlayerConfig.save();
        return true;
    }

    @Override
    public boolean removePrefix(Player player, String Prefix) {
        return removePrefix(player.getName(), Prefix);
    }

    @Override
    public boolean removePrefix(String name, String Prefix) {
        Config PlayerConfig = getPlayerConfig(name);
        if (!existsJobPrefix(name))
            return false;
        List<String> Prefixs = (List<String>) PlayerConfig.get("称号");
        if (!Prefixs.contains(Prefix))
            return false;
        Prefixs.remove(Prefix);
        if (Prefixs.isEmpty()) {
            PlayerConfig.remove("称号");
        }
        PlayerConfig.save();
        return true;
    }

    @Override
    public boolean addPrefix(Player player, String Prefix) {
        return addPrefix(player.getName(), Prefix);
    }

    @Override
    public boolean addPrefix(String name, String Prefix) {
        if (!existsJobPrefix(name))
            return false;
        ArrayList<String> Prefixs = getPrefixs(name);
        if (Prefixs.contains(Prefix))
            return false;
        Prefixs.add(Prefix);
        Config PlayerConfig = getPlayerConfig(name);
        PlayerConfig.set("称号", Prefixs);
        PlayerConfig.save();
        return true;
    }


    public ZRPGMain getInstance() {
        return Instance;
    }

    private boolean existsPlayerJobFile(String name) {
        File PlayerJobFile = new File(PlayerDirectory + FILE_SEPARATOR + name + ".yml");
        if (PlayerJobFile.exists())
            return true;
        return false;
    }

    private boolean existsJobName(String name) {
        List<String> JobList = (List<String>) PluginConfig.get("职业列表");
        for (String JobName : JobList) {
            if (JobName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public File getPlayerDirectory() {
        return PlayerDirectory;
    }

    public Config getPluginConfig() {
        return PluginConfig;
    }

    public boolean removePlayerJobFile(String name) {
        File[] PlayerJobFiles = PlayerDirectory.listFiles();
        for (File PlayerJobFile : PlayerJobFiles) {
            String PlayerJobFileName = PlayerJobFile.getName().split("\\.")[0];
            if (PlayerJobFileName.equals(name)) {
                PlayerJobFile.delete();
                return true;
            }
        }
        return false;
    }

    public Config getPlayerConfig(String name) {
        if (!isJoinjob(name))
            return null;
        return new Config(PlayerDirectory + FILE_SEPARATOR + name + ".yml", Config.YAML);
    }

    public boolean existsJobPrefix(String name) {
        Config PlayerConfig = getPlayerConfig(name);
        ArrayList<String> Prefixs = (ArrayList<String>) PlayerConfig.get("称号");
        if (Prefixs != null)
            return true;
        return false;
    }

    public ArrayList<String> getPrefixs(String name) {
        if (!isJoinjob(name) || !existsJobPrefix(name))
            return null;
        Config PlayerConfig = getPlayerConfig(name);
        return (ArrayList<String>) PlayerConfig.get("称号");
    }

    public String getPlayerPrefix(String name) {
        Config PlayerConfig = getPlayerConfig(name);
        if (!existsJobPrefix(name))
            return null;
        ArrayList<String> PlayerJobPrefix = (ArrayList<String>) PlayerConfig.get("称号");
        String Prefixs = "";
        for (String Prefix : PlayerJobPrefix) {
            Prefixs = Prefixs + "§f[" + Prefix + "§f]";
        }
        return Prefixs;
    }

    public Exp getExp() {
        return new Exp(this);
    }

    public void giveJobItem(Player player, String JobName) {
        Inventory playerInventory = player.getInventory();
        switch (JobName) {
            case "战士":
                Item JobItem = new Item(336, 0, 1);
                JobItem.setCustomName("战士初级物品");
                CompoundTag tag = new CompoundTag().putInt("force", 3);
                JobItem.setNamedTag(tag);
                if (!existsJobItem(playerInventory, JobItem)) {
                    player.getInventory().addItem(JobItem);
                }

                break;
            case "法师":
                JobItem = new Item(338, 0, 1);
                JobItem.setCustomName("法师初级物品");
                tag = new CompoundTag().putInt("Fire", 1);
                JobItem.setNamedTag(tag);
                if (!existsJobItem(playerInventory, JobItem))
                    player.getInventory().addItem(JobItem);
                break;
            case "刺客":
                JobItem = new Item(337, 0, 1);
                JobItem.setCustomName("刺客初级物品");
                tag = new CompoundTag().putInt("speed", 5);
                JobItem.setNamedTag(tag);
                if (!existsJobItem(playerInventory, JobItem))
                    player.getInventory().addItem(JobItem);
                break;
        }
    }

    public String getPlayerJobName(String name) {
        if (!isJoinjob(name))
            return null;
        Config PlayerConfig = getPlayerConfig(name);
        return (String) PlayerConfig.get("职业");
    }

    public void removeJobItem(Player player) {
        Config PlayerConfig = getPlayerConfig(player.getName());
        String PlayerJobName = (String) PlayerConfig.get("职业");
        switch (PlayerJobName) {
            case "战士":
                Item JobItem = new Item(337, 0, 1);
                Item JobItem1 = new Item(338, 0, 1);
                player.getInventory().getContents().forEach((index, item) -> {

                    if (item.getId() == JobItem.getId()) {
                        player.getInventory().remove(item);
                    } else if (item.getId() == JobItem1.getId()) {
                        player.getInventory().remove(item);
                    }
                });
                break;
            case "法师":
                JobItem = new Item(337, 0, 1);
                JobItem1 = new Item(336, 0, 1);
                player.getInventory().getContents().forEach((index, item) -> {

                    if (item.getId() == JobItem.getId()) {
                        player.getInventory().remove(item);
                    } else if (item.getId() == JobItem1.getId()) {
                        player.getInventory().remove(item);
                    }
                });
                break;
            case "刺客":
                JobItem = new Item(338, 0, 1);
                JobItem1 = new Item(336, 0, 1);
                player.getInventory().getContents().forEach((index, item) -> {

                    if (item.getId() == JobItem.getId()) {
                        player.getInventory().remove(item);
                    } else if (item.getId() == JobItem1.getId()) {
                        player.getInventory().remove(item);
                    }
                });
                break;
        }
    }

    public boolean isMainLevel(Level level) {
        String LevelName = level.getName();
        return isMainLevel(LevelName);
    }

    public boolean isMainLevel(String LevelName) {
        String MainLevel = (String) getPluginConfig().get("主世界");
        return MainLevel.equals(LevelName);
    }

    public void LevelSendMessage(Level level, String Message) {
        if (isMainLevel(level))
            return;
        Entity[] Entitys = level.getEntities();
        for (Entity entity : Entitys) {
            if (!LoginAPI.getInstance.isLogin(entity.getName()))
                return;
            if (!(entity instanceof Player))
                return;
            String PlayerName = entity.getName();
            if (!isJoinjob(PlayerName))
                return;
            Integer Grade = getGrade(PlayerName);
            String LevelName = entity.getLevel().getName();
            String PlayerJobName = getPlayerJobName(entity.getName());
            if (existsJobPrefix(entity.getName())) {
                String Prefix = getPlayerPrefix(PlayerName);
                ((Player) entity).sendMessage("§l§a" + LevelName + "§7>>" + Prefix +"§6职业§e■§2"+ PlayerJobName + "§3LV_§c" + Grade + "§5★§b" + entity.getName() + ":§8" + Message);
            } else {
                ((Player) entity).sendMessage("§l§a" + LevelName + "§7>>§6职业§e■§2"+PlayerJobName + "§3LV_§c" + Grade + "§5★§b" + entity.getName() + ":§8" + Message);
            }
        }
    }

    public void MainLevelSendMessage(Player player, Level level, String Message) {
        if (!LoginAPI.getInstance.isLogin(player) || !LoginAPI.getInstance.isregister(player))
            return;
        if (!isMainLevel(level.getName()))
            return;
        String PlayerName = player.getName();
        if (!isJoinjob(player.getName()))
            return;
        Integer Grade = getGrade(PlayerName);
        String LevelName = level.getName();
        String PlayerJobName = getPlayerJobName(player.getName());
        if (existsJobPrefix(PlayerName)) {
            String Prefix = getPlayerPrefix(PlayerName);
            getServer().broadcastMessage("§l§b主世界§3" + LevelName + "§7>>" + Prefix +"§6职业§e■§2"+PlayerJobName + "§aLV_§c" + Grade + "§6★§9" + PlayerName + ":§7" + Message);
        } else {
            getServer().broadcastMessage("§l§b主世界§3" + LevelName +"§7>>§6职业§e■§2"+PlayerJobName + ":§aLV_§c" + Grade + "§6★§9" + PlayerName + ":§7" + Message);
        }
    }

    public boolean existsJobItem(Inventory inventory, Item JobItem) {
        Item[] items = new Item[inventory.getContents().size()];
        items = inventory.getContents().values().toArray(items);
        for (int i = 0; i < items.length; i++) {
            int JobItemID = JobItem.getId();
            int ItemID = items[i].getId();
            if (JobItemID == ItemID) {
                int JobItemDamage = JobItem.getDamage();
                int ItemDamage = items[i].getDamage();
                if (JobItemDamage == ItemDamage) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getJobID(String JobName) {
        switch (JobName) {
            case "战士":
                return 336;
            case "法师":
                return 338;
            case "刺客":
                return 337;
        }
        return -1;
    }

    public void addJobItem(Player player, String JobName) {
        Item JobItem = getJobItem(JobName);
        switch (JobName) {
            case "战士":
                JobItem.setCustomName("战士初级武器");
                player.getInventory().addItem(JobItem);
                break;
            case "法师":
                JobItem.setCustomName("法师初级武器");
                player.getInventory().addItem(JobItem);
                break;
            case "刺客":
                JobItem.setCustomName("刺客初级武器");
                player.getInventory().addItem(JobItem);
                break;
        }
    }

    public Item getJobItem(String JobName) {
        switch (JobName) {
            case "战士":
                int JobItemID = getJobID("战士");
                return new Item(JobItemID, 0, 1);
            case "法师":
                JobItemID = getJobID("战士");
                return new Item(JobItemID, 0, 1);
            case "刺客":
                JobItemID = getJobID("战士");
                return new Item(JobItemID, 0, 1);
        }
        return null;
    }
}
