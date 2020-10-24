package de.marvinleiers.pollplugin.poll;

import de.marvinleiers.pollplugin.utils.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean resultReady;

    public Poll(@NotNull String question, Player creator)
    {
        this.options = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.question = question;
        this.creator = creator;
        this.bossBar = null;

        this.started = false;
        this.resultReady = false;
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
        this.calculateVotes();
    }

    private void calculateVotes()
    {
        int[] res = new int[options.size()];

        for (Vote vote : votes)
            res[getOptionPosition(vote)] += 1;

        creator.playSound(creator.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        creator.sendMessage("§9" + getQuestion());

        for (int i = 0; i < res.length; i++)
            creator.sendMessage("§7- " + options.get(i) + " §b§l" + new DecimalFormat("##").format((res[i] / votes.size() * 100)) + "% §b(" + res[i] + ")");
    }

    private int getOptionPosition(Vote vote)
    {
        for (int i = 0; i < options.size(); i++)
        {
            if (options.get(i).equals(vote.getOption()))
                return i;
        }

        return -1;
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
