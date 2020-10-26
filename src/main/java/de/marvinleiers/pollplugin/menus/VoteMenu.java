package de.marvinleiers.pollplugin.menus;

import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import de.marvinleiers.pollplugin.PollPlugin;
import de.marvinleiers.pollplugin.poll.Poll;
import de.marvinleiers.pollplugin.poll.Vote;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class VoteMenu extends Menu
{
    private Poll poll;

    public VoteMenu(@NotNull Poll poll, MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);

        this.poll = poll;
    }

    @Override
    public String getTitle()
    {
        return poll.getQuestion();
    }

    @Override
    public int getSlots()
    {
        return 9;
    }

    @Override
    public void setItems()
    {
        for (String option : poll.getOptions())
        {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(option);
            item.setItemMeta(meta);

            inventory.addItem(item);
        }
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {
        ItemStack item = inventoryClickEvent.getCurrentItem();

        if (item == null)
            return;

        Vote vote = new Vote(poll, item.getItemMeta().getDisplayName(), player);

        boolean result = false;

        try
        {
            result = poll.vote(vote);
        }
        catch (UnsupportedOperationException e)
        {
            player.sendMessage("§cGlückwunsch, du hast einen Bug gefunden! Melde diesen bitte an das Serverteam (Plugin: §4§lPollPlugin by Marvin2k0)");
            PollPlugin.err.put(System.currentTimeMillis(), e.getMessage() + " -(-)- " + player.getUniqueId().toString());
            return;
        }

        if (result)
            player.sendMessage("§aDu hast erfolgreich abgestimmt!");
        else
            player.sendMessage("§cHuch... da ist etwas schief gegangen!");

        player.closeInventory();
    }
}
