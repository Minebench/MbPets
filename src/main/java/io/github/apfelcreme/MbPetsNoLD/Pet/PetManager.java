package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
    private Map<UUID, Pet> pets;

    /**
     * a {@link HashMap} which contains all pets
     */
    private Map<UUID, Pet> petEntities;

    /**
     * a {@link HashMap} which contains players and the last time their pet died
     * pets
     */
    private Map<UUID, Long> cooldowns;

    /**
     * a {@link HashMap} which contains all current configurations (= Pet which
     * are currently in configuration and not yet spawned nor confirmed)
     */
    private Map<UUID, PetConfiguration> configurations;

    /**
     * The {@link LinkedHashMap} of all levels in ascending order and with the level number as the key
     */
    private Map<Integer, PetLevel> levels;

    /**
     * constructor
     */
    private PetManager() {
        pets = new HashMap<>();
        petEntities = new HashMap<>();
        cooldowns = new HashMap<>();
        configurations = new HashMap<>();

        loadLevels();
    }

    private void loadLevels() {
        Map<Integer, PetLevel> levels = new LinkedHashMap<>();
        levels.put(0, new PetLevel(0, 0, 0, null, 0));
        ConfigurationSection levelConfig = MbPets.getInstance().getConfig().getConfigurationSection("level");
        for (String key : levelConfig.getKeys(false)) {
            int level = Integer.parseInt(key);
            double attackStrengthModifier = MbPets.getInstance().getConfig().getDouble("level." + key + ".attackStrengthModifier");
            double receivedDamageModifier = MbPets.getInstance().getConfig().getDouble("level." + key + ".receivedDamageModifier");
            int expThreshold = MbPets.getInstance().getConfig().getInt("level." + key + ".expThreshold");
            Particle particle = null;
            String particleStr = MbPets.getInstance().getConfig().getString("level." + key + ".particle");
            if (particleStr == null) {
                particleStr = MbPets.getInstance().getConfig().getString("level." + key + ".effect");
            }
            if (!particleStr.isEmpty()) {
                try {
                    particle = Particle.valueOf(particleStr);
                } catch (IllegalArgumentException e) {
                    MbPets.getInstance().getLogger().log(Level.SEVERE, "Invalid effect for level " + key + ": " + MbPets.getInstance().getConfig().getString("level." + key + ".effect"));
                }
            }
            levels.put(level, new PetLevel(level, attackStrengthModifier, receivedDamageModifier, particle, expThreshold));
        }

        this.levels = levels.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
                        return createPet(resultSet);
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
                                "WHERE player.uuid = ? " +
                                "ORDER BY pet.number");
                statement.setString(1, owner.toString());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    pets.add(createPet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
        }
        return pets;
    }

    private Pet createPet(ResultSet resultSet) throws SQLException {
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
        ItemStack callItem = new ItemStack(Material.CARROT_ON_A_STICK, 1);
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
    public Map<UUID,Pet> getPetEntities() {
        return petEntities;
    }

    /**
     * @return the cooldowns
     */
    public Map<UUID, Long> getCooldowns() {
        return cooldowns;
    }

    /**
     * @return the configurations
     */
    public Map<UUID, PetConfiguration> getConfigurations() {
        return configurations;
    }


    /**
     * @return the levels
     */
    public Map<Integer, PetLevel> getLevels() {
        return levels;
    }

    /**
     * Get the pet level object from the level number
     *
     * @param level the level
     * @return the level with that number or the first/laste one if it is below/above it
     */
    public PetLevel getLevel(int level) {
        if (levels.containsKey(level)) {
            return levels.get(level);
        } else if (level < levels.keySet().iterator().next()) {
            return levels.values().iterator().next();
        } else {
            return getLevel(levels.keySet().stream().min(Comparator.reverseOrder()).get());
        }
    }

    /**
     * Get the pet level object from the amount of exp the pet has
     *
     * @param currentExp the pets current amount of exp
     * @return the level that results from the amount of exp
     */
    public PetLevel getLevelFromExp(int currentExp) {
        PetLevel foundLevel = null;
        for (PetLevel level : levels.values()) {
            if (level.getExpThreshold() > currentExp) {
                break;
            }
            foundLevel = level;
        }
        return foundLevel;
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
