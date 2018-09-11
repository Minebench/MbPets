package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Type.*;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
public class PetConfiguration {

    private UUID owner = null;
    private PetType type = null;
    private Integer number = null;
    private String name = null;
    private Boolean isBaby = false;
    private Horse.Style horseStyle = null;
    private Horse.Color horseColor = null;
    private DyeColor sheepColor = null;
    private DyeColor wolfColor = null;
    private Ocelot.Type ocelotType = null;
    private Rabbit.Type rabbitType = null;
    private Llama.Color llamaColor = null;
    private Parrot.Variant parrotColor = null;
    private Integer slimeSize = null;
    private Integer exp = 0;

    private Double price = null;
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
        switch (type) {
            case HORSE:
                horseColor = ((HorsePet) pet).getColor();
                horseStyle = ((HorsePet) pet).getStyle();
                isBaby = ((HorsePet) pet).isBaby();
                break;
            case PIG:
                isBaby = ((PigPet) pet).isBaby();
                break;
            case SHEEP:
                sheepColor = ((SheepPet) pet).getColor();
                isBaby = ((SheepPet) pet).isBaby();
                break;
            case WOLF:
                wolfColor = ((WolfPet) pet).getColor();
                isBaby = ((WolfPet) pet).isBaby();
                break;
            case CHICKEN:
                isBaby = ((ChickenPet) pet).isBaby();
                break;
            case COW:
                isBaby = ((CowPet) pet).isBaby();
                break;
            case MUSHROOM_COW:
                isBaby = ((MooshroomPet) pet).isBaby();
                break;
            case OCELOT:
                isBaby = ((OcelotPet) pet).isBaby();
                ocelotType = ((OcelotPet) pet).getStyle();
                break;
            case POLAR_BEAR:
                isBaby = ((PolarBearPet) pet).isBaby();
                break;
            case RABBIT:
                isBaby = ((RabbitPet) pet).isBaby();
                rabbitType = ((RabbitPet) pet).getStyle();
                break;
            case PARROT:
                parrotColor = ((ParrotPet) pet).getColor();
                break;
            case SKELETON_HORSE:
                isBaby = ((SkeletonHorsePet) pet).isBaby();
                break;
            case UNDEAD_HORSE:
                isBaby = ((UndeadHorsePet) pet).isBaby();
                break;
            case DONKEY:
                isBaby = ((DonkeyPet) pet).isBaby();
                break;
            case MULE:
                isBaby = ((MulePet) pet).isBaby();
                break;
            case LLAMA:
                llamaColor = ((LlamaPet) pet).getColor();
                break;
            case MAGMA_CUBE:
                slimeSize = ((MagmaCubePet) pet).getSize();
                break;
            case SLIME:
                slimeSize = ((SlimePet) pet).getSize();
                break;
            case TURTLE:
                isBaby = ((TurtlePet) pet).isBaby();
                break;
        }
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBaby() {
        return isBaby;
    }

    public void setBaby(Boolean baby) {
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

    public Ocelot.Type getOcelotType() {
        return ocelotType;
    }

    public void setOcelotType(Ocelot.Type ocelotType) {
        this.ocelotType = ocelotType;
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

    public Integer getSlimeSize() {
        return slimeSize;
    }

    public void setSlimeSize(Integer slimeSize) {
        this.slimeSize = slimeSize;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
                        "MbPets_Pet(playerid, petname, type, baby, sheepcolor, wolfcolor, horsecolor, horsestyle, ocelottype, rabbittype, llamacolor, parrotcolor, slimesize, number, exp)"
                        + " VALUES ("
                        + "(Select playerid from MbPets_Player where uuid = ?),"
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
                        + ")");
                preparedStatement.setString(1, owner.toString());
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, type.toString());
                preparedStatement.setBoolean(4, isBaby);
                preparedStatement.setString(5, sheepColor != null ? sheepColor.name() : null);
                preparedStatement.setString(6, wolfColor != null ? wolfColor.name() : null);
                preparedStatement.setString(7, horseColor != null ? horseColor.name() : null);
                preparedStatement.setString(8, horseStyle != null ? horseStyle.name() : null);
                preparedStatement.setString(9, ocelotType != null ? ocelotType.name() : null);
                preparedStatement.setString(10, rabbitType != null ? rabbitType.name() : null);
                preparedStatement.setString(11, llamaColor != null ? llamaColor.name() : null);
                preparedStatement.setString(12, parrotColor != null ? parrotColor.name() : null);
                preparedStatement.setInt(13, slimeSize != null ? slimeSize : -1);
                preparedStatement.setInt(14, number);
                preparedStatement.setInt(15, exp);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MbPets.getInstance().getDatabaseConnector().closeConnection(connection);
            }
        });
    }

    /**
     * returns a pet object
     * @return a pet object
     */
    public Pet getPet() {
        Pet pet = null;
        if (type != null) {
            switch (type) {
                case HORSE:
                    pet = new HorsePet(owner, number);
                    ((HorsePet) pet).setBaby(isBaby);
                    ((HorsePet) pet).setColor(horseColor);
                    ((HorsePet) pet).setStyle(horseStyle);
                    break;
                case PIG:
                    pet = new PigPet(owner, number);
                    ((PigPet) pet).setBaby(isBaby);
                    break;
                case SHEEP:
                    pet = new SheepPet(owner, number);
                    ((SheepPet) pet).setBaby(isBaby);
                    ((SheepPet) pet).setColor(sheepColor);
                    break;
                case WOLF:
                    pet = new WolfPet(owner, number);
                    ((WolfPet) pet).setBaby(isBaby);
                    ((WolfPet) pet).setColor(wolfColor);
                    break;
                case CHICKEN:
                    pet = new ChickenPet(owner, number);
                    ((ChickenPet) pet).setBaby(isBaby);
                    break;
                case COW:
                    pet = new CowPet(owner, number);
                    ((CowPet) pet).setBaby(isBaby);
                    break;
                case MUSHROOM_COW:
                    pet = new MooshroomPet(owner, number);
                    ((MooshroomPet) pet).setBaby(isBaby);
                    break;
                case OCELOT:
                    pet = new OcelotPet(owner, number);
                    ((OcelotPet) pet).setBaby(isBaby);
                    ((OcelotPet) pet).setStyle(ocelotType);
                    break;
                case POLAR_BEAR:
                    pet = new PolarBearPet(owner, number);
                    ((PolarBearPet) pet).setBaby(isBaby);
                    break;
                case BAT:
                    pet = new BatPet(owner, number);
                    break;
                case IRON_GOLEM:
                    pet = new IronGolemPet(owner, number);
                    break;
                case RABBIT:
                    pet = new RabbitPet(owner, number);
                    ((RabbitPet) pet).setBaby(isBaby);
                    ((RabbitPet) pet).setStyle(rabbitType);
                    break;
                case PARROT:
                    pet = new ParrotPet(owner, number);
                    ((ParrotPet) pet).setColor(parrotColor);
                    break;
                case SKELETON_HORSE:
                    pet = new SkeletonHorsePet(owner, number);
                    ((SkeletonHorsePet) pet).setBaby(isBaby);
                    break;
                case UNDEAD_HORSE:
                    pet = new UndeadHorsePet(owner, number);
                    ((UndeadHorsePet) pet).setBaby(isBaby);
                    break;
                case DONKEY:
                    pet = new DonkeyPet(owner, number);
                    ((DonkeyPet) pet).setBaby(isBaby);
                    break;
                case MULE:
                    pet = new MulePet(owner, number);
                    ((MulePet) pet).setBaby(isBaby);
                    break;
                case LLAMA:
                    pet = new LlamaPet(owner, number);
                    ((LlamaPet) pet).setBaby(isBaby);
                    ((LlamaPet) pet).setColor(llamaColor);
                    break;
                case ENDERMAN:
                    pet = new EndermanPet(owner, number);
                    break;
                case MAGMA_CUBE:
                    pet = new MagmaCubePet(owner, number);
                    ((MagmaCubePet) pet).setSize(slimeSize);
                    break;
                case SLIME:
                    pet = new SlimePet(owner, number);
                    ((SlimePet) pet).setSize(slimeSize);
                    break;
                case VEX:
                    pet = new VexPet(owner, number);
                    break;
                case TURTLE:
                    pet = new TurtlePet(owner, number);
                    ((TurtlePet) pet).setBaby(isBaby);
                    break;
                default:
                    pet = new Pet(owner, type, number);
            }
            pet.setNumber(number);
            pet.setOwner(owner);
            pet.setSpeed(MbPetsConfig.getPetSpeed(type));
            pet.setPrice(MbPetsConfig.getPetPrice(type));
            pet.setName(name);
            pet.setExp(exp);
        }
        return pet;
    }

    /**
     * checks if all necessary attributes for a pets creation have been set
     *
     * @return is the pet configuration finished or not
     */
    public boolean isFinished() {
        if (name == null) {
            return false;
        }
        switch (type) {
            case HORSE:
                return (horseColor != null && horseStyle != null);
            case SHEEP:
                return (sheepColor != null);
            case WOLF:
                return (wolfColor != null);
            case OCELOT:
                return (ocelotType != null);
            case RABBIT:
                return (rabbitType != null);
            case PARROT:
                return (parrotColor != null);
            case LLAMA:
                return (llamaColor != null);
            case MAGMA_CUBE:
                return (slimeSize != null);
            case SLIME:
                return (slimeSize != null);
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
                ", horseStyle=" + horseStyle +
                ", horseColor=" + horseColor +
                ", sheepColor=" + sheepColor +
                ", wolfColor=" + wolfColor +
                ", ocelotType=" + ocelotType +
                ", rabbitType=" + rabbitType +
                ", llamaColor=" + llamaColor +
                ", parrotColor=" + parrotColor +
                ", slimeSize=" + slimeSize +
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
