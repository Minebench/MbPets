package io.github.apfelcreme.MbPetsNoLD;

import io.github.apfelcreme.MbPetsNoLD.Database.DatabaseConnector;
import io.github.apfelcreme.MbPetsNoLD.Database.HikariCPConnection;
import io.github.apfelcreme.MbPetsNoLD.Database.MySQLConnection;
import io.github.apfelcreme.MbPetsNoLD.Dependencies.WorldGuardAdapter;
import io.github.apfelcreme.MbPetsNoLD.Listener.*;
import io.github.apfelcreme.MbPetsNoLD.Pet.*;

import io.github.apfelcreme.MbPetsNoLD.Tasks.FollowTask;
import io.github.apfelcreme.MbPetsNoLD.Tasks.ParticleTask;
import net.milkbowl.vault.economy.Economy;
import net.zaiyers.AnimalProtect.AnimalProtect;

import net.zaiyers.UUIDDB.bukkit.UUIDDB;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;


/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class MbPets extends JavaPlugin {
    private static MbPets instance;
    /**
     * the event listener for the animal2Pet converting
     */
    private ConvertRightclickListener convertRightclickListener;

    private static DatabaseConnector databaseConnector = null;
    private Economy economy = null;
    private UUIDDB uuiddb;
    private WorldGuardAdapter worldGuard;

    public void onEnable() {
        instance = this;
        //set enabled on plugin load
        // create config if necessary
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        MbPetsConfig.init();

        Permission allPetsPerm = getServer().getPluginManager().getPermission("MbPets.pet.*");
        for (PetType petType : PetType.values()) {
            Permission petPerm = new Permission("MbPets.pet." + petType.name().toLowerCase(), "Allows usage of the " + petType + " pet");
            petPerm.addParent(allPetsPerm, true);
            getServer().getPluginManager().addPermission(petPerm);
        }

        // register commands and listener
        convertRightclickListener = new ConvertRightclickListener();
        getServer().getPluginCommand("pet").setExecutor(new MbPetsCommand());
        getServer().getPluginCommand("pet").setTabCompleter(new MbPetsTabCompleter());
        getServer().getPluginManager().registerEvents(convertRightclickListener, this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityTargetListener(), this);
        getServer().getPluginManager().registerEvents(new EntityTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLogoutListener(), this);
        getServer().getPluginManager().registerEvents(new EntityPickupItemListener(), this);
        getServer().getPluginManager().registerEvents(new EntityRightClickListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamagesEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeashItemClickListener(), this);

        // loadPets the db connection
        getDatabaseConnector().initConnection();

        // get the plugin instances
        if (getServer().getPluginManager().getPlugin("UUIDDB") != null) {
            getLogger().info("Plugin 'UUIDDB' was found!");
            uuiddb = UUIDDB.getInstance();
        }
        if (getServer().getPluginManager().getPlugin("MbAnimalProtect") != null) {
            getLogger().info("Plugin 'MbAnimalProtect' was found!");
        }
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().info("Plugin 'Vault' was not found!");
        } else {
            setupEconomy();
            getServer().getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onServiceRegister(ServiceRegisterEvent event) {
                    if (event.getProvider().getProvider() instanceof Economy) {
                        setupEconomy();
                    }
                }
                @EventHandler
                public void onServiceUnregister(ServiceUnregisterEvent event) {
                    if (event.getProvider().getProvider() instanceof Economy) {
                        setupEconomy();
                    }
                }
            }, this);
        }
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuard = new WorldGuardAdapter();
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        getLogger().info("Using " + economy.getName() + " as Economy provider");
        return economy != null;
    }

    public void onDisable() {
        getDatabaseConnector().close();
        ParticleTask.kill();
        FollowTask.kill();
        for (Pet pet : PetManager.getInstance().getPets().values()) {
            pet.uncall();
        }
    }

    /**
     * returns the plugin instance
     *
     * @return the plugin instance
     */
    public static MbPets getInstance() {
        return instance;
    }

    /**
     * returns the database connection instance
     *
     * @return the database
     */
    public DatabaseConnector getDatabaseConnector() {
        if (databaseConnector == null) {
            if (MbPetsConfig.useHikariCP()) {
                databaseConnector = new HikariCPConnection();
                getLogger().info("HikariCP is active!");
            } else {
                databaseConnector = new MySQLConnection();
                getLogger().info("HikariCP is not active!");
            }
        }
        return databaseConnector;
    }

    /**
     * @return the convertRightclickListener
     */
    public ConvertRightclickListener getConvertRightclickListener() {
        return convertRightclickListener;
    }

    /**
     * @return the animalprotect instance
     */
    public AnimalProtect getPluginAnimalProtect() {
        return (AnimalProtect) MbPets.getInstance().getServer().getPluginManager().getPlugin("MbAnimalProtect");
    }

    /**
     * @return if Vault is enabled and loaded
     */
    public boolean isVaultEnabled() {
        return MbPets.getInstance().getServer().getPluginManager().isPluginEnabled("Vault");
    }

    /**
     * returns the servers Economy
     *
     * @return Vault economy object
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * sends a message to a player
     *
     * @param target  the player
     * @param message the key to the message in the config
     */
    public static void sendMessage(Player target, String message) {
        target.sendMessage(MbPetsConfig.getTextNode("prefix") + message);
    }
    
    /**
     * sends a message to a player
     *
     * @param target  the player's UUID
     * @param message the key to the message in the config
     */
    public static void sendMessage(UUID target, String message) {
        Player player = Bukkit.getPlayer(target);
        if (player != null) {
            sendMessage(player, message);
        }
    }

    public BukkitTask runAsync(Runnable runnable) {
        if (!getServer().isPrimaryThread()) {
            runnable.run();
            return null;
        }
        return getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    public BukkitTask runSync(Runnable runnable) {
        if (getServer().isPrimaryThread()) {
            runnable.run();
            return null;
        }
        return getServer().getScheduler().runTask(this, runnable);
    }

    /**
     * Get the offline player by name
     *
     * @param playerName the player's name
     * @return the offline player or null if not found
     */
    public OfflinePlayer getOfflinePlayer(String playerName) {
        Player player = getServer().getPlayerExact(playerName);
        if (player != null) {
            return player;
        }
        if (uuiddb != null) {
            String uuidString = uuiddb.getStorage().getUUIDByName(playerName, false);
            if (uuidString != null) {
                return getServer().getOfflinePlayer(UUID.fromString(uuidString));
            }
        }
        return null;
    }

    /**
     * Check if the player is in a region where pet spawning is blocked
     *
     * @param player the player
     * @return true if pet spawning is blocked
     */
    public boolean isSpawningBlocked(Player player) {
        if (worldGuard != null && worldGuard.isSpawningBlocked(player)) {
            return true;
        }
        return false;
    }
}
