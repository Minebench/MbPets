package io.github.apfelcreme.MbPetsNoLD;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import io.github.apfelcreme.MbPetsNoLD.Database.DatabaseConnector;
import io.github.apfelcreme.MbPetsNoLD.Database.HikariCPConnection;
import io.github.apfelcreme.MbPetsNoLD.Database.MySQLConnection;
import io.github.apfelcreme.MbPetsNoLD.Listener.*;
import io.github.apfelcreme.MbPetsNoLD.Pet.*;

import java.sql.SQLException;

import io.github.apfelcreme.MbPetsNoLD.Tasks.FollowTask;
import io.github.apfelcreme.MbPetsNoLD.Tasks.ParticleTask;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.zaiyers.AnimalProtect.AnimalProtect;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


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

    /**
     * the event listener for the animal2Pet converting
     */
    private ConvertRightclickListener convertRightclickListener;

    private static DatabaseConnector databaseConnector = null;

    public void onEnable() {
        //set enabled on plugin load
        // create config if necessary
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        MbPetsConfig.init();

        // register commands and listener
        convertRightclickListener = new ConvertRightclickListener();
        getServer().getPluginCommand("pet").setExecutor(new MbPetsCommand());
        getServer().getPluginCommand("pet").setTabCompleter(
                new MbPetsTabCompleter());
        getServer().getPluginManager().registerEvents(
                convertRightclickListener, this);
        getServer().getPluginManager().registerEvents(
                new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(
                new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(
                new EntityTeleportListener(), this);
        getServer().getPluginManager().registerEvents(
                new PlayerWorldChangeListener(), this);
        getServer().getPluginManager().registerEvents(
                new PlayerLogoutListener(), this);
        getServer().getPluginManager().registerEvents(
                new EntityRightClickListener(), this);
        getServer().getPluginManager().registerEvents(
                new EntityDamagesEntityListener(), this);
        getServer().getPluginManager().registerEvents(
                new PlayerLeashItemClickListener(), this);

        // loadPets the db connection
        getDatabaseConnector().initConnection();

        // get the plugin instances
        if (getServer().getPluginManager().getPlugin("UUIDDB") == null) {
            getLogger().severe("Plugin 'UUIDDB' was not found!");
            getServer().getPluginManager().disablePlugin(this);
        }
        if (getServer().getPluginManager().getPlugin("MbAnimalProtect") != null) {
            getLogger().info("Plugin 'MbAnimalProtect' was not found!");
        }
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            getLogger().info("Plugin 'Vault' was not found!");
        }
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
        return (MbPets) Bukkit.getServer().getPluginManager()
                .getPlugin("MbPetsNoLD");
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
     * @return the vault instance
     */
    public Vault getPluginVault() {
        return (Vault) MbPets.getInstance().getServer().getPluginManager().getPlugin("Vault");
    }

    /**
     * @return world guard instance
     */
    public WorldGuardPlugin getPluginWorldGuard() {
        return (WorldGuardPlugin) MbPets.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
    }

    /**
     * returns the servers Economy
     *
     * @return Vault economy object
     */
    public Economy getEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = MbPets
                .getInstance().getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        Economy economy = null;
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
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
}
