package de.marvinleiers.pollplugin.commands;

import de.marvinleiers.pollplugin.commands.adminsubcommands.AddOption;
import de.marvinleiers.pollplugin.commands.adminsubcommands.CreatePoll;
import de.marvinleiers.pollplugin.commands.adminsubcommands.StartPoll;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements CommandExecutor
{
    List<Subcommand> subcommands = new ArrayList<>();

    public AdminCommand()
    {
        subcommands.add(new CreatePoll());
        subcommands.add(new AddOption());
        subcommands.add(new StartPoll());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cOnly for players!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1)
        {
            for (Subcommand subcommand : subcommands)
                player.sendMessage("§7" + subcommand.getSyntax() + " §e- §7" + subcommand.getDescription());

            return true;
        }

        for (Subcommand subcommand : subcommands)
        {
            if (subcommand.getName().equalsIgnoreCase(args[0]))
            {
                subcommand.execute(player, args);
                return true;
            }
        }

        for (Subcommand subcommand : subcommands)
            player.sendMessage("§7" + subcommand.getSyntax() + " §e- §7" + subcommand.getDescription());

        return true;
    }
}
