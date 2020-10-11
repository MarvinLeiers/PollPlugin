package de.marvinleiers.pollplugin;

import de.marvinleiers.pollplugin.commands.AdminCommand;
import de.marvinleiers.pollplugin.poll.Poll;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PollPlugin extends JavaPlugin implements Listener
{
    /*
    TODO: /abstimmen
    TODO: /ergebnisse
    TODO: Wenn frage zu lange -> scrollbarer text
     */
    @Override
    public void onEnable()
    {
        this.getCommand("umfrage").setExecutor(new AdminCommand());
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        if (Poll.getActivePoll() != null)
            Poll.getActivePoll().getBossBar().addPlayer(event.getPlayer());
    }

    public static PollPlugin getPlugin()
    {
        return getPlugin(PollPlugin.class);
    }
}
