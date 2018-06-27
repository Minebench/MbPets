package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
public class PetManager {

    private static PetManager instance = null;

    /**
     * a {@link HashMap} which contains all pets
     */
    private HashMap<UUID, Pet> pets;

    /**
     * a {@link HashMap} which contains all pets
     */
    private HashMap<UUID, Pet> petEntities;

    /**
     * a {@link HashMap} which contains players and the last time their pet died
     * pets
     */
    private HashMap<UUID, Long> cooldowns;

    /**
     * a {@link HashMap} which contains all current configurations (= Pet which
     * are currently in configuration and not yet spawned nor confirmed)
     */
    private HashMap<UUID, PetConfiguration> configurations;

    /**
     * constructor
     */
    private PetManager() {
        pets = new HashMap<>();
        petEntities = new HashMap<>();
        cooldowns = new HashMap<>();
        configurations = new HashMap<>();
    }

    /**
     * loads a pet
     *
     * @param owner  the pets owner
     * @param number the number
     * @return the pet object
     */
    public Pet loadPet(UUID owner, Integer number) {
        Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
        try {
            if (connection != null) {
                PreparedStatement statement = connection
                        .prepareStatement("SELECT * " +
                                "FROM MbPets_Pet pet left join MbPets_Player player on pet.playerId = player.playerId " +
                                "WHERE player.uuid = ? and pet.number = ?");
                statement.setString(1, owner.toString());
                statement.setInt(2, number);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (MbPetsConfig.parseType(resultSet.getString("type")) != null) {
                        PetConfiguration petConfiguration = new PetConfiguration(
                                UUID.fromString(resultSet.getString("uuid")),
                                MbPetsConfig.parseType(resultSet.getString("type")),
                                PetConfiguration.ConfigurationType.PURCHASE);
                        petConfiguration.setNumber(resultSet.getInt("number"));
                        petConfiguration.setName(resultSet.getString("petname"));
                        petConfiguration.setBaby(resultSet.getBoolean("baby"));
                        petConfiguration.setHorseColor(MbPetsConfig.parseHorseColor(resultSet.getString("horseColor")));
                        petConfiguration.setHorseStyle(MbPetsConfig.parseHorseStyle(resultSet.getString("horseStyle")));
                        petConfiguration.setSheepColor(MbPetsConfig.parseColor(resultSet.getString("sheepColor")));
                        petConfiguration.setWolfColor(MbPetsConfig.parseColor(resultSet.getString("wolfColor")));
                        petConfiguration.setOcelotType(MbPetsConfig.parseOcelotType(resultSet.getString("ocelotType")));
                        petConfiguration.setRabbitType(MbPetsConfig.parseRabbitType(resultSet.getString("rabbitType")));
                        petConfiguration.setLlamaColor(MbPetsConfig.parseLlamaColor(resultSet.getString("llamaColor")));
                        petConfiguration.setParrotColor(MbPetsConfig.parseParrotColor(resultSet.getString("parrotColor")));
                        petConfiguration.setSlimeSize(resultSet.getInt("slimeSize"));
                        petConfiguration.setExp(resultSet.getInt("exp"));
                        return petConfiguration.getPet();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
        }
        return null;
    }

    /**
     * loads all pets of a single player
     *
     * @param owner a players uuid
     */
    public List<Pet> loadPets(UUID owner) {
        List<Pet> pets = new ArrayList<>();
        Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
        try {
            if (connection != null) {
                PreparedStatement statement = connection
                        .prepareStatement("SELECT * from MbPets_Pet pet " +
                                "left join MbPets_Player player on pet.playerId = player.playerId " +
                                "WHERE player.uuid = ?");
                statement.setString(1, owner.toString());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    PetConfiguration petConfiguration = new PetConfiguration(
                            UUID.fromString(resultSet.getString("uuid")),
                            MbPetsConfig.parseType(resultSet.getString("type")),
                            PetConfiguration.ConfigurationType.PURCHASE);
                    petConfiguration.setNumber(resultSet.getInt("number"));
                    petConfiguration.setName(resultSet.getString("petname"));
                    petConfiguration.setBaby(resultSet.getBoolean("baby"));
                    petConfiguration.setHorseColor(MbPetsConfig.parseHorseColor(resultSet.getString("horseColor")));
                    petConfiguration.setHorseStyle(MbPetsConfig.parseHorseStyle(resultSet.getString("horseStyle")));
                    petConfiguration.setSheepColor(MbPetsConfig.parseColor(resultSet.getString("sheepColor")));
                    petConfiguration.setWolfColor(MbPetsConfig.parseColor(resultSet.getString("wolfColor")));
                    petConfiguration.setOcelotType(MbPetsConfig.parseOcelotType(resultSet.getString("ocelotType")));
                    petConfiguration.setRabbitType(MbPetsConfig.parseRabbitType(resultSet.getString("rabbitType")));
                    petConfiguration.setLlamaColor(MbPetsConfig.parseLlamaColor(resultSet.getString("llamaColor")));
                    petConfiguration.setParrotColor(MbPetsConfig.parseParrotColor(resultSet.getString("parrotColor")));
                    petConfiguration.setSlimeSize(resultSet.getInt("slimeSize"));
                    petConfiguration.setExp(resultSet.getInt("exp"));
                    pets.add(petConfiguration.getPet());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
        }
        return pets;
    }

    /**
     * creates an item that can be rightclicked to call the pet
     *
     * @param pet the pet
     * @return an item
     */
    public static ItemStack createCallItem(Pet pet) {
        return createItem(pet.getName(), pet.getNumber());
    }

    /**
     * creates an item that can be rightclicked to call the pet
     *
     * @param petConfiguration the pet configuration
     * @return an item
     */
    public static ItemStack createCallItem(PetConfiguration petConfiguration) {
        return createItem(petConfiguration.getName(), petConfiguration.getNumber());
    }

    /**
     * creates a leash item
     *
     * @param name   the pet name
     * @param number the pet number
     * @return the item
     */
    private static ItemStack createItem(String name, Integer number) {
        ItemStack callItem = new ItemStack(Material.CARROT_STICK, 1);
        ItemMeta meta = callItem.getItemMeta();
        meta.setDisplayName(MbPetsConfig.getTextNode("info.leashTitle")
                .replace("{0}", ChatColor.translateAlternateColorCodes('&', name)));
        List<String> lore = new ArrayList<>();
        lore.add(MbPetsConfig.getTextNode("info.leashName").replace("{0}", ChatColor.translateAlternateColorCodes('&', name)));
        lore.add(MbPetsConfig.getTextNode("info.leashName").replace("{0}", "#" + number));
        Collections.addAll(lore, MbPetsConfig.getTextNode("info.leashLore").replace("{0}", name).split("/n"));
        meta.setLore(lore);
        callItem.setItemMeta(meta);
        return callItem;
    }

    /**
     * returns the pet object of the given entity or null if the entity isnt a
     * pet
     *
     * @param entity
     * @return
     */
    public Pet getPetByEntity(Entity entity) {
        return entity != null ? petEntities.get(entity.getUniqueId()) : null;
    }

    /**
     * returns the pet object whose target equals the given entity
     *
     * @param entity the entity
     * @return the pet with the entity as its target
     */
    public Pet getPetByTargetEntity(Entity entity) {
        for (Pet pet : pets.values()) {
            if (pet.getTarget().equals(entity)) {
                return pet;
            }
        }
        return null;
    }

    /**
     * returns the number of pets the given player owns. call async only!
     *
     * @param owner the pet owner
     * @return the number of the last pet he bought
     */
    public int getHighestPetNumber(UUID owner) {
        int ret = 0;
        Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
        try {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT Max(number) as m FROM MbPets_Pet " +
                                "WHERE playerid = (Select playerid from MbPets_Player where uuid = ?)");
                statement.setString(1, owner.toString());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.first()) {
                    return resultSet.getInt("m");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
        }
        return ret;
    }

    /**
     * returns a map of all pets
     *
     * @return a map of all pets
     */
    public Map<UUID, Pet> getPets() {
        return pets;
    }

    /**
     * returns a map of all spawned pets
     *
     * @return a map of entity UUIDs to pets
     */
    public HashMap<UUID,Pet> getPetEntities() {
        return petEntities;
    }

    /**
     * @return the cooldowns
     */
    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    /**
     * @return the configurations
     */
    public Map<UUID, PetConfiguration> getConfigurations() {
        return configurations;
    }

    /**
     * returns the PetManager instance
     *
     * @return the instance
     */
    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }
}
