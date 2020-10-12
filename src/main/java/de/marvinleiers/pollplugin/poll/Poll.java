package de.marvinleiers.pollplugin.poll;

import de.marvinleiers.pollplugin.utils.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll
{
    private static Poll activePoll = null;
    public static HashMap<Player, Poll> pendingPolls = new HashMap<>();

    private final List<String> options;
    private List<Vote> votes;
    private final String question;
    private final Player creator;
    private BossBar bossBar;
    private boolean started;

    public Poll(@NotNull String question, Player creator)
    {
        this.options = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.question = question;
        this.creator = creator;
        this.bossBar = null;

        this.started = false;
    }

    public void addOption(@NotNull String option)
    {
        options.add(ChatColor.translateAlternateColorCodes('&', option));
    }

    public void start()
    {
        if (activePoll != null)
        {
            creator.sendMessage("§cEs findet bereits eine Umfrage statt!");
            return;
        }

        if (isValid())
        {
            activePoll = this;
            started = true;
            pendingPolls.remove(creator);

            bossBar = new BossBar(question, this, BarColor.BLUE);
            bossBar.createBossBar();
            bossBar.addAll();

        }
        else
        {
            creator.sendMessage("§cBitte füge zunächst Auswahlmöglichkeiten hinzu!");
        }
    }

    public boolean vote(Vote vote)
    {
        boolean valid = false;

        for (String option : options)
        {
            if (option.equals(vote.getOption()))
            {
                valid = true;
                break;
            }
        }

        if (!valid)
            throw new UnsupportedOperationException(vote.getOption() + " is not valid for poll");

        for (Vote v : votes)
        {
            if (v.getPlayer() == vote.getPlayer())
                return false;
        }

        votes.add(vote);
        return true;
    }

    public void stop()
    {
        Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendMessage("§aUmfrage ist beendet! Bitte nicht mehr abstimmen."));
    }

    private boolean isValid()
    {
        return options.size() >= 1 && !question.isEmpty();
    }

    public boolean hasStarted()
    {
        return started;
    }

    public Player getCreator()
    {
        return creator;
    }

    public BossBar getBossBar()
    {
        return bossBar;
    }

    public List<String> getOptions()
    {
        return options;
    }

    public String getQuestion()
    {
        return question;
    }

    public static void setActivePoll(@Nullable Poll activePoll)
    {
        Poll.activePoll = activePoll;
    }

    public static Poll getActivePoll()
    {
        return activePoll;
    }
}
