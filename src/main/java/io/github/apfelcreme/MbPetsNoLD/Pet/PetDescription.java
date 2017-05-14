package io.github.apfelcreme.MbPetsNoLD.Pet;

import io.github.apfelcreme.MbPetsNoLD.Interface.Ageable;
import io.github.apfelcreme.MbPetsNoLD.Interface.Sizeable;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Type.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Rabbit;

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
public class PetDescription {

    private PetType type = null;
    private UUID owner = null;
    private Integer number = null;
    private String name = null;
    private Boolean isBaby = null;
    private Horse.Style horseStyle = null;
    private Horse.Color horseColor = null;
    private DyeColor sheepColor = null;
    private DyeColor wolfColor = null;
    private Ocelot.Type ocelotType = null;
    private Rabbit.Type rabbitType = null;
    private Llama.Color llamaColor = null;
    private Integer slimeSize = null;
    private Integer exp = null;
    private Double price = null;

    /**
     * creates a description for a pet object
     *
     * @param pet the pet object
     */
    protected PetDescription(Pet pet) {
        this.type = pet.getType();
        this.owner = pet.getOwner();
        this.number = pet.getNumber();
        this.name = pet.getName();
        this.price = pet.getPrice();
        this.exp = pet.getExp();
        if (pet instanceof Ageable) this.isBaby = ((Ageable) pet).isBaby();
        if (pet instanceof HorsePet) this.horseColor = ((HorsePet) pet).getColor();
        if (pet instanceof HorsePet) this.horseStyle = ((HorsePet) pet).getStyle();
//        if (pet instanceof MulePet) this.horseColor = ((MulePet) pet).getColor();
//        if (pet instanceof MulePet) this.horseStyle = ((MulePet) pet).getStyle();
        if (pet instanceof SheepPet) this.sheepColor = ((SheepPet) pet).getColor();
        if (pet instanceof WolfPet) this.wolfColor = ((WolfPet) pet).getColor();
        if (pet instanceof OcelotPet) this.ocelotType = ((OcelotPet) pet).getStyle();
        if (pet instanceof RabbitPet) this.rabbitType = ((RabbitPet) pet).getStyle();
        if (pet instanceof LlamaPet) this.llamaColor = ((LlamaPet) pet).getColor();
        if (pet instanceof Sizeable) this.slimeSize = ((Sizeable) pet).getSize();
    }

    /**
     * creates a description for a petConfiguration object
     *
     * @param petConfiguration the configuration object
     */
    protected PetDescription(PetConfiguration petConfiguration) {
        this.type = petConfiguration.getType();
        this.owner = petConfiguration.getOwner();
        this.number = petConfiguration.getNumber();
        this.name = petConfiguration.getName();
        this.price = petConfiguration.getPrice();
        this.exp = petConfiguration.getExp();
        this.isBaby = petConfiguration.getBaby();
        this.horseColor = petConfiguration.getHorseColor();
        this.horseStyle = petConfiguration.getHorseStyle();
        this.sheepColor = petConfiguration.getSheepColor();
        this.wolfColor = petConfiguration.getWolfColor();
        this.ocelotType = petConfiguration.getOcelotType();
        this.rabbitType = petConfiguration.getRabbitType();
        this.llamaColor = petConfiguration.getLlamaColor();
        this.slimeSize = petConfiguration.getSlimeSize();
    }

    /**
     * returns an information String which is later sent to the user
     *
     * @return an information String which is later sent to the user
     */
    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MbPetsConfig.getTextNode("info.StatusHeader").replace("{0}", MbPetsConfig.getNode("PetTypes." + type.name() + ".displaytext"))).append("\n");
        stringBuilder.append(getLightDescription());
        return stringBuilder.toString();

    }

    /**
     * returns a lightweight description that does not include the header and footer
     *
     * @return a lightweight description that does not include the header and footer
     */
    public String getLightDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Typ").replace("{1}", MbPetsConfig.getNode("PetTypes." + type.name() + ".displaytext"))).append("\n");
        stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Name").replace("{1}", name != null && !name.isEmpty() ? Pet.stripName(name) : ChatColor.DARK_GRAY + MbPetsConfig.getTextNode("help.NAME"))).append("\n");
        switch (type) {

            case HORSE:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", horseColor != null ? MbPetsConfig.getNode("HorseColors." + horseColor.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableHorseColors(), ", "))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Style").replace("{1}", horseStyle != null ? MbPetsConfig.getNode("HorseStyles." + horseStyle.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableHorseStyles(), ", "))).append("\n");
                break;
            case PIG:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case SHEEP:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", sheepColor != null ? MbPetsConfig.getNode("DyeColors." + sheepColor.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableDyeColors(), ", "))).append("\n");
                break;
            case WOLF:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", wolfColor != null ? MbPetsConfig.getNode("DyeColors." + wolfColor.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableDyeColors(), ", "))).append("\n");
                break;
            case CHICKEN:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case COW:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case MUSHROOM_COW:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case OCELOT:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Style").replace("{1}", ocelotType != null ? MbPetsConfig.getNode("OcelotTypes." + ocelotType.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableOcelotStyles(), ", "))).append("\n");
                break;
            case POLAR_BEAR:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case BAT:
                break;
            case IRON_GOLEM:
                break;
            case RABBIT:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Style").replace("{1}", rabbitType != null ? MbPetsConfig.getNode("RabbitTypes." + rabbitType.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableRabbitTypes(), ", "))).append("\n");
                break;
            case SKELETON_HORSE:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case UNDEAD_HORSE:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case DONKEY:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                break;
            case MULE:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
//                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", horseColor != null ? MbPetsConfig.getNode("HorseColors." + horseColor.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableHorseColors(), ", "))).append("\n");
//                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Style").replace("{1}", horseStyle != null ? MbPetsConfig.getNode("HorseStyles." + horseStyle.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableHorseStyles(), ", "))).append("\n");
                break;
            case LLAMA:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Baby").replace("{1}", WordUtils.capitalize(Boolean.toString(isBaby).toLowerCase()))).append("\n");
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Color").replace("{1}", llamaColor != null ? MbPetsConfig.getNode("LlamaColors." + llamaColor.name() + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableLlamaColors(), ", "))).append("\n");
                break;
            case ENDERMAN:
                break;
            case MAGMA_CUBE:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Size").replace("{1}", slimeSize != null ? MbPetsConfig.getNode("SlimeSizes." + slimeSize + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableSlimeSizes(), ", "))).append("\n");
                break;
            case SLIME:
                stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Size").replace("{1}", slimeSize != null ? MbPetsConfig.getNode("SlimeSizes." + slimeSize + ".displaytext") : ChatColor.DARK_GRAY + StringUtils.join(MbPetsConfig.getAvailableSlimeSizes(), ", "))).append("\n");
                break;
            default:

                break;
        }
        stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Exp").replace("{1}", exp.toString())).append("\n");
        if (price != null) {
            stringBuilder.append(MbPetsConfig.getTextNode("info.Element").replace("{0}", "Wert").replace("{1}", price.toString())).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * returns a String that is appended when a player enters /pet list
     *
     * @return a String that is appended when a player enters /pet list
     */
    public String getListDescription() {
        return MbPetsConfig
                .getTextNode("info.listElement")
                .replace(
                        "{0}", number.toString())
                .replace(
                        "{1}", name == null || name.isEmpty() ? "unbenannt"
                                : name)
                .replace(
                        "{2}", MbPetsConfig.getNode("PetTypes." + type.name() + ".displaytext"))
                .replace(
                        "{3}", "Lv. " + PetLevel.from(exp).getLevel().toString());
    }

    public String getLogDescription() {
        return "Pet " + name + " (" + owner.toString() + "): number=" + number +
                ", isBaby=" + isBaby +
                ", horseStyle=" + horseStyle +
                ", horseColor=" + horseColor +
                ", sheepColor=" + sheepColor +
                ", wolfColor=" + wolfColor +
                ", ocelotType=" + ocelotType +
                ", rabbitType=" + rabbitType +
                ", llamaColor=" + llamaColor +
                ", slimeSize=" + slimeSize +
                ", exp=" + exp +
                ", price=" + price;
    }

}