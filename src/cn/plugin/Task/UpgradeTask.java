package cn.plugin.Task;

import cn.nukkit.scheduler.PluginTask;
import cn.plugin.ZRPGMain;

/**
 * Created by Handsomezixuan on 2017/5/17.
 */
public class UpgradeTask extends PluginTask<ZRPGMain> {
    public ZRPGMain main;

    public UpgradeTask(ZRPGMain main) {
        super(main);
        this.main = main;
    }

    @Override
    public void onRun(int i) {
        main.getServer().getOnlinePlayers().forEach(((uuid, player) -> {
            String name = player.getName();
            main.getExp().upGrade(name);
        }));
    }
}
