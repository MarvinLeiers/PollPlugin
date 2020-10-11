package de.marvinleiers.pollplugin.commands.adminsubcommands;

import de.marvinleiers.pollplugin.commands.Subcommand;
import de.marvinleiers.pollplugin.poll.Poll;
import org.bukkit.entity.Player;

public class StartPoll extends Subcommand
{
    @Override
    public String getName()
    {
        return "start";
    }

    @Override
    public String getDescription()
    {
        return "Veröffentliche deine fertige Umfrage";
    }

    @Override
    public String getSyntax()
    {
        return "/umfrage start";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (!Poll.pendingPolls.containsKey(player))
        {
            player.sendMessage("§cErstelle zuerst eine Umfrage mit /umfrage create");
            return;
        }

        Poll poll = Poll.pendingPolls.get(player);
        poll.start();
    }
}
