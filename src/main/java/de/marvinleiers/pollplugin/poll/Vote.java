package de.marvinleiers.pollplugin.poll;

import org.bukkit.entity.Player;

public class Vote
{
    private final Poll poll;
    private final String option;
    private final Player player;

    public Vote(Poll poll, String option, Player player)
    {
        this.poll = poll;
        this.option = option;
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Poll getPoll()
    {
        return poll;
    }

    public String getOption()
    {
        return option;
    }
}
