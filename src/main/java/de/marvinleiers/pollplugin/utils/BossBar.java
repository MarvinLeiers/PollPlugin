package de.marvinleiers.pollplugin.utils;

import de.marvinleiers.pollplugin.PollPlugin;
import de.marvinleiers.pollplugin.poll.Poll;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class BossBar
{
    private static final int DURATION_IN_SECONDS = 60;

    private org.bukkit.boss.BossBar bossBar;
    private BarColor color;
    private String title;
    private Poll poll;
    private int taskID;

    public BossBar(String title, Poll poll, BarColor color)
    {
        org.bukkit.boss.BossBar bossBar;
        this.title = title;
        this.color = color;
        this.poll = poll;
        this.taskID = -1;
    }

    public void createBossBar()
    {
        bossBar = Bukkit.createBossBar(format(title), color, BarStyle.SOLID);
        bossBar.setVisible(true);

        start();
    }

    private void start()
    {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(PollPlugin.getPlugin(), new Runnable()
        {
            double progress = 1;
            double time =  1 / (double) (DURATION_IN_SECONDS * 20);

            @Override
            public void run()
            {
                bossBar.setProgress(progress);

                progress -= time;

                if (progress <= 0)
                {
                    stop();
                }
            }
        }, 0, 0);
    }

    public void stop()
    {
        poll.stop();
        Poll.setActivePoll(null);
        bossBar.setVisible(false);
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void addAll()
    {
        Bukkit.getOnlinePlayers().stream().forEach(this::addPlayer);
    }

    public void addPlayer(Player player)
    {
        bossBar.addPlayer(player);
    }

    public org.bukkit.boss.BossBar getBossBar()
    {
        return bossBar;
    }

    private static String format(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
