package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
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
        Map<Integer, PetLevel> levels = new TreeMap<>();
        levels.put(0, new PetLevel(0, 0, 0, 0, null, 0, 0.01, null, 0));
        ConfigurationSection levelsConfig = MbPets.getInstance().getConfig().getConfigurationSection("level");
        for (String key : levelsConfig.getKeys(false)) {
            ConfigurationSection levelConfig = levelsConfig.getConfigurationSection(key);
            int level = Integer.parseInt(key);
            double attackStrengthModifier = levelConfig.getDouble("attackStrengthModifier");
            double receivedDamageModifier = levelConfig.getDouble("receivedDamageModifier");
            double regenerationModifier = levelConfig.getDouble("regenerationModifier");
            int expThreshold = levelConfig.getInt("expThreshold");
            Particle particle = null;
            String particleStr = levelConfig.getString("particle");
            if (particleStr == null) {
                particleStr = levelConfig.getString("effect");
            }
            int particleCount = levelsConfig.getInt("particleCount", 32);
            double particleExtra = levelConfig.getDouble("particleExtra", 0.01);
            Object particleData = levelConfig.get("particleData", null);
            if (!particleStr.isEmpty()) {
                try {
                    particle = Particle.valueOf(particleStr.toUpperCase());
                    if (particleData != null) {
                        switch (particle) {
                            case REDSTONE:
                                Color color = Color.RED;
                                float size = 1;
                                if (particleData instanceof String) {
                                    String[] parts = ((String) particleData).split(",");
                                    if (parts.length == 1) {
                                        size = Float.parseFloat(parts[0]);
                                    } else if (parts.length >= 3) {
                                        color = Color.fromRGB(
                                                Integer.parseInt(parts[0]),
                                                Integer.parseInt(parts[1]),
                                                Integer.parseInt(parts[2])
                                        );
                                        if (parts.length >= 4) {
                                            size = Float.parseFloat(parts[3]);
                                        }
                                    }
                                } else if (particleData instanceof Float) {
                                    size = (float) particleData;
                                } else if (particleData instanceof Integer) {
                                    color = Color.fromRGB((int) particleData);
                                }
                                particleData = new Particle.DustOptions(color, size);
                                break;
                            case ITEM_CRACK:
                                particleData = new ItemStack(Material.matchMaterial(String.valueOf(particleData)));
                                break;
                            case BLOCK_CRACK:
                            case BLOCK_DUST:
                            case FALLING_DUST:
                                particleData = Bukkit.createBlockData(String.valueOf(particleData));
                                break;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    MbPets.getInstance().getLogger().log(Level.SEVERE, "Invalid effect for level " + key + ": " + particleStr + " - " + particleData + " - " + e.getMessage());
                }
            }
            levels.put(level, new PetLevel(level, attackStrengthModifier, receivedDamageModifier, regenerationModifier, particle, particleCount, particleExtra, particleData, expThreshold));
        }

        this.levels = levels;
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
        petConfiguration.setHorseColor(MbPetsConfig.parseHorseColor(resultSet.getString("horsecolor")));
        petConfiguration.setHorseStyle(MbPetsConfig.parseHorseStyle(resultSet.getString("horsestyle")));
        petConfiguration.setSheepColor(MbPetsConfig.parseColor(resultSet.getString("sheepcolor")));
        petConfiguration.setWolfColor(MbPetsConfig.parseColor(resultSet.getString("wolfcolor")));
        petConfiguration.setCatType(MbPetsConfig.parseCatType(resultSet.getString("cattype")));
        petConfiguration.setRabbitType(MbPetsConfig.parseRabbitType(resultSet.getString("rabbittype")));
        petConfiguration.setLlamaColor(MbPetsConfig.parseLlamaColor(resultSet.getString("llamacolor")));
        petConfiguration.setParrotColor(MbPetsConfig.parseParrotColor(resultSet.getString("parrotcolor")));
        petConfiguration.setFoxType(MbPetsConfig.parseFoxType(resultSet.getString("foxtype")));
        petConfiguration.setSlimeSize(resultSet.getInt("slimesize"));
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
                if (resultSet.next()) {
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
