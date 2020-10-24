package de.marvinleiers.pollplugin.commands;

import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.pollplugin.menus.VoteMenu;
import de.marvinleiers.pollplugin.poll.Poll;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cnur für Spieler!");
            return true;
        }

        Player player = (Player) sender;

        if (Poll.getActivePoll() == null)
        {
            player.sendMessage("§cZurzeit findet keine Umfrage statt!");
            return true;
        }

        Poll poll = Poll.getActivePoll();

        new VoteMenu(poll, MenuAPI.getMenuUserInformation(player)).open();

        return true;
    }
}
