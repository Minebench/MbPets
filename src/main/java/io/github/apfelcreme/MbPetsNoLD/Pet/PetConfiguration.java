package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Dyeable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Sizeable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Styleable;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Type.*;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

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
public class PetConfiguration {

    private UUID owner = null;
    private PetType type = null;
    private int number = -1;
    private String name = null;
    private Boolean isBaby = false;
    private String color = null;
    private String style = null;
    private int size = -1;
    private int exp = 0;

    private double price = 0;
    private ConfigurationType configurationType = null;

    private Entity convertedEntity = null;

    public PetConfiguration(UUID owner, PetType type, ConfigurationType configurationType) {
        this.owner = owner;
        this.type = type;
        this.configurationType = configurationType;
        if (configurationType != null) {
            switch (configurationType) {
                case PURCHASE:
                    this.price = MbPetsConfig.getPetPrice(type);
                    this.number = PetManager.getInstance().getHighestPetNumber(owner) + 1;
                    break;
                case MODIFICATION:
                    this.price = MbPetsConfig.getModificationPrice();
                    this.number = PetManager.getInstance().getHighestPetNumber(owner);
                    break;
                case CONVERSION:
                    this.price = MbPetsConfig.getPetPrice(type);
                    this.number = PetManager.getInstance().getHighestPetNumber(owner) + 1;
                    break;
            }
        }
    }

    public PetConfiguration(Pet pet, ConfigurationType configurationType) {
        this.owner = pet.getOwner();
        this.type = pet.getType();
        this.name = pet.getName();
        this.exp = pet.getExp();

        if (pet instanceof Ageable) this.isBaby = ((Ageable) pet).isBaby();
        if (pet instanceof Dyeable) this.color = String.valueOf(((Dyeable) pet).getColor());
        if (pet instanceof Styleable) this.style = String.valueOf(((Styleable) pet).getStyle());
        if (pet instanceof Sizeable) this.size = ((Sizeable) pet).getSize();

        this.configurationType = configurationType;
        switch (configurationType) {
            case PURCHASE:
                this.price = MbPetsConfig.getPetPrice(type);
                this.number = pet.getNumber() + 1;
                break;
            case MODIFICATION:
                this.price = MbPetsConfig.getModificationPrice();
                this.number = pet.getNumber();
                break;
            case CONVERSION:
                this.price = MbPetsConfig.getPetPrice(type);
                this.number = pet.getNumber() + 1;
                break;
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getBaby() {
        return isBaby;
    }

    public void setBaby(boolean baby) {
        isBaby = baby;
    }

    public Horse.Style getHorseStyle() {
        return horseStyle;
    }

    public void setHorseStyle(Horse.Style horseStyle) {
        this.horseStyle = horseStyle;
    }

    public Horse.Color getHorseColor() {
        return horseColor;
    }

    public void setHorseColor(Horse.Color horseColor) {
        this.horseColor = horseColor;
    }

    public DyeColor getSheepColor() {
        return sheepColor;
    }

    public void setSheepColor(DyeColor sheepColor) {
        this.sheepColor = sheepColor;
    }

    public DyeColor getWolfColor() {
        return wolfColor;
    }

    public void setWolfColor(DyeColor wolfColor) {
        this.wolfColor = wolfColor;
    }

    public Cat.Type getCatType() {
        return catType;
    }

    public void setCatType(Cat.Type catType) {
        this.catType = catType;
    }

    public Rabbit.Type getRabbitType() {
        return rabbitType;
    }

    public void setRabbitType(Rabbit.Type rabbitType) {
        this.rabbitType = rabbitType;
    }

    public Llama.Color getLlamaColor() {
        return llamaColor;
    }

    public void setLlamaColor(Llama.Color llamaColor) {
        this.llamaColor = llamaColor;
    }

    public Parrot.Variant getParrotColor() {
        return parrotColor;
    }

    public void setParrotColor(Parrot.Variant parrotColor) {
        this.parrotColor = parrotColor;
    }

    public Fox.Type getFoxType() {
        return foxType;
    }

    public void setFoxType(Fox.Type foxType) {
        this.foxType = foxType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ConfigurationType getConfigurationType() {
        return configurationType;
    }

    public void setConfigurationType(ConfigurationType configurationType) {
        this.configurationType = configurationType;
    }

    public Entity getConvertedEntity() {
        return convertedEntity;
    }

    public void setConvertedEntity(Entity convertedEntity) {
        this.convertedEntity = convertedEntity;
    }

    /**
     * confirms the pet and writes its attributes to the db
     */
    public void confirm() {
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), () -> {
            Connection connection = MbPets.getInstance().getDatabaseConnector().getConnection();
            try {
                PreparedStatement preparedStatement;
                //Insert a player;
                preparedStatement = connection.prepareStatement("INSERT IGNORE INTO MbPets_Player (uuid, playername) VALUES (?, ?)");
                preparedStatement.setString(1, owner.toString());
                preparedStatement.setString(2, MbPets.getInstance().getServer().getPlayer(getOwner()).getName());
                preparedStatement.executeUpdate();
                preparedStatement.close();

                //Insert a pet
                preparedStatement = connection.prepareStatement("INSERT INTO " +
                        "MbPets_Pet(playerid, petname, type, baby, color, style, size, number, exp)"
                        + " VALUES ("
                        + "(Select playerid from MbPets_Player where uuid = ?),"
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?"
                        + ")");
                preparedStatement.setString(1, owner.toString());
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, type.toString());
                preparedStatement.setBoolean(4, isBaby);
                preparedStatement.setString(5, color);
                preparedStatement.setString(6, style);
                preparedStatement.setInt(7, size);
                preparedStatement.setInt(8, number);
                preparedStatement.setInt(9, exp);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
            }
        });
    }

    /**
     * returns a pet object, creates a new one if not set yet!
     * @return a pet object
     */
    public Pet getPet() {
        Pet pet = null;
        if (type != null) {
            try {
                pet = type.getPetClass().getConstructor(UUID.class, Integer.class).newInstance(owner, number);
            } catch (NoSuchMethodException e) {
                pet = new Pet(owner, type, number);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                MbPets.getInstance().getLogger().log(Level.SEVERE, "Error while creating pet instance!", e);
                return null;
            }
            if (pet instanceof Dyeable dyeable) {
                dyeable.setColor(dyeable.parseColor(color));
            }
            if (pet instanceof Styleable styleable) {
                styleable.setStyle(styleable.parseStyle(style));
            }
            pet.setSpeed(MbPetsConfig.getPetSpeed(type));
            pet.setPrice(MbPetsConfig.getPetPrice(type));
            pet.setName(name);
            pet.setExp(exp);
            if (pet instanceof Ageable ageable) {
                ageable.setBaby(isBaby);
            }
        }
        return pet;
    }

    /**
     * checks if all necessary attributes for a pets creation have been set
     *
     * @return is the pet configuration finished or not
     */
    public boolean isFinished() {
        if (name == null || price <= 0) {
            return false;
        }
        if (Dyeable.class.isAssignableFrom(type.getPetClass()) && color == null) {
            return false;
        }
        if (Styleable.class.isAssignableFrom(type.getPetClass()) && style == null) {
            return false;
        }
        if (Sizeable.class.isAssignableFrom(type.getPetClass()) && size == -1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PetConfiguration{" +
                "owner=" + owner +
                ", type=" + type +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", isBaby=" + isBaby +
                ", color=" + color +
                ", style=" + style +
                ", slimeSize=" + size +
                ", exp=" + exp +
                ", price=" + price +
                ", configurationType=" + configurationType +
                ", convertedEntity=" + convertedEntity +
                '}';
    }

    /**
     * returns a description object
     *
     * @return a description object containing all information about this pet
     */
    public PetDescription getPetDescription() {
        return new PetDescription(this);
    }

    /**
     * which type?
     */
    public enum ConfigurationType {
        PURCHASE, MODIFICATION, CONVERSION
    }
}
