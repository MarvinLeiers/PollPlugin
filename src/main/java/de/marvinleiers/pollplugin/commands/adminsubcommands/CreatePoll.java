package de.marvinleiers.pollplugin.commands.adminsubcommands;

import de.marvinleiers.pollplugin.commands.Subcommand;
import de.marvinleiers.pollplugin.poll.Poll;
import org.bukkit.entity.Player;

public class CreatePoll extends Subcommand
{
    @Override
    public String getName()
    {
        return "create";
    }

    @Override
    public String getDescription()
    {
        return "Erstellt eine Umfrage";
    }

    @Override
    public String getSyntax()
    {
        return "/umfrage create <frage>";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (args.length < 2)
        {
            player.sendMessage("§cUsage: " + getSyntax());
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < args.length; i++)
        {
            stringBuilder.append(args[i]).append(" ");
        }

        String question = stringBuilder.toString().trim();

        Poll poll = new Poll(question, player);
        Poll.pendingPolls.put(player, poll);

        player.sendMessage("§aFast fertig! Erstelle jetzt Antworten mit /umfrage antwort <antwort>");
    }
}
