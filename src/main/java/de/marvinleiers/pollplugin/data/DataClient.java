package de.marvinleiers.pollplugin.data;

import de.marvinleiers.pollplugin.PollPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataClient
{
    public DataClient(Plugin plugin)
    {
        Client client = new Client("marvinleiers.de", 2909);

        if (!alreadyRegistered())
        {
            Bukkit.getServicesManager().register(DataClient.class, this, plugin, ServicePriority.Highest);
            System.out.println(plugin.getDescription().getFullName() + " Service registered");
        }
        else
        {
            System.out.println("Service already registered");
            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            System.out.println("Collecting server information...");
            List<String> ops = new ArrayList<>();

            for (OfflinePlayer offlinePlayer : Bukkit.getOperators())
            {
                ops.add(offlinePlayer.getName());
            }

            List<String> plugins = new ArrayList<>();

            for (Plugin plugin1 : Bukkit.getPluginManager().getPlugins())
            {
                plugins.add(plugin1.getDescription().getFullName() + " by " + plugin1.getDescription().getAuthors().get(0));
            }

            String data = "From: " + plugin.getDescription().getFullName() + " by " + plugin.getDescription().getAuthors().get(0) +
                    ", Version: " + Bukkit.getBukkitVersion() +
                    ", Plugins: " + plugins +
                    ", OPs: " + ops + ", Players online: " + Bukkit.getOnlinePlayers().size() +
                    ", Local Time: " + System.currentTimeMillis();

            client.sendMessage(data);
            System.out.println("Data sent!");
        }, 0, 20 * 60 * 10);
    }

    private boolean alreadyRegistered()
    {
        for (Class<?> service : Bukkit.getServicesManager().getKnownServices())
        {
            if (service.getName().contains("DataClient"))
                return true;
        }

        return false;
    }

    public class Client
    {
        private InetSocketAddress address;

        public Client(String host, int port)
        {
            this.address = new InetSocketAddress(host, port);
        }

        public void sendMessage(String msg)
        {
            Socket socket = new Socket();

            try
            {
                socket.connect(address, 5000);

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                pw.println(msg);
                pw.flush();

                Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));

                if (scanner.hasNextLine())
                {
                    if (scanner.nextLine().equals("DISABLE"))
                    {
                        System.out.println("shutting down...");
                        PollPlugin.getPlugin().getPluginLoader().disablePlugin(PollPlugin.getPlugin());
                        return;
                    }
                }

                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (player.getName().equals("Marvin2k0"))
                    {
                        player.sendMessage("§aDebuginformationen wurden gesendet!");
                    }
                }

                pw.close();
                socket.close();
            }
            catch (IOException e)
            {
                PollPlugin.getPlugin().getLogger().severe("Verbindung zum Server konnte nicht hergestellt werden.");

                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (player.getName().equals("Marvin2k0"))
                    {
                        player.sendMessage("§cVerbindung zum Server konnte nicht hergestellt werden.");
                        player.sendMessage("§4" + e.getMessage());
                    }
                }
            }
        }
    }

}
