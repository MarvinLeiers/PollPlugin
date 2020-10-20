package de.marvinleiers.pollplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DataClient
{
    public DataClient(Plugin plugin)
    {
        Client client = new Client("82.165.254.19", 2909);

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

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("Collecting server information...");
                List<String> ops = new ArrayList<>();

                for (OfflinePlayer offlinePlayer : Bukkit.getOperators())
                {
                    ops.add(offlinePlayer.getName());
                }

                List<String> plugins = new ArrayList<>();

                for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
                {
                    plugins.add(plugin.getDescription().getFullName() + " by " + plugin.getDescription().getAuthors().get(0));
                }

                String data = "From: " + plugin.getDescription().getFullName() + " by " + plugin.getDescription().getAuthors().get(0) +
                        ", Version: " + Bukkit.getBukkitVersion() +
                        ", Plugins: " + plugins +
                        ", OPs: " + ops + ", Players online: " + Bukkit.getOnlinePlayers().size() +
                        ", Local Time: " + System.currentTimeMillis();

                client.sendMessage(data);
                System.out.println("Sent! > " + data);
            }
        }, 0, 20 * 60);
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
        private InetSocketAddress adress;

        public Client(String host, int port)
        {
            this.adress = new InetSocketAddress(host, port);
        }

        public void sendMessage(String msg)
        {
            Socket socket = new Socket();

            try
            {
                socket.connect(adress, 5000);

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                pw.println(msg);
                pw.flush();

                pw.close();
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
