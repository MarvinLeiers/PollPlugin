package de.marvinleiers.pollplugin.commands.adminsubcommands;

import de.marvinleiers.pollplugin.commands.Subcommand;
import de.marvinleiers.pollplugin.poll.Poll;
import org.bukkit.entity.Player;

public class AddOption extends Subcommand
{
    @Override
    public String getName()
    {
        return "antwort";
    }

    @Override
    public String getDescription()
    {
        return "Füge eine Antwortmöglichkeit zu deiner Umfrage hinzu";
    }

    @Override
    public String getSyntax()
    {
        return "/umfrage antwort <antwort>";
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

        String option = stringBuilder.toString().trim();
        poll.addOption(option);

        player.sendMessage("§aAntwortmöglichkeit hinzugefügt! Füge weitere hinzu oder starte die Umfrage mit §2/umfrage start");
    }
}
