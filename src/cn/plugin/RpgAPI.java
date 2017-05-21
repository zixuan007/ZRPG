package cn.plugin;

import cn.nukkit.Player;

/**
 * @author zixuan
 * @version 1.0.0
 *
 *
 */
public interface RpgAPI{
    static final ZRPGMain Instance = ZRPGMain.Instance;
    /**
     * 判断一个玩家是否加入职业
     *
     * @param player,name 玩家,玩家名字
     * @return 玩家加入了职业就返回true,否则返回false
     */
    boolean isJoinjob(Player player);

    boolean isJoinjob(String name);

    /**
     * 获取这个玩家加入了的职业
     *
     * @return 存在这个玩家加入了职业就返回true,否则返回false
     */
    String getJobName(Player player);

    String getJobName(String name);

    /**
     *获取玩家当前的等级
     *
     * @return 获取这个玩家的当前等级，这个玩家没有登录则返回-1
     */
    int getGrade(Player player);


    int getGrade(String name);

    /**
     * 获取玩家当前的经验
     *
     * @return 玩家当前的经验,没有这个玩家则返回-1
     */
    int getExp(Player player);

    int getExp(String name);

    /**
     *玩家加入职业
     *
     * @return 存在这个玩家则返回true,否则返回false
     */
    boolean joinJob(Player player,String JobName);

    boolean joinJob(String name,String JobName);

    /**
     * 给指定玩家添加称号
     *
     * @return 成功返回true,否则返回false
     */

    boolean setPrefix(Player player,String Prefix);

    boolean setPrefix(String name,String Prefix);

    /**
     * 移除指定的玩家称号
     *
     * @return 移除成功返回true,否则返回false;
     */
    boolean removePrefix(Player player,String Prefix);

    boolean removePrefix(String name,String Prefix);

    /**
     * 给玩家添加称号，想要添加称号必须玩家已经设置过称号了
     *
     * @return 添加成功返回true，否则返回false
     */
    boolean addPrefix(Player player,String Prefix);

    boolean addPrefix(String name,String Prefix);

}
